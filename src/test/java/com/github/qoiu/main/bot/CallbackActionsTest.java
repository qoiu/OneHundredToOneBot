package com.github.qoiu.main.bot;

import com.github.qoiu.main.presenter.MainPresenterCallbackInterface;
import com.github.qoiu.main.presenter.MainPresenterInterface;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.github.qoiu.main.StateStatus.*;
import static org.testng.Assert.assertEquals;

public class CallbackActionsTest {

    private CallbackActions callbackActions;

    @BeforeTest
    void create(){
        BotInterface bot = new BotInterface() {
            @Override
            public void setPresenter(MainPresenterInterface presenter) {}

            @Override
            public void sendMessage(SendMessage message) {
                System.out.println(message);
            }

            @Override
            public void startGame(long id) { }
        };
        MainPresenterCallbackInterface presenter = new MainPresenterCallbackInterface(){
            @Override
            public void startGame(User user) {
                System.out.println("gameStarted");
            }

            @Override
            public void startGameForAll(User user) {

            }

            @Override
            public void connectPlayer(int gameId, long playerId) {

            }
        };
        callbackActions = new CallbackActions(presenter,bot);
    }

    @Test
    void test_start(){
        User user = new User();
        user.setId(0L);
        int expected = PLAYER_WAITING_OTHER_PLAYERS_HOST;
        int actual = callbackActions.action(user,"/start");
        assertEquals(actual,expected);
        expected = PLAYER_CHOSE_GAME;
        actual = callbackActions.action(user,"/connecting");
        assertEquals(actual,expected);
        expected = PLAYER_WAITING_OTHER_PLAYERS;
        actual = callbackActions.action(user,"/connect: 0");
        assertEquals(actual,expected);
    }



}