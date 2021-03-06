package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.StateActions;
import com.github.qoiu.main.StateStatus;
import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.*;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.*;
import com.github.qoiu.main.presenter.game.GameEngine;
import com.github.qoiu.main.presenter.game.GamePresenter;
import com.github.qoiu.main.presenter.mappers.SendMapperGameCancelledClient;
import com.github.qoiu.main.presenter.mappers.SendMapperWaitingForPlayersClient;
import com.github.qoiu.main.presenter.mappers.SendMapperWaitingForPlayersHost;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Set;

import static com.github.qoiu.main.StateStatus.*;

public class MainPresenter implements MainPresenterInterface, MessageSender, PlayerNotifier {


    public MainPresenter(DatabaseInterface.Global db) {
        this.db = db;
        this.gamePresenter = new GamePresenter(db,this);
        new DbMapperRestartApp(db).map(null);
        tables = new PresenterTables(db,this);
        messages = new PreparedSendMessages();
    }

    private final MainPresenterHashTables.Global tables;
    private final PreparedSendMessages messages;
    private final GamePresenter gamePresenter;
    private BotInterface bot;
    private final DatabaseInterface.Global db;
    private final StateActions stateActions = new StateActions();

    @Override
    public List<BotMessage> getDisconnectedMessages() {
        return new MessagesDbToBotMessagesMapper().map(
                new DbMapperGetDisconnectedMessages(db).map(null));
    }

    @Override
    public Set<Long> getAllUsers() {
        return new DbMapperGetAllUsersId(db).map(null);
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
        switch (status) {
            case PLAYER_IN_GAME:
                tables.getGame(userMessaged.getId()).getChatMessage(userMessaged);
                break;
            case PLAYER_ADD_QUESTION:
                tables.getEditedQuestion(userMessaged.getId()).addAnswer(userMessaged.getMessage());
                sendMessage(tables.getSendMsg(PLAYER_ADD_QUESTION).map(userMessaged));
                break;
            case PLAYER_VOTE:
                new DbMapperAddUserQuestionAnswer(db,tables.getCurrentQuestion(userMessaged.getId())).map(userMessaged.getMessage());
                sendMessage(tables.getSendMsg(PLAYER_VOTE).map(userMessaged));
                break;
            case PLAYER_EDIT_QUESTION:
                tables.getEditedQuestion(userMessaged.getId()).setTitle(userMessaged.getMessage());
                sendMessage(tables.getSendMsg(PLAYER_ADD_QUESTION).map(userMessaged));
                new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(PLAYER_ADD_QUESTION).map(userMessaged));
                break;
            default:
                tables.getSendMsg(PLAYER_BASE_STATUS);
        }
    }

    @Override
    public void receiveCallback(UserMessaged userMessaged) {
        int status = stateActions.actionCallback(userMessaged.getMessage());
        new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        switch (status) {
            case PLAYER_IN_GAME:
                if (!tables.isGameExist(userMessaged.getId())) {
                    GameObject game =
                            new DbMapperGetGameByHostId(db).map(userMessaged.getId());
                    createGame(game);
                }
                break;
            case PLAYER_SAVE_QUESTION:
                new DbMapperAddUserQuestion(db).map(tables.getEditedQuestion(userMessaged.getId()));
                new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(PLAYER_WAITING_ACTION).map(userMessaged));
                sendMessage(tables.getSendMsg(status).map(userMessaged));
                break;
            case PLAYER_ADD_QUESTION:
                tables.setQuestionTemplate(userMessaged.getId(), new QuestionTemplate(userMessaged));
            default:
                if (tables.isGameExist(userMessaged.getId())) {
                    tables.getGame(userMessaged.getId()).playerLeaveGame(userMessaged);
                    tables.setGameEngine(userMessaged.getId(),null);
                }
                SendMessage sendMessage = null;
                if (tables.isMsgExist(status))
                    sendMessage = tables.getSendMsg(status).map(userMessaged);
                if (sendMessage != null) sendMessage(sendMessage);
                new DbMapperUpdateUser(db).map(new UserMessagedToUserDb(status).map(userMessaged));
        }
    }

    private void createGame(GameObject game){
        List<GamePlayer> playerList = new PlayersDbToGamePlayersMapper().map(game.getUserInGames());
        GameEngine gameEngine;
        gameEngine = new GameEngine.Base(gamePresenter, playerList, this);
        for (GamePlayer player : playerList) {
            tables.setGameEngine(player.getId(), gameEngine);
            new DbMapperUpdateUser(db).map(new GamePlayerToUserDbMapper(StateStatus.PLAYER_IN_GAME).map(player));
        }
    }

    public void notifyGamePlayersChanged(int gameId) {
        GameObject game = new DbMapperGetGameByGameId(db).map(gameId);
        if (!hostInGame(game)) {
            notifyGameCanceled(game);
        } else {
            for (UserMessaged user : new GamePlayersToUserMessagedsMapper().map(
                    new PlayersDbToGamePlayersMapper().map(
                            game.getUserInGames()
                    ))) {
                if (user.getId() != game.getHostId()) {
                    sendMessage(new SendMapperWaitingForPlayersClient(db).map(user));
                } else {
                    sendMessage(new SendMapperWaitingForPlayersHost(db).map(user));
                }
            }
        }
    }

    private boolean hostInGame(GameObject game) {
        for (PlayerDb player : game.getUserInGames()) {
            if (player.getId() == game.getHostId()) return true;
        }
        return false;
    }

    public void notifyGameCanceled(GameObject game) {
        for (UserMessaged user : new GamePlayersToUserMessagedsMapper().map(
                new PlayersDbToGamePlayersMapper().map(
                        game.getUserInGames()
                ))) {
            if (user.getId() != game.getHostId()) {
                sendMessage(new SendMapperGameCancelledClient(db).map(user));
            }
        }
        new DbMapperClearGame(db).map(game.getHostId());
    }

    private void checkForNewUsers(UserMessaged userMessaged) {
        UserDb userDb = new DbMapperGetUserById(db).map(userMessaged.getId());
        if (userDb == null)
            new DbMapperAddUser(db).map(new UserMessagedToUserDb(0).map(userMessaged));
    }

    private void sendMessage(SendMessage sendMessage){
        bot.sendMessage(sendMessage);
    }

    @Override
    public void sendMessage(GameMessage gameMessage) {
        sendMessage(new GameMessageToSendMessageMapper().map(gameMessage));
    }

    @Override
    public void sendChatMessage(GameMessage gameMessage) {
        bot.sendChatMessage(new GameMessageToSendChatMessageMapper().map(gameMessage));
    }

    @Override
    public void clearChat(long id) {
        bot.clearChat(id);
    }

    @Override
    public void updateQuestion(Question question, GameMessage message) {
        bot.sendMessage(messages.playerAnswer(
                message.getText(),
                message.getFrom(),
                question));
    }

    public void setBot(BotInterface bot) {
        this.bot = bot;
        bot.setPresenter(this);
    }

    public void lostConnection(){
        new DbMapperRestartApp(db).map(null);
        for (Long id:tables.getGameSet()) {
            if(tables.getGame(id)!=null) {
                tables.getGame(id).playerLeaveGame(new UserMessaged(id, "somePlayer"));
                tables.setGameEngine(id,null);
            }
        }
        tables.clearGames();
        tables.clearQuestionTemplate();
    }
}
