package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.tables.DatabaseBase;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.validation.constraints.Null;
import java.util.List;

public class DbMapperTest {

    private DatabaseBase db;

    @BeforeTest
    void create() {
        db = new DatabaseBase("jdbc:sqlite:test.db");
    }


    @Test
    void test_DbMapperGameId() {

        UserDb user = new UserDb(112L, 0, "zozo");
        int actualGameId = new <Integer, UserMessaged>DbMapperCreateGameForPlayer(db)
                .map(user);

        int gameId = new DbMapperGameId(db).map(112L);

        Assert.assertEquals(actualGameId, gameId);
        int gameIncorrectId = new DbMapperGameId(db).map(145L);
        Assert.assertEquals(gameIncorrectId, 0);
    }

    @Test
    void test_DbMapperGame() {

        UserDb user = new UserDb(112L, 0, "zozo");
        int actualGameId = new <Integer, UserMessaged>DbMapperCreateGameForPlayer(db)
                .map(user);

        GameObject game = new <GameObject, Long>DbMapperGameById(db).map(112);

        Assert.assertNotNull(game);
        GameObject expected = new GameObject("zozo", 112L, actualGameId);
        Assert.assertEquals(game, expected);
        int gameIncorrectId = new DbMapperGameId(db).map(145L);
        Assert.assertEquals(gameIncorrectId, 0);
    }

    @Test
    void test_DbMapperAllGames() {


        UserDb user1 = new UserDb(112L, 0, "lolko");
        UserDb user2 = new UserDb(113L, 0, "zolko");
        UserDb user3 = new UserDb(114L, 0, "igolko");

        new DbMapperAddUser(db).map(user1);
        new DbMapperAddUser(db).map(user2);
        new DbMapperAddUser(db).map(user3);

        Integer game1 = new <Integer, UserMessaged>DbMapperCreateGameForPlayer(db)
                .map(user1);

        Integer game2 = new <Integer, UserMessaged>DbMapperCreateGameForPlayer(db)
                .map(user2);

        Integer game3 = new <Integer, UserMessaged>DbMapperCreateGameForPlayer(db)
                .map(user3);

        new DbMapperConnectPlayer(db,game1).map(user2);

        List<GameObject> games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);

        Assert.assertEquals(games.get(0).getName(), user1.getName());
        Assert.assertEquals(games.get(0).getUserInGames().get(0).getId(),user1.getId());
        Assert.assertEquals(games.get(0).getUserInGames().get(1).getId(),user2.getId());
        Assert.assertEquals(games.get(1).getUserInGames().get(0).getId(),user2.getId());
        Assert.assertEquals(games.get(2).getUserInGames().get(0).getId(),user3.getId());
        Assert.assertEquals(games.size(), 3);
    }

    @Test
    void test_execute_update() {
        String name;
        name = new DbMapperGetUserById(db).map(1112L).getName();
        //this mapper will get SQLException cause no user in db
        Assert.assertEquals(name, "");

        UserDb user = new UserDb(1112L, 0, "lolko");
        int gameId = new DbMapperAddUser(db).map(user);
        name = new DbMapperGetUserById(db).map(1112L).getName();

        Assert.assertEquals(name, user.getName());
    }

    @Test
    void test_change_userInGame_status(){
        UserDb user = new UserDb(1112L, 0, "lolko");

        new DbMapperCreateGameForPlayer(db).map(user);

        user.setStatus(3);
        new DbMapperUpdateUserInGameStatus(db).map(user);

        List<GameObject> games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);


        Assert.assertEquals(games.get(0).getUserInGames().get(0).getStatusGame(),3L);
    }


    @AfterTest
    @BeforeMethod
    void clearTables() {
        System.out.println("clearDb");
        db.clearDatabase();
    }

    @AfterTest
    void disconnect(){
        System.out.println("disconnect");
        db.disconnect();
    }
}