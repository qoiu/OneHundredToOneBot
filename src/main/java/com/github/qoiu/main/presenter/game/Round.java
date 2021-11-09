package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperAddUserAnswer;
import com.github.qoiu.main.data.mappers.DbMapperGetQuestion;
import com.github.qoiu.main.data.mappers.DbMapperGetUnansweredQuestionsByPlayers;
import com.github.qoiu.main.data.mappers.DbMapperUpdateQuestionAnswerRate;
import com.github.qoiu.main.mappers.GamePlayerToUserDbMapper;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import com.github.qoiu.main.presenter.GamePlayer;
import com.github.qoiu.main.presenter.MessageSender;
import com.github.qoiu.main.presenter.mappers.SendMapperSimpleText;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

interface Round {
    void start();

    void checkAnswer(String answer);

    boolean isActivePlayer(GamePlayer player);

    GameScoreboard finish();

    void subscribe(SingleObserver<GameScoreboard> observer);

    class Base implements Round {
        private final RoundType round;
        private GamePlayer activePlayer;
        private List<GamePlayer> playerList;
        private List<GamePlayer> baseList;
        private final Question currentQuestion;
        private final PreparedSendMessages messages;
        private final GameScoreboard scoreboard;
        private final Statistic statistic;
        private DatabaseInterface.Executor db;
        private final MessageSender sender;
        private final  Single<GameScoreboard> observable;
        private SingleObserver<GameScoreboard> singleObserver;

        // TODO: 09.11.2021 Do something with sender. Too much for this class!
        public Base(RoundType roundType, DatabaseInterface.Executor db, List<GamePlayer> list, MessageSender sender) {
            this.sender = sender;
            this.round = roundType;
            this.scoreboard = new GameScoreboard.Base(list);
            this.playerList = new ArrayList<>(list);
            this.baseList = new ArrayList<>(list);
            statistic = new Statistic.Base(new ArrayList<>(list));
            Set<Long> ids = new HashSet<>();
            observable = new Single<GameScoreboard>() {
                @Override
                protected void subscribeActual(SingleObserver<? super GameScoreboard> observer) {
                    Base.this.singleObserver = (SingleObserver<GameScoreboard>) observer;
                }
            };
            this.db = db;
            messages = new PreparedSendMessages();

            for (GamePlayer player : list) {
                ids.add(player.getId());
            }
            List<Integer> qIds = new ArrayList<>(new DbMapperGetUnansweredQuestionsByPlayers(db).map(ids));
            int currentQuestionId = qIds.get(new Random().nextInt(qIds.size()));
            currentQuestion = new QuestionDbToQuestionMapper().map(
                    new DbMapperGetQuestion(db).map(currentQuestionId));
            currentQuestion.getAnswers().sort(round.getComparator());
        }

        public void start() {
            sendAll("\nОжидание других игроков...");
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

        private void selectPlayer() {
            if (playerList.size() == 0) {
                for (GamePlayer player : baseList) {
                    if (statistic.getAmount(player) != 0) playerList.add(player);
                }
            }
            activePlayer = playerList.get(0);
            if (activePlayer != null) updateQuestionForPlayers();
        }

        public void checkAnswer(String answer) {
            Question.Answer trueAnswer = currentQuestion.checkTrueAnswer(answer);
            if (trueAnswer == null) {
                sendChatMsgToAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "К сожалению, это не популярный ответ");
                statistic.decreaseTry(activePlayer);
                playerList.remove(activePlayer);
                checkRoundEnd();
            } else {
                trueAnswer.setAnswered();
                new DbMapperUpdateQuestionAnswerRate(db, currentQuestion.getId()).map(trueAnswer);
                int scoreAdded = round.setScores(currentQuestion, trueAnswer);
                scoreboard.addScores(activePlayer, scoreAdded);
                sendChatMsgToAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "За этот ответ вы получаете " + scoreAdded + " очков");
                if (checkAnsweredQuestions()) {
                    checkRoundEnd();
                }
            }
            updateQuestionForPlayers();
        }

        private void updateQuestionForPlayers() {
            SendMessage msg;
            String before = round.getName() + "\n" + statistic.toString();
            for (GamePlayer player : playerList) {
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

        @Override
        public boolean isActivePlayer(GamePlayer player) {
            return player.getId() == activePlayer.getId();
        }

        private void roundEndMessage() {
            SendMessage msg;
            currentQuestion.markAllAnswerAsAnswered();
            for (GamePlayer player : baseList) {
                msg = messages.playerAnswer(
                        "Раунд окончен. Результаты:\n" + scoreboard.toString() + "\nНовый раунд начнётся через 10 секунд",
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
            // TODO: 09.11.2021 fixme
//            observer.onNext(scoreboard);
//            observer.onComplete();
//            startNewRound();
            singleObserver.onSuccess(scoreboard);
        }

        private void checkRoundEnd() {
            if (statistic.checkTryAmount() || checkAnsweredQuestions()) {
                //round end
                for (GamePlayer player : playerList) {
                    new DbMapperAddUserAnswer(db).map(new GamePlayerToUserDbMapper(5).map(player).setExtra(currentQuestion.getId()));
                }
                roundEndMessage();
            } else {
                //next try
                selectPlayer();
            }
        }

        public GameScoreboard finish() {
            activePlayer = null;
            playerList = null;
            return scoreboard;
        }

        private boolean checkAnsweredQuestions() {
            for (Question.Answer answer : currentQuestion.getAnswers()) {
                if (!answer.isAnswered()) return false;
            }
            return true;
        }

        private void clearChatForAll() {
            for (GamePlayer player : baseList) {
                sender.clearChat(player.getId());
            }
        }

        private void sendChatMsgToAll(String from, String text) {
            for (GamePlayer player : baseList) {
                sender.sendChatMessage(new BotChatMessage(from, text, player.getId()));
            }
        }

        private void sendAll(String text) {
            SendMessage msg;
            for (GamePlayer player : baseList) {
                msg = new SendMapperSimpleText().map(new UserMessaged(player.getId(), text));
                sender.sendMessage(msg);
            }
        }

        public void subscribe(SingleObserver<GameScoreboard> observer){
            new Single<GameScoreboard>(){
                @Override
                protected void subscribeActual(SingleObserver<? super GameScoreboard> observer) {
                    Base.this.singleObserver = (SingleObserver<GameScoreboard>) observer;
                }
            }.subscribe(observer);
        }
    }
}