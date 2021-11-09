package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

public class DbMapperAddUserAnswer extends DbMapper.Base<Integer, UserDb> {

    public DbMapperAddUserAnswer(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        int qId = (Integer)user.getExtra();
        return db.executeUpdate("INSERT INTO questionAnswered (questionId,userId) VALUES (?,?)",qId, user.getId());
    }
}
