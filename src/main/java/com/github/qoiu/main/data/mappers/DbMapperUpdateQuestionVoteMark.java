package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperUpdateQuestionVoteMark extends DbMapper.Base<Integer, Long> {
    private int questionId;
    public DbMapperUpdateQuestionVoteMark(DatabaseInterface.Executor db, int questionId) {
        super(db);
        this.questionId = questionId;
    }

    @Override
    public Integer map(Long userId) {
        return db.executeUpdate("INSERT INTO vote (userId, questionId) VALUES (?,?)", userId, questionId);
    }
}
