package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddUserAnswer extends DbMapper.Base<Integer, UserDb> {

    public DbMapperAddUserAnswer(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        int qId = (Integer)user.getExtra();
        return db.executeUpdate("INSERT INTO questionAnswered (questionId,userId) VALUES (?,?)",qId, user.getId());
    }
}
