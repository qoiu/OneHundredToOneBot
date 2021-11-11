package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.presenter.GameMessage;
import com.github.qoiu.main.presenter.GamePlayer;
import com.github.qoiu.main.presenter.MessageSender;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

import java.util.*;

interface Round {
    void start();

    void checkAnswer(String answer);

    boolean isActivePlayer(GamePlayer player);

    void finish();

    void subscribe(SingleObserver<GameScoreboard> observer);

    GamePlayer getActivePlayer();

    class Base implements Round {
        private final RoundType round;
        private GamePlayer activePlayer;
        private List<GamePlayer> playerList;
        private List<GamePlayer> baseList;
        private final Question currentQuestion;
        private final GameScoreboard scoreboard;
        private final Statistic statistic;
        private final MessageSender sender;
        private final Single<GameScoreboard> observable;
        private SingleObserver<GameScoreboard> singleObserver;
        private final GameActions gamePresenter;

        // TODO: 09.11.2021 Do something with sender. Too much for this class!
        public Base(RoundType roundType, GameActions gamePresenter, List<GamePlayer> list, MessageSender sender) {
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
            this.gamePresenter = gamePresenter;

            for (GamePlayer player : list) {
                ids.add(player.getId());
            }
            List<Integer> qIds = new ArrayList<>(gamePresenter.getUnansweredQuestions(ids));
            int currentQuestionId = qIds.get(new Random().nextInt(qIds.size()));
            currentQuestion = gamePresenter.getQuestion(currentQuestionId);
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
                gamePresenter.updateQuestionAnswerRate(currentQuestion, trueAnswer);
                int scoreAdded = round.setScores(currentQuestion, trueAnswer);
                scoreboard.addScores(activePlayer, scoreAdded);
                sendChatMsgToAll(activePlayer.getName() + " *отвечает* '" + answer + "'", "Правильный ответ\n" + activePlayer.getName() + " получает " + scoreAdded + " очков");
                if (checkAnsweredQuestions()) {
                    checkRoundEnd();
                }
            }
            updateQuestionForPlayers();
        }

        private void updateQuestionForPlayers() {
            String before = round.getName() + "\n" + statistic.toString();
            if (activePlayer != null) {
                for (GamePlayer player : playerList) {
                    GameMessage msg = new GameMessage(player.getId(),
                            before + ((player != activePlayer)
                                    ? activePlayer.getName() + " отвечает на вопрос:"
                                    : "Вы отвечаете на вопрос:"));
                    sender.updateQuestion(currentQuestion, msg);
                }
            }
        }

        @Override
        public boolean isActivePlayer(GamePlayer player) {
            return player.getId() == activePlayer.getId();
        }

        private void roundEndMessage() {
            currentQuestion.markAllAnswerAsAnswered();
            for (GamePlayer player : baseList) {
                GameMessage msg = new GameMessage(
                        player.getId(),
                        "Раунд окончен. Результаты:\n" + scoreboard.toString() + "\nНовый раунд начнётся через 10 секунд"
                );
                sender.updateQuestion(currentQuestion, msg);
            }
            clearChatForAll();
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            singleObserver.onSuccess(scoreboard);
        }

        private void checkRoundEnd() {
            if (statistic.checkTryAmount() || checkAnsweredQuestions()) {
                //round end
                for (GamePlayer player : playerList) {
                    gamePresenter.addUserAnswer(currentQuestion.getId(), player);
                }
                roundEndMessage();
            } else {
                //next try
                selectPlayer();
            }
        }

        public void finish() {
            activePlayer = null;
            playerList = null;
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
                sender.sendChatMessage(new GameMessage(player.getId(), text, from));
            }
        }

        private void sendAll(String text) {
            for (GamePlayer player : baseList) {
                GameMessage msg = new GameMessage(player.getId(), text);
                sender.sendMessage(msg);
            }
        }

        public void subscribe(SingleObserver<GameScoreboard> observer) {
            new Single<GameScoreboard>() {
                @Override
                protected void subscribeActual(SingleObserver<? super GameScoreboard> observer) {
                    Base.this.singleObserver = (SingleObserver<GameScoreboard>) observer;
                }
            }.subscribe(observer);
        }

        public GamePlayer getActivePlayer() {
            return activePlayer;
        }
    }
}