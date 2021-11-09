package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.StateStatus;
import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperUpdateUser;
import com.github.qoiu.main.data.mappers.DbMapperUpdateUserResults;
import com.github.qoiu.main.mappers.GamePlayerResultsToUserDbMapper;
import com.github.qoiu.main.mappers.GamePlayerToUserDbMapper;
import com.github.qoiu.main.mappers.GamePlayerToUserMessagedMapper;
import com.github.qoiu.main.mappers.UserMessagedToGamePlayer;
import com.github.qoiu.main.presenter.GamePlayer;
import com.github.qoiu.main.presenter.MessageSender;
import com.github.qoiu.main.presenter.mappers.SendMapperEndGame;
import com.github.qoiu.main.presenter.mappers.SendMapperLeaveGame;
import com.github.qoiu.main.presenter.mappers.SendMapperSimpleText;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface GamePresenter {
    void playerLeaveGame(UserMessaged userMessaged);
    void getChatMessage(UserMessaged userMessaged);
    class Base implements GamePresenter {
        private final List<GamePlayer> list;
        private final DatabaseInterface.Executor db;
        private Round current;
        private int currentRoundId = 0;
        private final GameScoreboard scoreboard;
        private final MessageSender sender;
        private final List<RoundType> rounds = new ArrayList<>(Arrays.asList(
                new RoundUsual(), new RoundDouble(), new RoundTriple(), new RoundReversed()));

        public Base(DatabaseInterface.Executor db, List<GamePlayer> list, MessageSender sender) {
            this.db = db;
            this.sender = sender;
            this.list = list;
            scoreboard = new GameScoreboard.Base(list);
            for (GamePlayer player : list) {
                new DbMapperUpdateUser(db).map(new GamePlayerToUserDbMapper(StateStatus.PLAYER_IN_GAME).map(player));
            }
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
                current = new Round.Base(rounds.get(currentRoundId), db, list,sender);
                System.out.println("startNewRound");
                current.subscribe(getSingle());
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
                sender.sendMessage(new SendMapperEndGame(text + scoreboard)
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

        public void getChatMessage(UserMessaged userMessaged) {
            String text = userMessaged.getMessage();
            GamePlayer player =  new UserMessagedToGamePlayer().map(userMessaged);
            if (text.equals("/leave")) {
                new SendMapperLeaveGame(db,player);
                playerLeaveGame(userMessaged);
            }
            if (current.isActivePlayer(player))
                if (userMessaged.getMessage().charAt(0) != '?') {
                    System.out.println("sendMessage"+ userMessaged.getMessage());
                    current.checkAnswer(userMessaged.getMessage());
                } else {
                    sendChatMsgToAll(userMessaged.getName(), userMessaged.getMessage().substring(1));
                }
        }

        private void sendChatMsgToAll(String from, String text) {
            for (GamePlayer player : list) {
                sender.sendChatMessage(new BotChatMessage(from, text, player.getId()));
            }
        }

        public void playerLeaveGame(UserMessaged userMessaged) {
            System.out.println("list: " + list.size());
            list.remove(new UserMessagedToGamePlayer().map(userMessaged));
            System.out.println("list after: " + list.size());
            if (list.size() == 0) current.finish();
        }
    }
}
