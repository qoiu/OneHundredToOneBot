package com.github.qoiu.main.bot;

import com.github.qoiu.main.MainPresenterCallbackInterface;
import com.github.qoiu.main.StateStatus;

public class CallbackActions {

    public CallbackActions(MainPresenterCallbackInterface presenter) {
        this.presenter = presenter;
    }
    private MainPresenterCallbackInterface presenter;
    private PreparedSendMessages messages = new PreparedSendMessages();

    public Integer action(long longId, int status, String text, BotInterface bot){
        String id = String.valueOf(longId);
        switch (text){
            case "/start":
                int gameId = presenter.getGameId(longId);
                if(status == StateStatus.WAITING_ACTION){
                    bot.sendMessage(messages.waitingPlayers(id,String.valueOf(gameId)));
                    presenter.startGame(longId);
                    return StateStatus.WAITING_OTHER_PLAYERS;
                } else {
                    bot.sendMessage(messages.connectionError(id));
                }
            case "/allReady":

        }
        return status;
    }
}
