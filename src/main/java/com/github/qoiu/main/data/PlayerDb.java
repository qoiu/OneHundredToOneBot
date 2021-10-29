package com.github.qoiu.main.data;

public class PlayerDb {
    private final long id;
    private final long gameId;
    private int playerStatus;
    private String name;

    public PlayerDb(long id, long gameId, int playerStatus) {
        this.id = id;
        this.gameId = gameId;
        this.playerStatus = playerStatus;
    }

    public PlayerDb(long id, long gameId, int playerStatus, String name) {
        this.id = id;
        this.gameId = gameId;
        this.playerStatus = playerStatus;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(int playerStatus) {
        this.playerStatus = playerStatus;
    }
}
