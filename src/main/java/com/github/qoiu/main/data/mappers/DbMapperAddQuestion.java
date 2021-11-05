package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.QuestionDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddQuestion extends DbMapper.Base<Integer, QuestionDb> {
    public DbMapperAddQuestion(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(QuestionDb question) {
        new DbMapperDeleteQuestionWithText(db).map(question.getId());
        String sql = "INSERT INTO questions (id,text) VALUES(?,?)";
       int id = db.executeUpdate(sql,question.getId(),question.getText());
        db.executeUpdate("DELETE FROM answers WHERE questionId = ?",id);
        for (QuestionDb.Answer answer:question.getAnswers()) {
            db.executeUpdate("INSERT INTO answers (questionId,text,rate) VALUES(?,?,?)",id,answer.getText(),answer.getRate());
        }
        return id;
    }
}
