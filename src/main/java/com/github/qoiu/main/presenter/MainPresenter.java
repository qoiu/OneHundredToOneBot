package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.bot.StateActions;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.BotMessageToMessageDbMapper;
import com.github.qoiu.main.mappers.MessagesDbToBotMessagesMapper;
import com.github.qoiu.main.mappers.PlayersDbToGamePlayersMapper;
import com.github.qoiu.main.mappers.UserMessagedToUserDb;
import com.github.qoiu.main.presenter.mappers.SendMapper;
import com.github.qoiu.main.presenter.mappers.SendMapperListOfGames;
import com.github.qoiu.main.presenter.mappers.SendMapperMainMenu;
import com.github.qoiu.main.presenter.mappers.SendMapperWaitingForPlayers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;

import static com.github.qoiu.main.StateStatus.*;

public class MainPresenter implements MainPresenterInterface, MessageSender {
    public MainPresenter(BotInterface bot,
                         DatabaseBase db) {
        this.bot = bot;
        this.db = db;
        bot.setPresenter(this);
        initMessageMap();
    }

    private void initMessageMap(){
        messageMap.put(PLAYER_BASE_STATUS, new SendMapperMainMenu(db));
        messageMap.put(PLAYER_WAITING_ACTION, new SendMapperMainMenu(db));
        messageMap.put(PLAYER_CHOSE_GAME, new SendMapperListOfGames(db));
        messageMap.put(PLAYER_WAITING_OTHER_PLAYERS_HOST, new SendMapperWaitingForPlayers(db));
    }

    private final HashMap<Integer, SendMapper> messageMap = new HashMap<>();
    private final HashMap<Long, GamePresenter> games = new HashMap<>();
    private final BotInterface bot;
    private final DatabaseBase db;
    private final StateActions stateActions = new StateActions();

    @Override
    public List<BotMessage> getDisconnectedMessages() {
        return new MessagesDbToBotMessagesMapper().map(
                new DbMapperGetDisconnectedMessages(db).map(null));
    }

    @Override
    public void saveMsg(BotMessage msg) {
        new DbMapperAddMessage(db).map(new BotMessageToMessageDbMapper().map(msg));
    }

    @Override
    public void deletedMsg(BotMessage msg) {
        new DbMapperDeleteMessage(db).map(new BotMessageToMessageDbMapper().map(msg));
    }

    public void receiveMessage(UserMessaged userMessaged) {
        checkForNewUsers(userMessaged);
        int status = new DbMapperGetUserById(db).map(userMessaged.getId()).getStatusId();
        if (status != PLAYER_IN_GAME) {
            if (messageMap.containsKey(status))
                sendMessage(messageMap.get(status).map(userMessaged));
        } else {
            // TODO: 30.10.2021 chat
        }
    }

    private void checkForNewUsers(UserMessaged userMessaged) {
        UserDb userDb = new DbMapperGetUserById(db).map(userMessaged.getId());
        if (userDb.getName().equals(""))
            new DbMapperAddUser(db).map(new UserMessagedToUserDb(0).map(userMessaged));
    }

    @Override
    public void receiveCallback(UserMessaged userMessaged) {
        int status = stateActions.actionCallback(userMessaged.getMessage());
        SendMessage sendMessage = null;
        if (status != PLAYER_IN_GAME) {
            if (messageMap.containsKey(status))
                sendMessage = messageMap.get(status).map(userMessaged);
            sendMessage(sendMessage);
            new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        } else {
            if (!games.containsKey(userMessaged.getId())) {
                GameObject game =
                        new DbMapperGetGameByHostId(db).map(userMessaged.getId());
                games.put(userMessaged.getId(), new GamePresenter(db,
                        new PlayersDbToGamePlayersMapper().map(game.getUserInGames()),
                        this));
            } else {
                // TODO: 30.10.2021 actions for  other players
            }
        }
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        bot.sendMessage(sendMessage);
    }
}
