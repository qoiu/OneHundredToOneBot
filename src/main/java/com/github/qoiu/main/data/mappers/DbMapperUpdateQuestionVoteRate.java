package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperUpdateQuestionVoteRate extends DbMapper.Base<Integer, String> {
    private int questionId;
    public DbMapperUpdateQuestionVoteRate(DatabaseInterface.Executor db, int questionId) {
        super(db);
        this.questionId = questionId;
    }

    @Override
    public Integer map(String answer) {
        return db.executeUpdate("UPDATE answersUsers SET rate = rate+1 WHERE questionId = ? AND text =?",questionId, answer);
    }
}
