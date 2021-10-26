package com.github.qoiu.main.data;

import java.util.Objects;

public class UserInGameDb {
    private final long id;
    private final long gameId;
    private final int statusGame;

    public UserInGameDb(long id, long gameId, int statusGame) {
        this.id = id;
        this.gameId = gameId;
        this.statusGame = statusGame;
    }

    public long getId() {
        return id;
    }

    public int getStatusGame() {
        return statusGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInGameDb userDb = (UserInGameDb) o;
        return id == userDb.id && gameId == userDb.gameId && statusGame == userDb.statusGame;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, statusGame);
    }
}
