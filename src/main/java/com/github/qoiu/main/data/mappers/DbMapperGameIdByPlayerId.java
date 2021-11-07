package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;

import java.sql.SQLException;

public class DbMapperGameIdByPlayerId extends DbMapper.Base<Integer,Long> {

    public DbMapperGameIdByPlayerId(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(Long id) {
        try {
            return db.executeQuery("SELECT gameId FROM userInGame WHERE id = " + id).getInt("gameId");
        } catch (SQLException e) {
            return null;
        }
    }
}
