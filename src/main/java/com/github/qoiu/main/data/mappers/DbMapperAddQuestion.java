package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.QuestionDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddQuestion extends DbMapper.Base<Integer, QuestionDb> {
    public DbMapperAddQuestion(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(QuestionDb question) {
        int id = db.executeUpdate("INSERT INTO questions (id,text) VALUES(?,?) ON CONFLICT(id) DO UPDATE SET text=?",question.getId(),question.getText(),question.getText());
        for (QuestionDb.Answer answer:question.getAnswers()) {
            db.execute("DELETE FROM answers WHERE questionId = ?",id);
            db.executeUpdate("INSERT INTO answers (questionId,text,rate) VALUES(?,?)",question.getId(),answer.getText(),answer.getRate());
        }
        return id;
    }
}
