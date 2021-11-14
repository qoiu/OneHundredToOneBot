
package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperRestartApp extends DbMapper.Base<Integer, Integer> {
    public DbMapperRestartApp(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(Integer nullable) {
        return db.executeUpdate("UPDATE users SET state = 0");
    }
}
