package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.QuestionDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetQuestion extends DbMapper.Base<QuestionDb, Integer> {
    public DbMapperGetQuestion(DatabaseBase db) {
        super(db);
    }

    @Override
    public QuestionDb map(Integer questionId) {
        QuestionDb questionDb = new ResultSetToQuestionDbMapper().map(
                db.executeQuery("SELECT id, text FROM questions WHERE questions.id = " + questionId));
        if (questionDb != null) {
            ResultSet rs = db.executeQuery("SELECT text, rate FROM answers WHERE questionId = " + questionDb.getId() + " LIMIT 6");
           try {
               while (rs.next()) {
                   questionDb = new ResultSetToAnswerMapper(db,questionDb).map(rs);
               }
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
        return questionDb;
    }
}
