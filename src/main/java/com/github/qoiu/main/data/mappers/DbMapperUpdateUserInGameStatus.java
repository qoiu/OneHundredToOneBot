package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.PlayerDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperUpdateUserInGameStatus extends DbMapper.Base<Integer, PlayerDb>{
    public DbMapperUpdateUserInGameStatus(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(PlayerDb user) {
        return db.executeUpdate("UPDATE userInGame SET statusGame = ? WHERE id = ?",user.getPlayerStatus(),user.getId());
    }
}
