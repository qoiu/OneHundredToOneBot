package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperClearGame extends DbMapper.Base<Integer, Long> {

    public DbMapperClearGame(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(Long userId) {
        db.execute("DELETE FROM userInGame WHERE id = " + userId);
        db.execute("DELETE FROM game WHERE hostDialogId = " + userId);
        return 0;
    }
}
