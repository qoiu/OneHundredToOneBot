package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.*;
import com.github.qoiu.main.data.DatabaseBase;
import org.junit.*;

import javax.validation.constraints.Null;
import java.util.List;

public class DbMapperTest {

    private static DatabaseBase db;

    @BeforeClass
    public static void create() {
        db = new DatabaseBase("jdbc:sqlite:test.db");
    }


    @Test
    public void test_DbMapperGameId() {

        UserDb user = new UserDb(112L, 0, "zozo");
        int actualGameId = new DbMapperCreateGameForPlayer(db)
                .map(user);

        int gameId = new DbMapperGameId(db).map(112L);

        Assert.assertEquals(actualGameId, gameId);
        int gameIncorrectId = new DbMapperGameId(db).map(145L);
        Assert.assertEquals(gameIncorrectId, 0);
    }

    @Test
    public void test_DbMapperGame() {

        GameObject game = new DbMapperGetGameById(db).map(112);
        Assert.assertEquals(game.getName(),"Error");

        UserDb user = new UserDb(112L, 0, "zozo");
        int actualGameId = new DbMapperCreateGameForPlayer(db)
                .map(user);

        game = new <GameObject, Long>DbMapperGetGameById(db).map(112);

        Assert.assertNotNull(game);
        GameObject expected = new GameObject("zozo", 112L, actualGameId);
        Assert.assertEquals(game, expected);
        Assert.assertEquals(game.getUserInGames().size(),1);
        int gameIncorrectId = new DbMapperGameId(db).map(145L);
        Assert.assertEquals(gameIncorrectId, 0);
    }

    @Test
    public void test_DbMapperAllGames_exceptions(){
        List<GameObject> games = new DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.size(),0);
        UserDb user1 = new UserDb(112L, 0, "lolko");

        Integer game1 = new DbMapperCreateGameForPlayer(db)
                .map(user1);
        new DbMapperDeletePlayer(db).map(user1.getId());
        games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.get(0).getUserInGames().size(),0);
    }

    @Test
    public void test_DbMapperAllGames() {



        UserDb user1 = new UserDb(112L, 0, "lolko");
        UserDb user2 = new UserDb(113L, 0, "zolko");
        UserDb user3 = new UserDb(114L, 0, "igolko");

        new DbMapperAddUser(db).map(user1);
        new DbMapperAddUser(db).map(user2);
        new DbMapperAddUser(db).map(user3);

        Integer game1 = new DbMapperCreateGameForPlayer(db)
                .map(user1);

        Integer game2 = new DbMapperCreateGameForPlayer(db)
                .map(user2);

        Integer game3 = new DbMapperCreateGameForPlayer(db)
                .map(user3);

        new DbMapperAddPlayer(db,game1).map(user2);

        List<GameObject> games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);

        Assert.assertEquals(games.get(0).getName(), user1.getName());
        Assert.assertEquals(games.get(0).getUserInGames().get(0).getId(),user1.getId());
        Assert.assertEquals(games.get(0).getUserInGames().get(1).getId(),user2.getId());
        Assert.assertEquals(games.get(1).getUserInGames().get(0).getId(),user2.getId());
        Assert.assertEquals(games.get(2).getUserInGames().get(0).getId(),user3.getId());
        Assert.assertEquals(games.size(), 3);
    }

    @Test
    public void test_execute_update() {
        String name;
        name = new DbMapperGetUserById(db).map(1112L).getName();
        //this mapper will get SQLException cause no user in db
        Assert.assertEquals(name, "");

        UserDb user = new UserDb(1112L, 0, "lolko");
        int gameId = new DbMapperAddUser(db).map(user);
        UserDb actual = new DbMapperGetUserById(db).map(1112L);

        Assert.assertEquals(actual,user);
    }

    @Test
    public void test_change_userInGame_status(){
        UserDb user = new UserDb(1112L, 0, "lolko");

        PlayerDb playerDb = new PlayerDb(user.getId(),0,0);
        new DbMapperCreateGameForPlayer(db).map(user);
        new DbMapperAddUser(db).map(user);

        playerDb.setPlayerStatus(3);
        new DbMapperUpdateUserInGameStatus(db).map(playerDb);

        List<GameObject> games = new DbMapperAllGames(db).map(null);


        Assert.assertEquals(games.get(0).getUserInGames().get(0).getPlayerStatus(),3);
    }

    @Test
    public void test_user_update(){
        UserDb user = new UserDb(1112L, 0, "lolko");
        new DbMapperAddUser(db).map(user);
        user.setStatus(3);
        new DbMapperUpdateUser(db).map(user);
        UserDb actual = new DbMapperGetUserById(db).map(user.getId());
        Assert.assertEquals(actual.getStatusId(),3);
    }

    @Test
    public void test_DbMessage(){
        List<MessageDb> actual = new DbMapperGetDisconnectedMessages(db).map(null);
        Assert.assertEquals(actual.size(),0);
        MessageDb messageDb = new MessageDb(10,11);
        new DbMapperAddMessage(db).map(messageDb);
        actual = new DbMapperGetDisconnectedMessages(db).map(null);
        Assert.assertEquals(actual.size(),1);
        new DbMapperDeleteMessage(db).map(messageDb);
        actual = new DbMapperGetDisconnectedMessages(db).map(null);
        Assert.assertEquals(actual.size(),0);
    }

    @Test
    public void test_questions(){
        QuestionDb question = new QuestionDb(1,"New question");
        question.new Answer("text",10);
        new DbMapperAddQuestion(db).map(question);
        question = new QuestionDb(1,"Updated");
        new DbMapperAddQuestion(db).map(question);
        Assert.assertEquals(question.getId(),1);
    }

    @Before
    public void clearTables() {
        System.out.println("clearDb");
        db.clearDatabase();
    }

    @AfterClass
    public static void disconnect(){
        System.out.println("disconnect");
        db.clearDatabase();
        db.disconnect();
    }
}