package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.CallbackActions;
import com.github.qoiu.main.bot.MessageSender;
import com.github.qoiu.main.bot.StateActions;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserInGame;
import com.github.qoiu.main.data.tables.GameDatabase;
import com.github.qoiu.main.data.tables.MessageHistoryDatabase;
import com.github.qoiu.main.data.tables.QuestionsDatabase;
import com.github.qoiu.main.data.tables.UserDatabase;
import javafx.util.Pair;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static com.github.qoiu.main.StateStatus.*;

public class MainPresenter implements MainPresenterInterface, MainPresenterCallbackInterface, MainPresenterMessageSenderInterface {
    public MainPresenter(BotInterface bot,
                         GameDatabase dbGame,
                         UserDatabase dbUser,
                         MessageHistoryDatabase dbMessages,
                         QuestionsDatabase dbQuestions) {
        this.bot = bot;
        this.dbGame = dbGame;
        this.dbUser = dbUser;
        this.dbMessages = dbMessages;
        this.dbQuestions = dbQuestions;
        bot.setPresenter(this);
        callbackActions = new CallbackActions(this,bot);
        sender = new MessageSender(this, bot);
    }

    private BotInterface bot;
    private GameDatabase dbGame;
    private UserDatabase dbUser;
    private MessageHistoryDatabase dbMessages;
    private QuestionsDatabase dbQuestions;
    private StateActions stateActions = new StateActions();
    private CallbackActions callbackActions;
    private MessageSender sender;

    @Override
    public List<Pair<Long, Integer>> getDisconnectedMessages() {
        return dbMessages.getDisconnectedMessages();
    }

    @Override
    public void saveMsg(Pair<Long, Integer> pair) {
        dbMessages.saveMsg(pair);
    }

    @Override
    public void deletedMsg(Pair<Long, Integer> pair) {
        dbMessages.deletedMsg(pair);
    }

    public void receiveMessage(User user, String message) {
        if (!dbUser.isUserExists(user.getId())) {
            dbUser.addUser(user.getId(),user.getFirstName() + " " + user.getLastName());
        }
        int status = stateActions.action(user.getId(), dbUser.getUserState(user.getId()), bot);
        dbUser.changeUserState(user.getId(), status);
        sender.sendMessageOnStatus(user.getId(), status);
    }

    @Override
    public void receiveCallback(User user, String message) {
        int status = callbackActions.action(user, message);
        dbUser.changeUserState(user.getId(),status);
        sender.sendMessageOnStatus(user.getId(), status);
    }

    @Override
    public void startGame(User user) {
        dbGame.createGameForPlayer(user.getId(), user.getFirstName() + " " + user.getLastName());
        dbGame.changeStatus(user.getId(), USER_IS_READY);
    }

    @Override
    public void connectPlayer(int gameId, long playerId){
        dbGame.connectPlayer(gameId, playerId);
        notifyPlayerStatusChanged(dbGame.getGame(gameId));
    }

    @Override
    public void startGameForAll(User user) {
        notifyPlayerStatusChanged(dbGame.getGame(dbGame.getGameId(user.getId())));
    }

    private void notifyPlayerStatusChanged(GameObject game){
        for (UserInGame player: game.getUserInGames()) {
            dbGame.changeStatus(player.getId(),USER_IN_GAME);
            dbUser.changeUserState(player.getId(),PLAYER_IN_GAME);
            player.setStatus(PLAYER_IN_GAME);
            sender.sendMessageOnStatus(player.getId(),player.getStatusId());
        }

    }


    public int getGameId(long id) {
        return dbGame.getGameId(id);
    }

    @Override
    public GameObject getGameByHostId(long id) {
        return dbGame.getGame(dbGame.getGameId(id));
    }


    public List<GameObject> getAllGames() {
        return dbGame.getAllGames();
    }


}
