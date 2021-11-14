package com.github.qoiu.main.presenter;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.presenter.game.GameEngine;
import com.github.qoiu.main.presenter.mappers.*;

import java.util.HashMap;
import java.util.Set;

import static com.github.qoiu.main.StateStatus.*;

public class PresenterTables implements MainPresenterHashTables.Global {

    private final HashMap<Integer, SendMapper> messageMap = new HashMap<>();
    private final HashMap<Long, Integer> currentQuestion = new HashMap<>();
    private final HashMap<Long, GameEngine> games = new HashMap<>();
    private final HashMap<Long, QuestionTemplate> questionTemplate = new HashMap<>();

    public PresenterTables(DatabaseInterface.Executor db, PlayerNotifier notifier) {

        initMessageMap(db, notifier);
    }

    private void initMessageMap(DatabaseInterface.Executor db, PlayerNotifier notifier) {
        messageMap.put(PLAYER_BASE_STATUS, new SendMapperMainMenu(db, notifier));
        messageMap.put(PLAYER_WAITING_ACTION, new SendMapperMainMenu(db, notifier));
        messageMap.put(PLAYER_CHOSE_GAME, new SendMapperListOfGames(db));
        messageMap.put(PLAYER_WAITING_OTHER_PLAYERS, new SendMapperAddPlayerToGame(db, notifier));
        messageMap.put(PLAYER_WAITING_OTHER_PLAYERS_HOST, new SendMapperCreateGame(db));
        messageMap.put(PLAYER_ADD_QUESTION, new SendMapperEditQuestion(questionTemplate));
        messageMap.put(PLAYER_EDIT_QUESTION, new SendMapperQuestionChangeTitle(questionTemplate));
        messageMap.put(PLAYER_VOTE, new SendMapperVoteQuestion(db,this));
        messageMap.put(PLAYER_HIGHSCOE, new SendMapperHighscore(db));
    }

    @Override
    public SendMapper getSendMsg(int playerStatus) {
        return messageMap.get(playerStatus);
    }

    @Override
    public GameEngine getGame(long playerId) {
        return games.get(playerId);
    }

    @Override
    public QuestionTemplate getEditedQuestion(long playerId) {
        return questionTemplate.get(playerId);
    }

    @Override
    public void setQuestionTemplate(long playerId, QuestionTemplate question) {
        questionTemplate.put(playerId, question);

    }

    @Override
    public void clearQuestionTemplate() {
        questionTemplate.clear();
    }

    @Override
    public void setGameEngine(long playerId, GameEngine engine) {
        games.put(playerId, engine);
    }

    @Override
    public boolean isGameExist(long playerId) {
        return games.containsKey(playerId) && games.get(playerId) != null;
    }

    @Override
    public Set<Long> getGameSet() {
        return games.keySet();
    }

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public boolean isMsgExist(int status) {
        return messageMap.containsKey(status);
    }

    @Override
    public int getCurrentQuestion(long playerId) {
        return currentQuestion.get(playerId);
    }

    @Override
    public void setCurrentQuestion(long playerId, int value) {
        currentQuestion.put(playerId, value);
    }
}
