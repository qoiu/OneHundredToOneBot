package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

public class DbMapperAddUser extends DbMapper.Base<Integer, UserDb> {

    public DbMapperAddUser(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("INSERT INTO users (id, name, state) VALUES (?,?,0)", user.getId(), user.getName());
    }
}
