package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.tables.DatabaseBase;

public class DbMapperUpdateUserInGameStatus extends DbMapper.Base<Integer, UserDb>{
    public DbMapperUpdateUserInGameStatus(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("UPDATE userInGame SET statusGame = ? WHERE id = ?",user.getStatusId(),user.getId());
    }
}
