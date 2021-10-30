package com.github.qoiu.main.presenter;

public class GamePlayer {
    private long id;
    private String name;
    private int status;

    public GamePlayer(long id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
