package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.*;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import org.junit.*;

import javax.validation.constraints.Null;
import java.util.*;

public class DbMapperTest {

    private static DatabaseBase db;

    @BeforeClass
    public static void create() {
        db = new DatabaseBase("jdbc:sqlite:test.db");
    }


    @Test
    public void test_DbMapperGameId() {

        UserDb user = new UserDb(112L, 0, "Alex");
        int actualGameId = new DbMapperCreateGameForPlayer(db)
                .map(user);

        int gameId = new DbMapperGameHostId(db).map(112L);

        Assert.assertEquals(actualGameId, gameId);
        Integer gameIncorrectId = new DbMapperGameHostId(db).map(145L);
        Assert.assertNull(gameIncorrectId);
    }

    @Test
    public void test_DbMapperGame() {

        GameObject game = new DbMapperGetGameByHostId(db).map(112L);
        //check if no such game
        Assert.assertNull(game);

        UserDb user = new UserDb(112L, 0, "Alex");
        int actualGameId = new DbMapperCreateGameForPlayer(db)
                .map(user);

        //we should create new user, because it doesn't create with DbMapperCreateGameForPlayer
        new DbMapperAddUser(db).map(user);
        game = new DbMapperGetGameByHostId(db).map(112L);

        Assert.assertNotNull(game);
        GameObject expected = new GameObject("Alex", 112L, actualGameId);
        Assert.assertEquals(game, expected);
        Assert.assertEquals(1,game.getUserInGames().size());
        Integer gameIncorrectId = new DbMapperGameHostId(db).map(145L);
        Assert.assertNull(gameIncorrectId);
    }

    @Test
    public void test_DbMapperAllGames_exceptions(){
        List<GameObject> games = new DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.size(),0);
        UserDb user1 = new UserDb(112L, 0, "Alex");

        new DbMapperCreateGameForPlayer(db).map(user1);
        games = new DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.get(0).getUserInGames().size(),0);
        new DbMapperDeletePlayer(db).map(user1.getId());
        games = new DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.get(0).getUserInGames().size(),0);
    }

    @Test
    public void test_DbMapperAllGames() {
        UserDb user1 = new UserDb(112L, 0, "Alex");
        UserDb user2 = new UserDb(113L, 0, "Yuri");
        UserDb user3 = new UserDb(114L, 0, "Sergei");

        new DbMapperAddUser(db).map(user1);
        new DbMapperAddUser(db).map(user2);
        new DbMapperAddUser(db).map(user3);

        Integer game1 = new DbMapperCreateGameForPlayer(db)
                .map(user1);
        new DbMapperCreateGameForPlayer(db).map(user2);
        new DbMapperCreateGameForPlayer(db).map(user3);

        new DbMapperAddPlayer(db,game1).map(user2);

        List<GameObject> games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);

        Assert.assertEquals(games.get(0).getName(), user1.getName());
        Assert.assertEquals(games.get(0).getUserInGames().get(0).getId(),user1.getId());
        Assert.assertEquals(games.get(0).getUserInGames().get(1).getId(),user2.getId());
        Assert.assertEquals(games.get(1).getUserInGames().get(0).getId(),user2.getId());
        Assert.assertEquals(games.get(2).getUserInGames().get(0).getId(),user3.getId());
        Assert.assertEquals(games.size(), 3);

        new DbMapperClearGame(db).map(user2.getId());
        games = new <List<GameObject>, Null>DbMapperAllGames(db).map(null);
        Assert.assertEquals(games.size(), 2);

        Integer id = new DbMapperGameIdByPlayerId(db).map(user1.getId());
        GameObject gameObject = new DbMapperGetGameByGameId(db).map(id);
        Assert.assertEquals(new Integer(gameObject.getId()),id);


        id = new DbMapperGameIdByPlayerId(db).map(3333333L);
        Assert.assertNull(id);

        gameObject = new DbMapperGetGameByGameId(db).map(3333);
        Assert.assertNull(gameObject);




    }

    @Test
    public void test_get_user_by_id() {
        UserDb userDb = new DbMapperGetUserById(db).map(1112L);
        Assert.assertNull(userDb);

        UserDb user = new UserDb(1112L, 0, "Alex");
        new DbMapperAddUser(db).map(user);
        UserDb actual = new DbMapperGetUserById(db).map(1112L);

        Assert.assertEquals(user,actual);
    }

    @Test
    public void test_change_userInGame_status(){
        UserDb user = new UserDb(1112L, 0, "Alex");

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
        UserDb user = new UserDb(1112L, 0, "Alex");
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
    public void test_add_question(){
        new DbMapperAddQuestion(db).map(getTestQuestion(1));
        QuestionDb questionDb = new DbMapperGetQuestion(db).map(1);
        Assert.assertEquals(getTestQuestion(1),questionDb);
    }

    @Test
    public void update_answer_rate(){
        new DbMapperAddQuestion(db).map(getTestQuestion(1));
        QuestionDb questionDb = new DbMapperGetQuestion(db).map(1);
        int expected = 15;
        questionDb.getAnswers().get(0).setRate(expected);
        new DbMapperUpdateQuestionAnswerRate(db,1).map(
                new QuestionDbToQuestionMapper().map(questionDb).getAnswers().get(0)
        );
        QuestionDb actual = new DbMapperGetQuestion(db).map(1);
        Assert.assertEquals(expected,actual.getAnswers().get(0).getRate());
    }

    @Test
    public void check_with_user_answer(){
        UserDb user = new UserDb(112L,0,"Alex");
        new DbMapperAddUser(db).map(user);
        user.setExtra(1);
        new DbMapperAddUserAnswer(db).map(user);
        Set<Integer> actual = new DbMapperGetUnansweredQuestionsByPlayers(db).map(
                new HashSet<>(Collections.singletonList(user.getId())));
        Assert.assertFalse(actual.contains(1));
    }


    private QuestionDb getTestQuestion(int id){
        QuestionDb question = new QuestionDb(id,"New question");
        question.new Answer("text",10);
        question.new Answer("text2",20);
        return question;
    }

    @Test
    public void empty_user(){
        UserDb user = new DbMapperGetUserById(db).map(112L);
        Assert.assertNull(user);
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