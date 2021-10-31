package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;

import java.sql.SQLException;

public class DbMapperGameByPlayerId extends DbMapper.Base<Integer,Long> {

    public DbMapperGameByPlayerId(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(Long id) {
        try {
            return db.executeQuery("SELECT gameId FROM userInGame WHERE id = " + id).getInt("gameId");
        } catch (SQLException e) {
            return 0;
        }
    }
}
