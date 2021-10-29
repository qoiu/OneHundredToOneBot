package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperUpdateUser extends DbMapper.Base<Integer, UserDb> {
    public DbMapperUpdateUser(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        return db.executeUpdate("UPDATE users SET state = ? WHERE id = ?",user.getStatusId(),user.getId());
    }
}
