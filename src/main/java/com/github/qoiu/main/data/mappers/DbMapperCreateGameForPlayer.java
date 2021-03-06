package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

public class DbMapperCreateGameForPlayer extends DbMapper.Base<Integer, UserDb> {

    public DbMapperCreateGameForPlayer(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        db.execute("DELETE FROM userInGame WHERE id = " + user.getId());
        db.execute("DELETE FROM game WHERE hostDialogId = " + user.getId());
        int gameId =db.executeUpdate("INSERT INTO game(gameName,hostDialogId) VALUES (?,?)", user.getName(), user.getId());
        db.execute("INSERT INTO userInGame (gameId, id) VALUES (?,?)", gameId, user.getId());
        return gameId;
    }
}
