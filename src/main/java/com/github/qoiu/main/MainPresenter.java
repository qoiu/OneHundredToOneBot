package com.github.qoiu.main;

import com.github.qoiu.main.bot.BotInterface;
import com.github.qoiu.main.bot.CallbackActions;
import com.github.qoiu.main.bot.StateActions;
import com.github.qoiu.main.data.WorkWithDb;
import org.telegram.telegrambots.meta.api.objects.User;

public class MainPresenter implements MainPresenterInterface,MainPresenterCallbackInterface, WorkWithDb {
    MainPresenter(BotInterface bot, WorkWithDb db) {
        this.bot = bot;
        this.db = db;
        bot.setPresenter(this);
    }

    private BotInterface bot;
    private WorkWithDb db;
    private StateActions stateActions = new StateActions();
    private CallbackActions callbackActions = new CallbackActions(this);

    public void receiveMessage(User user, String message){
        if(!isUserExists(user.getId())){
            addUser(user.getId(), user.getUserName());
        }
        db.changeUserState(user.getId(), stateActions.action(user.getId(),getUserState(user.getId()), bot));
    }

    @Override
    public void receiveCallback(User user, String message) {
        Integer status = db.getUserState(user.getId());
        db.changeUserState(user.getId(),callbackActions.action(user.getId(),status,message,bot));
    }

    public void addUser(long id, String name) {
        db.addUser(id,name);
    }

    @Override
    public Boolean isUserExists(long id) {
        return db.isUserExists(id);
    }

    public String getUser(long id) {
        return db.getUser(id);
    }

    public void changeUserState(long id, int state) {
        db.changeUserState(id, state);
    }

    public Integer getUserState(long id) {
        return db.getUserState(id);
    }

    @Override
    public void createGameForPlayer(long id) {
        db.createGameForPlayer(id);
    }

    @Override
    public void startGame(long id) {
    }

    @Override
    public int getGameId(long id) {
        return db.getGameId(id);
    }
}
