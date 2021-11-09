package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperUpdateQuestionAnswerRate extends DbMapper.Base<Integer, Question.Answer> {
    private int questionId;
    public DbMapperUpdateQuestionAnswerRate(DatabaseInterface.Executor db, int questionId) {
        super(db);
        this.questionId = questionId;
    }

    @Override
    public Integer map(Question.Answer answer) {
        return db.executeUpdate("UPDATE answers SET rate = ? WHERE questionId = ? AND text =?",answer.getRate(),questionId, answer.getText());
    }
}
