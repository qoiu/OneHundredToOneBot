package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.PlayerDb;

public class DbMapperUpdateUserInGameStatus extends DbMapper.Base<Integer, PlayerDb>{
    public DbMapperUpdateUserInGameStatus(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(PlayerDb user) {
        return db.executeUpdate("UPDATE userInGame SET statusGame = ? WHERE id = ?",user.getPlayerStatus(),user.getId());
    }
}
