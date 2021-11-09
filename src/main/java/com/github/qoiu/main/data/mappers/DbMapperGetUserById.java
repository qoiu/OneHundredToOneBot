package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

import java.sql.ResultSet;

public class DbMapperGetUserById extends DbMapper.Base<UserDb,Long>{
    public DbMapperGetUserById(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public UserDb map(Long id) {
        String sql = "SELECT * FROM users WHERE id = " + id;
        ResultSet set = db.executeQuery(sql);
        return new ResultSetToUserDbMapper(sql).map(set);
    }
}
