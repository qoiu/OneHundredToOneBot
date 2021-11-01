package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.QuestionDb;

public class QuestionDbToQuestionMapper implements Mapper<QuestionDb, Question> {
    @Override
    public Question map(QuestionDb data) {
        Question question = new Question(data.getText(),data.getId());
        for (QuestionDb.Answer answer:data.getAnswers()) {
            new AnswerDbToAnswerMapper(question).map(answer);
        }
        return question;
    }
}
