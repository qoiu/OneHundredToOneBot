package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

import java.sql.SQLException;

public class DbMapperGameHostId extends DbMapper.Base<Integer,Long> {

    public DbMapperGameHostId(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(Long id) {
        try {
            return db.executeQuery("SELECT id FROM game WHERE hostDialogId = " + id).getInt("id");
        } catch (SQLException e) {
            return null;
        }
    }
}
