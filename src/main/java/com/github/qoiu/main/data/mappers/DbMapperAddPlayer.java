package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

public class DbMapperAddPlayer extends DbMapper.Base<Integer, UserDb> {

    private final Integer gameId;
    public DbMapperAddPlayer(DatabaseInterface.Executor db, Integer gameId) {
        super(db);
        this.gameId = gameId;
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("INSERT INTO userInGame (gameId, id) VALUES (?,?)",gameId, user.getId());
    }
}
