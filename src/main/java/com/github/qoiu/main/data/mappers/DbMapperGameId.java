package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;

import java.sql.SQLException;

public class DbMapperGameId extends DbMapper.Base<Integer,Long> {

    public DbMapperGameId(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(Long id) {
        try {
            return db.executeQuery("SELECT id FROM game WHERE hostDialogId = " + id).getInt("id");
        } catch (SQLException e) {
            return 0;
        }
    }
}
