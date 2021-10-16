package com.github.qoiu.main;

import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.CallbackActions;
import com.github.qoiu.main.bot.StateActions;
import com.github.qoiu.main.data.WorkWithDb;
import javafx.util.Pair;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class MainPresenter implements MainPresenterInterface,MainPresenterCallbackInterface {
    MainPresenter(BotInterface bot, WorkWithDb db) {
        this.bot = bot;
        this.db = db;
        bot.setPresenter(this);
    }

    private BotInterface bot;
    private WorkWithDb db;
    private StateActions stateActions = new StateActions();
    private CallbackActions callbackActions = new CallbackActions(this);

    @Override
    public List<Pair<Long, Integer>> getDisconnectedMessages() {
        return db.getDisconnectedMessages();
    }

    @Override
    public void saveMsg(Pair<Long, Integer> pair) {
        db.saveMsg(pair);
    }

    @Override
    public void deletedMsg(Pair<Long, Integer> pair) {
        db.deletedMsg(pair);
    }

    public void receiveMessage(User user, String message){
        if(!db.isUserExists(user.getId())){
            db.addUser(user.getId(), user.getUserName());
        }
        db.changeUserState(user.getId(), stateActions.action(user.getId(), db.getUserState(user.getId()), bot));
    }

    @Override
    public void receiveCallback(User user, String message) {
        Integer status = db.getUserState(user.getId());
        db.changeUserState(user.getId(),callbackActions.action(user.getId(),status,message,bot));
    }

    @Override
    public void startGame(long id) {
    }

    @Override
    public int getGameId(long id) {
        return db.getGameId(id);
    }
}
