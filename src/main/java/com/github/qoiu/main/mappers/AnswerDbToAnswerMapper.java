package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.QuestionDb;

public class AnswerDbToAnswerMapper implements Mapper<QuestionDb.Answer,Question.Answer> {

    private Question question;

    public AnswerDbToAnswerMapper(Question question) {
        this.question = question;
    }

    @Override
    public Question.Answer map(QuestionDb.Answer data) {
        return question.new Answer(data.getText(), data.getRate());
    }
}
