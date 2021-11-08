package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.presenter.GamePlayer;

public class GamePlayerToUserDbMapper implements Mapper<GamePlayer, UserDb> {
    private int status;
    private int gamePlay;
    private int gameWin;
    private int highScore;
    public GamePlayerToUserDbMapper(int status) {
        this.status = status;
    }

    @Override
    public UserDb map(GamePlayer data) {
        return new UserDb(data.getId(),status,data.getName());
    }
}
