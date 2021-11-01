package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.bot.StateActions;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.*;
import com.github.qoiu.main.presenter.mappers.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;

import static com.github.qoiu.main.StateStatus.*;

public class MainPresenter implements MainPresenterInterface, MessageSender, PlayerNotifier {
    public MainPresenter(BotInterface bot,
                         DatabaseBase db) {
        this.bot = bot;
        this.db = db;
        bot.setPresenter(this);
        initMessageMap();
    }

    private void initMessageMap() {
        messageMap.put(PLAYER_BASE_STATUS, new SendMapperMainMenu(db,this));
        messageMap.put(PLAYER_WAITING_ACTION, new SendMapperMainMenu(db,this));
        messageMap.put(PLAYER_CHOSE_GAME, new SendMapperListOfGames(db));
        messageMap.put(PLAYER_WAITING_OTHER_PLAYERS, new SendMapperAddPlayerToGame(db,this));
        messageMap.put(PLAYER_WAITING_OTHER_PLAYERS_HOST, new SendMapperCreateGame(db));
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
        if (userMessaged.getMessage().startsWith("/")) {
            status = stateActions.actionCallback(userMessaged.getMessage());
            new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        } else if (status != PLAYER_IN_GAME) {
            if (messageMap.containsKey(status))
                sendMessage(messageMap.get(status).map(userMessaged));
        } else {
            games.get(userMessaged.getId()).getChatMessage(userMessaged);
        }
    }

    @Override
    public void receiveCallback(UserMessaged userMessaged) {
        int status = stateActions.actionCallback(userMessaged.getMessage());
        new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        if (status != PLAYER_IN_GAME) {
            if (games.containsKey(userMessaged.getId())&& games.get(userMessaged.getId())!=null) {
                games.get(userMessaged.getId()).playerLeaveGame(userMessaged);
                games.put(userMessaged.getId(),null);
            }
            SendMessage sendMessage = null;
            if (messageMap.containsKey(status))
                sendMessage = messageMap.get(status).map(userMessaged);
            if(sendMessage!=null)sendMessage(sendMessage);
            new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        } else {
            if (!games.containsKey(userMessaged.getId())|| games.get(userMessaged.getId())==null) {
                GameObject game =
                        new DbMapperGetGameByHostId(db).map(userMessaged.getId());
                List<GamePlayer> playerList= new PlayersDbToGamePlayersMapper().map(game.getUserInGames());
                GamePresenter gamePresenter;
                gamePresenter =  new GamePresenter(db, playerList,this);
                for (GamePlayer player:playerList) {
                    games.put(userMessaged.getId(),gamePresenter);
                }
            } else {
                // TODO: 30.10.2021 actions for  other players
            }
        }
    }

    public void notifyGamePlayersChanged(int gameId) {
        GameObject game = new DbMapperGetGameByGameId(db).map(gameId);

        for (UserMessaged user : new GamePlayersToUserMessagedsMapper().map(
                new PlayersDbToGamePlayersMapper().map(
                        game.getUserInGames()
                ))) {
            if (user.getId() != game.getHostId()){
                sendMessage(new SendMapperWaitingForPlayersClient(db).map(user));
            }else {
                sendMessage(new SendMapperWaitingForPlayersHost(db).map(user));
            }
        }
    }

    public void notifyGameCanceled(GameObject game){
        for (UserMessaged user : new GamePlayersToUserMessagedsMapper().map(
                new PlayersDbToGamePlayersMapper().map(
                        game.getUserInGames()
                ))) {
            if (user.getId() != game.getHostId()){
                sendMessage(new SendMapperGameCancelledClient(db).map(user));
            }
        }
    }

    private void checkForNewUsers(UserMessaged userMessaged) {
        UserDb userDb = new DbMapperGetUserById(db).map(userMessaged.getId());
        if (userDb.getName().equals(""))
            new DbMapperAddUser(db).map(new UserMessagedToUserDb(0).map(userMessaged));
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        bot.sendMessage(sendMessage);
    }

    public void sendChatMessage(BotChatMessage chatMessage) {
        bot.sendChatMessage(chatMessage);
    }

    @Override
    public void clearChat(long id) {
        bot.clearChat(id);
    }
}
