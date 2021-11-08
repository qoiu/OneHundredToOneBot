package com.github.qoiu.main.bot;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGameHostId;
import com.github.qoiu.main.data.mappers.DbMapperGetGameByHostId;
import com.github.qoiu.main.data.mappers.DbMapperGetUserById;
import com.github.qoiu.main.presenter.MainPresenter;
import com.github.qoiu.main.presenter.MainPresenterInterface;
import com.github.qoiu.main.presenter.mappers.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class GlobalLogicTest {

    private static MainPresenter presenter;
    private static DatabaseBase db;
    private static String botReceiveActual;
    private static final List<BotChatMessage> botChatMessageList=new ArrayList<>();
    private static final List<String> sendMessageList=new ArrayList<>();
    private static BotInterface bot = new BotInterface() {
        @Override
        public void setPresenter(MainPresenterInterface presenter) {
            //don't use in this test
        }

        @Override
        public void sendMessage(SendMessage message) {
            botReceiveActual =message.getText();
            sendMessageList.add(message.getText());
        }

        @Override
        public void sendChatMessage(BotChatMessage chatMessage) {

            botReceiveActual =chatMessage.getText();
            botChatMessageList.add(chatMessage);
        }

        @Override
        public void clearChat(long id) {
            botReceiveActual =String.valueOf(id);
        }
    };


    @BeforeClass
    public static void init(){
        db = new DatabaseBase("jdbc:sqlite:test.db");
        db.clearDatabase();
        presenter = new MainPresenter(bot,db);
    }

    @Test
    public void first_message_from_user(){
        //send message from 2 new users
        UserMessaged user1 = new UserMessaged("Alex",225L,"Hello!");
        UserMessaged user2 = new UserMessaged("Sergei",226L,"Hello!");
        UserMessaged user3 = new UserMessaged("Vasiliy",227L,"Hello!");
        presenter.receiveMessage(user1);
        presenter.receiveMessage(user2);
        presenter.receiveMessage(user3);

        //check user1 was created
        UserDb actual = new DbMapperGetUserById(db).map(user1.getId());
        Assert.assertNotNull(actual);

        //check botReceive
//        String expected = "Привет, рад тебя видеть!\nТы уже готов начать новую игру?";
//        Assert.assertEquals(expected, botReceiveActual);

        //send callback to start game
        user1 = new UserMessaged("Alex",225L,"/newGame");
        presenter.receiveCallback(user1);
        Assert.assertEquals(new SendMapperCreateGame(db).map(user1).getText(), botReceiveActual);


        //send callback connect to Alex
        Integer gameId = new DbMapperGameHostId(db).map(user1.getId());
        Assert.assertNotNull(gameId);
        user2 = new UserMessaged("Sergei",226L,"/connect:"+gameId);
        presenter.receiveCallback(user2);

        String actualString = new SendMapperWaitingForPlayersClient(db).map(user2).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        actualString = new SendMapperWaitingForPlayersHost(db).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));

        //user2 decide to leave
        user2 = new UserMessaged("Sergei",226L,"/menu");
        presenter.receiveCallback(user2);
        GameObject game = new DbMapperGetGameByHostId(db).map(user1.getId());
        Assert.assertNotNull(game);
        Assert.assertEquals(game.getUserInGames().size(),1);

        sendMessageList.clear();
        //connect another player
        user3 = new UserMessaged("Vasiliy",227L,"/connect:"+gameId);
        presenter.receiveCallback(user3);
        actualString = new SendMapperWaitingForPlayersClient(db).map(user3).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        actualString = new SendMapperWaitingForPlayersHost(db).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));

        sendMessageList.clear();
        //disconnect host
        user1 = new UserMessaged("Alex",225L,"/menu");
        presenter.receiveCallback(user1);
        actualString = new SendMapperGameCancelledClient(db).map(user3).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        actualString = new SendMapperMainMenu(db,presenter).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));

        sendMessageList.clear();
        //Vasiliy check for createdGames
        user3 = new UserMessaged("Vasiliy",227L,"/connecting");
        presenter.receiveCallback(user3);
        actualString = new SendMapperListOfGames(db).map(user3).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));



        //send callback to start game
        user1 = new UserMessaged("Alex",225L,"/newGame");
        presenter.receiveCallback(user1);
        Assert.assertEquals(new SendMapperCreateGame(db).map(user1).getText(), botReceiveActual);


        //send callback connect to Alex
        gameId = new DbMapperGameHostId(db).map(user1.getId());
        Assert.assertNotNull(gameId);
        user2 = new UserMessaged("Sergei",226L,"/connect:"+gameId);
        presenter.receiveCallback(user2);
        actualString = new SendMapperWaitingForPlayersClient(db).map(user2).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        actualString = new SendMapperWaitingForPlayersHost(db).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));

        //connect another player
        user3 = new UserMessaged("Vasiliy",227L,"/connect:"+gameId);
        presenter.receiveCallback(user3);
        actualString = new SendMapperWaitingForPlayersClient(db).map(user3).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        actualString = new SendMapperWaitingForPlayersHost(db).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));
        int expected = 3;
        game = new DbMapperGetGameByHostId(db).map(user1.getId());
        Assert.assertEquals(game.getUserInGames().size(),expected);

        //AlexStartGame
        user1 = new UserMessaged("Alex",225L,"/startGame");
        presenter.receiveCallback(user1);
        actualString = new SendMapperWaitingForPlayersHost(db).map(user1).getText();
        Assert.assertTrue(sendMessageList.contains(actualString));

    }

    @AfterClass
    public static void clear(){
        db.clearDatabase();
    }

}