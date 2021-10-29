package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddUser extends DbMapper.Base<Integer, UserDb> {

    public DbMapperAddUser(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("INSERT INTO users (id, name, state) VALUES (?,?,0)", user.getId(), user.getName());
    }
}
