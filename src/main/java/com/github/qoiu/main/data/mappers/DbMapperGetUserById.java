package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.DatabaseBase;

import java.sql.ResultSet;

public class DbMapperGetUserById extends DbMapper.Base<UserDb,Long>{
    public DbMapperGetUserById(DatabaseBase db) {
        super(db);
    }

    @Override
    public UserDb map(Long id) {
        ResultSet set = db.executeQuery("SELECT name, id, state FROM users WHERE id = "+id);
        return new ResultSetToUserDbMapper().map(set);
    }
}
