package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddPlayer extends DbMapper.Base<Integer, UserDb> {

    private final Integer gameId;
    public DbMapperAddPlayer(DatabaseBase db, Integer gameId) {
        super(db);
        this.gameId = gameId;
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("INSERT INTO userInGame (gameId, id) VALUES (?,?)",gameId, user.getId());
    }
}
