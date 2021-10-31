package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGetQuestion;
import com.github.qoiu.main.data.mappers.DbMapperGetUnansweredQuestionsByPlayers;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import com.github.qoiu.main.presenter.mappers.SendMapperSimpleText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

public class GamePresenter {
    private final int MIN_SAME_SYMBOL_CHECK=3;
    private final List<GamePlayer> list;
    private final DatabaseBase db;
    private Round current;
    private final MessageSender sender;
    private final PreparedSendMessages messages = new PreparedSendMessages();
    private final Map<Long,Integer> gameScores;

    public GamePresenter(DatabaseBase db, List<GamePlayer> list, MessageSender sender) {
        this.db = db;
        this.sender = sender;
        this.list = list;
        gameScores = new HashMap<>();
        for (GamePlayer player:list) {
            gameScores.put(player.getId(),0);
        }
        startNewRound();
    }

    void startNewRound(){
        current = new Round();
        current.start();
    }

    void getChatMessage(UserMessaged userMessaged) {
        String text = userMessaged.getMessage();
        if (userMessaged.getId() == current.activePlayer.getId())
            if (userMessaged.getMessage().charAt(0) != '?') {
                current.checkAnswer(userMessaged.getMessage());
                return;
            }
        // TODO: 30.10.2021 test me
        for (GamePlayer player : list) {
            sender.sendChatMessage(new BotChatMessage(
                    userMessaged.getName(),
                    text,
                    player.getId()
            ));
        }
    }

    private class Round {
        GamePlayer activePlayer;
        List<GamePlayer> playersSet = new ArrayList<>();
        private final Question currentQuestion;
        private final Statistic statistic;
        private Set<Long> ids;

        public Round() {
            ids = new HashSet<>();
            statistic = new Statistic(list);
            for (GamePlayer player : list) {
                ids.add(player.getId());
            }

            List<Integer> qIds = new ArrayList<>(new DbMapperGetUnansweredQuestionsByPlayers(db).map(ids));
            int currentQuestionId = qIds.get(new Random().nextInt(qIds.size()));
//            currentQuestionId = 322;

            currentQuestion = new QuestionDbToQuestionMapper().map(
                    new DbMapperGetQuestion(db).map(currentQuestionId));
        }

        public Statistic getStatistic() {
            return statistic;
        }


        private void updateQuestionForPlayers() {
            SendMessage msg;
            String before = current.getStatistic().getPlayersStatistic();
            for (GamePlayer player : list) {
                msg = messages.playerAnswer(
                        before + ((player != activePlayer) ? activePlayer.getName() + " отвечает на вопрос:" : "Вы отвечаете на вопрос:"),
                        player.getId(),
                        currentQuestion
                );
                sender.sendMessage(msg);
            }
        }

        private void checkAnswer(String answer) {
            Question.Answer trueAnswer = null;
            for (Question.Answer correctAnswer : currentQuestion.getAnswers()) {
                if (!correctAnswer.isAnswered())
                    if (compareStrings(answer, correctAnswer.getText()))
                        trueAnswer = correctAnswer;
            }
            if (trueAnswer == null) {
                sendAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "К сожалению, это не популярный ответ");
                statistic.decreaseTry(activePlayer);
            } else {
                trueAnswer.setAnswered();
                gameScores.put(activePlayer.getId(),currentQuestion.getPercentageOfAnswer(trueAnswer));
                sendAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "Также ответили " + currentQuestion.getPercentageOfAnswer(trueAnswer) + "%");
            }
            updateQuestionForPlayers();
            checkRoundEnd();
        }

        private void selectPlayer(){
            if(playersSet.size()==0){
                for (GamePlayer player:list) {
                    if(statistic.tryAmount.get(player)!=0)playersSet.add(player);
                }
            }
            int rand = new Random().nextInt(playersSet.size());
            activePlayer= playersSet.get(rand);
            updateQuestionForPlayers();
        }

        private void checkRoundEnd(){
            if(checkTryAmount() || checkAnsweredQuestions()){
                //round end
                startNewRound();
            }else {
                //next try
                selectPlayer();
            }
        }

        private boolean checkAnsweredQuestions() {
            for (Question.Answer answer:currentQuestion.getAnswers()) {
                if(!answer.isAnswered())return false;
            }
            return true;
        }

        private boolean checkTryAmount() {
            for (GamePlayer player:list) {
                if(statistic.tryAmount.get(player)!=0)return false;
            }
            return true;
        }

        private void sendAll(String from, String text) {
            SendMessage msg;
            for (GamePlayer player : list) {
                msg = new SendMapperSimpleText().map(new UserMessaged(player.getId(), text));
                sender.sendMessage(msg);
                sender.sendChatMessage(new BotChatMessage(from, text, player.getId()));
            }
        }

        private boolean compareStrings(String actual, String expected) {
            actual = actual.trim().toLowerCase();
            expected = expected.trim().toLowerCase();
            if (actual.contains(expected) && actual.length() >= MIN_SAME_SYMBOL_CHECK)
                return true;
            if (expected.contains(actual) && actual.length() >= MIN_SAME_SYMBOL_CHECK)
                return true;
            return testCompare(actual, expected);
        }

        private boolean testCompare(String actual, String expected) {
            final int DIFFERENCE_VALUE = 20;
            char[] charsFirst;
            char[] charsSecond;
            if (actual.length() >= expected.length()) {
                charsFirst = actual.toLowerCase().toCharArray();
                charsSecond = expected.toLowerCase().toCharArray();
            } else {
                charsFirst = expected.toLowerCase().toCharArray();
                charsSecond = actual.toLowerCase().toCharArray();
            }
            int value = 0;
            for (int i = 0; i < charsFirst.length; i++) {
                int minDiff = 15;
                for (int j = 0; j < charsSecond.length; j++) {
                    if (charsFirst[i] == charsSecond[j]) {
                        minDiff = Math.min(Math.abs(i - j), minDiff);
                    }
                }
                value += minDiff;
            }
            return value < DIFFERENCE_VALUE;
        }

        public void start() {
            for (int i = 3; i >0 ; i--) {
                clearChatForAll();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendAll("Игра начнётся через:", String.valueOf(i));
            }
            clearChatForAll();
            selectPlayer();
//            updateQuestionForPlayers();
        }

        private void clearChatForAll(){
            for (GamePlayer player:list) {
                sender.clearChat(player.getId());
            }
        }
    }

    class Statistic{
        Map<GamePlayer,Integer> tryAmount;
        private final int MAX_TRY = 3;

        public Statistic(List<GamePlayer> players) {
            this.tryAmount = new HashMap<>();
            for (GamePlayer player:players) {
                tryAmount.put(player,MAX_TRY);
            }
        }

        String getPlayersStatistic(){
            StringBuilder output= new StringBuilder();
            for (GamePlayer player:tryAmount.keySet()) {
                output.append(player.getName()).append("\n");
                for (int i = 0; i < MAX_TRY; i++) {
                    output.append((tryAmount.get(player) > i) ? "✔" : "❌");
                }
                output.append("\n");
            }
            return output.toString();
        }

        public void decreaseTry(GamePlayer activePlayer) {
            tryAmount.put(activePlayer,tryAmount.get(activePlayer)-1);
        }
    }

}
