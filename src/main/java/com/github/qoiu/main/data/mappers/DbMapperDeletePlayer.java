package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperDeletePlayer extends DbMapper.Base<Integer, Long> {
    public DbMapperDeletePlayer(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(Long id) {
        db.execute("DELETE FROM userInGame WHERE id = " + id);
        return 0;
    }
}
