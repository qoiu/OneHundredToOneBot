package com.github.qoiu.main.bot;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.presenter.MainPresenterMessageSenderInterface;

import java.util.List;

import static com.github.qoiu.main.StateStatus.*;

public class MessageSender{
    public MessageSender(MainPresenterMessageSenderInterface presenter, BotInterface bot) {
        this.presenter = presenter;
        this.bot = bot;
    }

    private MainPresenterMessageSenderInterface presenter;
    private BotInterface bot;

    public void sendMessageOnStatus(long id, int status){
        PreparedSendMessages messages = new PreparedSendMessages();
        String idString = String.valueOf(id);
        switch (status) {
            case PLAYER_BASE_STATUS:
                bot.sendMessage(messages.mainMenu(idString));
                return;
            case PLAYER_WAITING_ACTION:
                bot.sendMessage(messages.mainMenu(idString));
                return;
            case PLAYER_CHOSE_GAME:
                List<GameObject> games = presenter.getAllGames();
                bot.sendMessage(messages.listOfGames(idString, games));
                return;
            case PLAYER_WAITING_OTHER_PLAYERS_HOST:
                GameObject game = presenter.getGameByHostId(id);
                bot.sendMessage(messages.hostWaitingPlayers(id, game));
                return;
            case PLAYER_WAITING_OTHER_PLAYERS:
                bot.sendMessage(messages.waitingHostReady(idString));
                return;
            case PLAYER_IN_GAME:
                bot.sendMessage(messages.playerInGame(idString));
        }
    }
}
