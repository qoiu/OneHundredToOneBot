package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.QuestionDb;

public class AnswerToAnswerDbMapper implements Mapper<Question.Answer,QuestionDb.Answer> {

    private QuestionDb questionDb;

    public AnswerToAnswerDbMapper(QuestionDb questionDb) {
        this.questionDb = questionDb;
    }

    @Override
    public QuestionDb.Answer map(Question.Answer data) {
        return questionDb.new Answer(data.getText(), data.getRate());
    }
}
