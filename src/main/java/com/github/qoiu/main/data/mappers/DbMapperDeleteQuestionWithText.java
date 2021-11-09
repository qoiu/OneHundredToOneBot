package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperDeleteQuestionWithText extends DbMapper.Base<Integer, Integer> {
    public DbMapperDeleteQuestionWithText(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(Integer id) {
        db.execute("SELECT id, text FROM questions WHERE id = "+ id);
        db.executeUpdate("DELETE FROM questions WHERE  id=? ",id);
        db.execute("DELETE FROM answers WHERE  questionId=? ",id);
        return id;
    }
}
