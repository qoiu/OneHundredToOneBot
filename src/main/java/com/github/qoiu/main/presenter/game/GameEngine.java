package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.mappers.UserMessagedToGamePlayer;
import com.github.qoiu.main.presenter.GameMessage;
import com.github.qoiu.main.presenter.GamePlayer;
import com.github.qoiu.main.presenter.MessageSender;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface GameEngine {

    void playerLeaveGame(UserMessaged userMessaged);
    void getChatMessage(UserMessaged userMessaged);

    class Base implements GameEngine, GameInfo {
        private final List<GamePlayer> list;
        private final GameActions gamePresenter;
        private Round current;
        private int currentRoundId = 0;
        private final GameScoreboard scoreboard;
        private final MessageSender sender;
        private final List<RoundType> rounds = new ArrayList<>(Arrays.asList(
                new RoundUsual(), new RoundDouble(), new RoundTriple(), new RoundReversed()));

        public Base(GameActions gamePresenter, List<GamePlayer> list, MessageSender sender) {
            this.gamePresenter = gamePresenter;
            this.sender = sender;
            this.list = list;
            scoreboard = new GameScoreboard.Base(list);
            startNewRound();
        }

        private SingleObserver<GameScoreboard> getSingle(){
         return new SingleObserver<GameScoreboard>() {
             @Override
             public void onSubscribe(Disposable d) {

             }

             @Override
             public void onSuccess(GameScoreboard scoreboard) {
                 updateScoreboard(scoreboard);
                 startNewRound();
             }

             @Override
             public void onError(Throwable e) {

             }
         };
        }

        private void updateScoreboard(GameScoreboard scoreboard){
            this.scoreboard.update(scoreboard);
        }

        void startNewRound() {
            if (current != null) current.finish();
            if (currentRoundId < rounds.size()) {
                current = new Round.Base(rounds.get(currentRoundId), gamePresenter, list,sender);
                current.subscribe(getSingle());
                currentRoundId += 1;
                current.start();
            } else {
                gameEnd();
            }
        }

        private void gameEnd() {
            sendAll("Игра окончена");
            String text = scoreboard.getWinnerText() + "\nИгра завершена\n";
            for (GamePlayer player : list) {
                sender.clearChat(player.getId());
                GameMessage msg = new GameMessage(player.getId(),text + scoreboard);
                msg.addButton("В меню", "/menu");
                sender.sendMessage(msg);
                gamePresenter.updateUserResult(scoreboard,player);
            }
        }

        private void sendAll(String text) {
            for (GamePlayer player : list) {
                GameMessage msg = new GameMessage(player.getId(), text);
                sender.sendMessage(msg);
            }
        }

        public void getChatMessage(UserMessaged userMessaged) {
            String text = userMessaged.getMessage();
            GamePlayer player =  new UserMessagedToGamePlayer().map(userMessaged);
            if (text.equals("/leave")) {
                GameMessage msg = new GameMessage(player.getId(),"Очень жаль");
                msg.addButton("В меню", "/menu");
                sender.sendMessage(msg);
                playerLeaveGame(userMessaged);
            }
            if (current.isActivePlayer(player)) {
                if (userMessaged.getMessage().charAt(0) != '?') {
                    current.checkAnswer(userMessaged.getMessage());
                } else {
                    sendChatMsgToAll(userMessaged.getName(), userMessaged.getMessage().substring(1));
                }
            }else{
                sendChatMsgToAll(userMessaged.getName(), userMessaged.getMessage());
            }
        }

        private void sendChatMsgToAll(String header, String text) {
            for (GamePlayer player : list) {
                sender.sendChatMessage(new GameMessage(player.getId(), text, header ));
            }
        }

        public void playerLeaveGame(UserMessaged userMessaged) {
            list.remove(new UserMessagedToGamePlayer().map(userMessaged));
            if (list.size() == 0) current.finish();
        }

        @Override
        public GamePlayer getActivePlayer() {
            return current.getActivePlayer();
        }

        @Override
        public Statistic getStatistic() {
            // TODO: 11.11.2021 FIX
            return null;
        }

        @Override
        public Question getCurrentQuestion() {
            return null;
        }
    }
}
