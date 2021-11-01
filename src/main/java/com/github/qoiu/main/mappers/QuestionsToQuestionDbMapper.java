package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.QuestionDb;

public class QuestionsToQuestionDbMapper implements Mapper<Question, QuestionDb> {
    @Override
    public QuestionDb map(Question data) {
        QuestionDb questionDb = new QuestionDb(data.getId(),data.getText());
        for (Question.Answer answer:data.getAnswers()) {
            questionDb.addAnswer(new AnswerToAnswerDbMapper(questionDb).map(answer));
        }
        return questionDb;
    }
}
