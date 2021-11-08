package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.StateStatus;
import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.*;
import com.github.qoiu.main.presenter.mappers.SendMapperEndGame;
import com.github.qoiu.main.presenter.mappers.SendMapperLeaveGame;
import com.github.qoiu.main.presenter.mappers.SendMapperSimpleText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

public class GamePresenter {
    private final int MIN_SAME_SYMBOL_CHECK = 3;
    private final Statistic statistic;
    private final List<GamePlayer> list;
    private final DatabaseBase db;
    private Round current;
    private int currentRoundId = 0;
    private final GameScoreboard scoreboard;
    private final MessageSender sender;
    private final PreparedSendMessages messages;
    private final List<RoundType> rounds = new ArrayList<>(Arrays.asList(
            new RoundUsual(), new RoundDouble(), new RoundTriple(), new RoundReversed()));

    public GamePresenter(DatabaseBase db, List<GamePlayer> list, MessageSender sender) {
        this.db = db;
        this.sender = sender;
        this.list = list;
        statistic = new Statistic(list);
        messages = new PreparedSendMessages();
        scoreboard = new GameScoreboard(list);
        for (GamePlayer player : list) {
            new DbMapperUpdateUser(db).map(new GamePlayerToUserDbMapper(StateStatus.PLAYER_IN_GAME).map(player));
        }
        startNewRound();
    }

    void startNewRound() {
        if (current != null) current.clear();
        if (currentRoundId < rounds.size()) {
            current = new Round(rounds.get(currentRoundId));
            currentRoundId += 1;
            current.start();
        } else {
            gameEnd();
        }
    }

    private void gameEnd() {
        sendAll("Игра окончена");
        String text = scoreboard.getWinnerText() + "\n";
        for (GamePlayer player : list) {
            sender.clearChat(player.getId());
            sender.sendMessage(new SendMapperEndGame(text + scoreboard.getScoreboard())
                    .map(new GamePlayerToUserMessagedMapper().map(player)));
            int win = 0;
            if (player == scoreboard.getWinner()) win = 1;
            new DbMapperUpdateUserResults(db).map(
                    new GamePlayerResultsToUserDbMapper(1, win, scoreboard.getPlayerScores(player)).map(player));

        }
    }


    private void sendAll(String text) {
        SendMessage msg;
        for (GamePlayer player : list) {
            msg = new SendMapperSimpleText().map(new UserMessaged(player.getId(), text));
            sender.sendMessage(msg);
        }
    }

    private void sendChatMsgToAll(String from, String text) {
        for (GamePlayer player : list) {
            sender.sendChatMessage(new BotChatMessage(from, text, player.getId()));
        }
    }

    void getChatMessage(UserMessaged userMessaged) {
        String text = userMessaged.getMessage();
        if (text.equals("/leave")) {
            new SendMapperLeaveGame(db, new UserMessagedToGamePlayer().map(userMessaged));
            playerLeaveGame(userMessaged);
        }
        if (userMessaged.getId() == current.activePlayer.getId())
            if (userMessaged.getMessage().charAt(0) != '?') {
                current.checkAnswer(userMessaged.getMessage());
                sendChatMsgToAll("Счёт", scoreboard.getScoreboard());
            } else {
                sendChatMsgToAll(userMessaged.getName(), userMessaged.getMessage().substring(1));
            }
        current.updateQuestionForPlayers();
    }

    public void playerLeaveGame(UserMessaged userMessaged) {
        System.out.println("list: " + list.size());
        list.remove(new UserMessagedToGamePlayer().map(userMessaged));
        System.out.println("list after: " + list.size());
        if (list.size() == 0) current.clear();
    }

    private class Round {
        private final RoundType round;
        GamePlayer activePlayer;
        List<GamePlayer> playersSet = new ArrayList<>();
        private final Question currentQuestion;
        private final Statistic statistic;

        public Round(RoundType roundType) {
            this.round = roundType;
            statistic = new Statistic(list);
            Set<Long> ids = new HashSet<>();
            for (GamePlayer player : list) {
                ids.add(player.getId());
            }

            List<Integer> qIds = new ArrayList<>(new DbMapperGetUnansweredQuestionsByPlayers(db).map(ids));
            int currentQuestionId = qIds.get(new Random().nextInt(qIds.size()));
            currentQuestion = new QuestionDbToQuestionMapper().map(
                    new DbMapperGetQuestion(db).map(currentQuestionId));
            currentQuestion.getAnswers().sort(round.getComparator());
        }

        public Statistic getStatistic() {
            return statistic;
        }


        private void updateQuestionForPlayers() {
            SendMessage msg;
            String before = round.getName() + "\n" + current.getStatistic().getPlayersStatistic();
            for (GamePlayer player : list) {
                if (activePlayer != null) {
                    msg = messages.playerAnswer(
                            before + ((player != activePlayer) ? activePlayer.getName() + " отвечает на вопрос:" : "Вы отвечаете на вопрос:"),
                            player.getId(),
                            currentQuestion
                    );
                    sender.sendMessage(msg);
                }
            }
        }

        private void roundEndMessage() {
            SendMessage msg;
            for (Question.Answer answ : currentQuestion.getAnswers()) {
                answ.setAnswered();
            }
            for (GamePlayer player : list) {
                msg = messages.playerAnswer(
                        "Раунд окончен. Результаты:\n" + scoreboard.getScoreboard() + "\nНовый раунд начнётся через 10 секунд",
                        player.getId(),
                        currentQuestion
                );
                sender.sendMessage(msg);
            }
            clearChatForAll();
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startNewRound();
        }

        private void checkAnswer(String answer) {
            Question.Answer trueAnswer = null;
            for (Question.Answer correctAnswer : currentQuestion.getAnswers()) {
                if (!correctAnswer.isAnswered())
                    if (compareStrings(answer, correctAnswer.getText())) {
                        trueAnswer = correctAnswer;
                        trueAnswer.upgradeRate();
                        new DbMapperUpdateQuestionAnswerRate(db, currentQuestion.getId()).map(trueAnswer);
                    }
            }
            if (trueAnswer == null) {
                sendChatMsgToAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "К сожалению, это не популярный ответ");
                statistic.decreaseTry(activePlayer);
                playersSet.remove(activePlayer);
                checkRoundEnd();
            } else {
                trueAnswer.setAnswered();
                int scoreAdded = round.setScores(currentQuestion, trueAnswer);
                scoreboard.addScores(activePlayer, scoreAdded);
                sendChatMsgToAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "За этот ответ вы получаете " + scoreAdded + " очков");
                if (checkAnsweredQuestions()) {
                    checkRoundEnd();
                }
            }
        }

        private void selectPlayer() {
            if (playersSet.size() == 0) {
                for (GamePlayer player : list) {
                    if (statistic.tryAmount.get(player) != 0) playersSet.add(player);
                }
            }
            activePlayer = playersSet.get(0);
            if (activePlayer != null) updateQuestionForPlayers();
        }

        private void checkRoundEnd() {
            if (checkTryAmount() || checkAnsweredQuestions()) {
                //round end
                for (GamePlayer player : list) {
                    new DbMapperAddUserAnswer(db).map(new GamePlayerToUserDbMapper(5).map(player).setExtra(currentQuestion.getId()));
                }
                roundEndMessage();
            } else {
                //next try
                selectPlayer();
            }
        }

        void clear() {
            activePlayer = null;
            playersSet = null;
        }

        private boolean checkAnsweredQuestions() {
            for (Question.Answer answer : currentQuestion.getAnswers()) {
                if (!answer.isAnswered()) return false;
            }
            return true;
        }

        private boolean checkTryAmount() {
            for (GamePlayer player : list) {
                if (statistic.tryAmount.get(player) != 0) return false;
            }
            return true;
        }

        private boolean compareStrings(String actual, String expected) {
            actual = actual.trim().toLowerCase().replace("ё", "е");
            expected = expected.trim().toLowerCase().replace("ё", "е");
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
            sendAll(scoreboard.getScoreboard() + "\nОжидание других игроков...");
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 3; i > 0; i--) {
                sendChatMsgToAll("Игра начнётся через", String.valueOf(i));
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            clearChatForAll();
            selectPlayer();
        }

        private void clearChatForAll() {
            for (GamePlayer player : list) {
                sender.clearChat(player.getId());
            }
        }
    }


    private class Statistic {
        Map<GamePlayer, Integer> tryAmount;
        private final int MAX_TRY = 3;

        public Statistic(List<GamePlayer> players) {
            this.tryAmount = new HashMap<>();
            for (GamePlayer player : players) {
                tryAmount.put(player, MAX_TRY);
            }
        }

        String getPlayersStatistic() {
            StringBuilder output = new StringBuilder();
            for (GamePlayer player : tryAmount.keySet()) {
                output.append(player.getName()).append("\n");
                for (int i = 0; i < MAX_TRY; i++) {
                    output.append((tryAmount.get(player) > i) ? "✔" : "❌");
                }
                output.append("\n");
            }
            return output.toString();
        }

        public void decreaseTry(GamePlayer activePlayer) {
            tryAmount.put(activePlayer, tryAmount.get(activePlayer) - 1);
        }
    }
}
