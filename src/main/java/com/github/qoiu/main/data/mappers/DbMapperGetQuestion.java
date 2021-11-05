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
        String sql = "SELECT id, text FROM questions WHERE id = " + questionId;
        ResultSet resultSet = db.executeQuery(sql);
        QuestionDb questionDb = new ResultSetToQuestionDbMapper(sql).map(
                resultSet);
        if (questionDb != null) {
            sql = "SELECT text, rate FROM answers WHERE questionId = " + questionDb.getId() + " LIMIT 6";
            ResultSet rs = db.executeQuery(sql);
           try {
               while (rs.next()) {
                   questionDb = new ResultSetToAnswerMapper(questionDb,sql).map(rs);
               }
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
        return questionDb;
    }
}
