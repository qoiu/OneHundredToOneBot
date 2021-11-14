package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.presenter.QuestionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToQuestionTemplateDbMapper extends DbMapper.Result<QuestionTemplate> {

    public ResultSetToQuestionTemplateDbMapper(String sql) {
        super(sql);
    }

    @Override
    public QuestionTemplate map(ResultSet set) {

        try {
            int id = set.getInt("id");
            String text = set.getString("text");
            long author = set.getLong("author");
            return new QuestionTemplate(text,id,author);
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
