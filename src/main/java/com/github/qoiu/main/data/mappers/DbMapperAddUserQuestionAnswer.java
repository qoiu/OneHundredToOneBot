package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

public class DbMapperAddUserQuestionAnswer extends DbMapper.Base<Integer,String> {
    private int questionId;
    public DbMapperAddUserQuestionAnswer(DatabaseInterface.Executor db,int questionId) {
        super(db);
        this.questionId = questionId;
    }

    @Override
    public Integer map(String text) {
           return db.executeUpdate("INSERT INTO answersUsers (questionId,text,rate) VALUES(?,?,0)",questionId,text);

    }
}
