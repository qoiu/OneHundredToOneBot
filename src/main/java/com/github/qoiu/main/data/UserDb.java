package com.github.qoiu.main.data;

import java.util.Objects;

import static com.github.qoiu.main.StateStatus.*;

public class UserDb {
    private final long id;
    private int status;
    private final String name;
    private int gamePlay;
    private int gameWin;
    private int highScore;
    private Object extra;

    public UserDb(long id, int status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public UserDb(long id, int status, String name, int gamePlay, int gameWin, int highScore) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.gamePlay = gamePlay;
        this.gameWin = gameWin;
        this.highScore = highScore;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        switch (status) {
            case USER_NOT_IN_GAME:
                return "Не в игре";
            case USER_IS_READY:
                return "готов";
        }
        return "";
    }

    public long getId() {
        return id;
    }

    public int getStatusId() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getExtra() {
        return extra;
    }

    public UserDb setExtra(Object extra) {
        this.extra = extra;
        return this;
    }

    public int getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(int gamePlay) {
        this.gamePlay = gamePlay;
    }

    public int getGameWin() {
        return gameWin;
    }

    public void setGameWin(int gameWin) {
        this.gameWin = gameWin;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof PlayerDb)return(((PlayerDb)o).getId()==this.id);
        if (o == null || getClass() != o.getClass()) return false;

        UserDb that = (UserDb) o;
        return id == that.id && status == that.status && Objects.equals(name, that.name);
    }

}
