package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.presenter.GamePlayer;

public class GamePlayerResultsToUserDbMapper implements Mapper<GamePlayer, UserDb> {
    private final int gamePlay;
    private final int gameWin;
    private final int highScore;

    public GamePlayerResultsToUserDbMapper(int gamePlay, int gameWin, int highScore) {
        this.gamePlay = gamePlay;
        this.gameWin = gameWin;
        this.highScore = highScore;
    }

    @Override
    public UserDb map(GamePlayer data) {
        return new UserDb(data.getId(),0,data.getName(),gamePlay,gameWin,highScore);
    }
}
