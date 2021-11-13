package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.presenter.QuestionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetQuestionUser extends DbMapper.Base<QuestionTemplate, Integer> {
    public DbMapperGetQuestionUser(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public QuestionTemplate map(Integer questionId) {
        String sql = "SELECT id, text, author FROM questionsFromUsers WHERE id = " + questionId;
        ResultSet resultSet = db.executeQuery(sql);
        QuestionTemplate question = new ResultSetToQuestionTemplateDbMapper(sql).map(
                resultSet);
        if (question != null) {
            sql = "SELECT text, rate FROM answersUsers WHERE questionId = " + question.getId() ;
            ResultSet rs = db.executeQuery(sql);
           try {
               while (rs.next()) {
                   question = new ResultSetToAnswerTemplateMapper(question,sql).map(rs);
               }
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
        return question;
    }
}
