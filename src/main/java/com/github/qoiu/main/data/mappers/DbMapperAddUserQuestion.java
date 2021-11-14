package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.presenter.QuestionTemplate;

public class DbMapperAddUserQuestion extends DbMapper.Base<Integer, QuestionTemplate> {
    public DbMapperAddUserQuestion(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(QuestionTemplate question) {
        new DbMapperDeleteQuestionWithText(db).map(question.getId());
        String sql = "INSERT INTO questionsFromUsers (text,author) VALUES(?,?)";
       int id = db.executeUpdate(sql,question.getText(),question.getAuthorId());
        db.executeUpdate("DELETE FROM answersUsers WHERE questionId = ?",id);
        for (QuestionTemplate.Answer answer:question.getAnswers()) {
            db.executeUpdate("INSERT INTO answersUsers (questionId,text,rate) VALUES(?,?,?)",id,answer.getText(),answer.getRate());
        }
        return id;
    }
}
