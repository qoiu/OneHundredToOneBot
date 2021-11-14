package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.presenter.QuestionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToAnswerTemplateMapper extends DbMapper.Result<QuestionTemplate> {
    private final QuestionTemplate question;

    public ResultSetToAnswerTemplateMapper(QuestionTemplate questionDb, String sql) {
        super(sql);
        this.question = questionDb;
    }

    @Override
    public QuestionTemplate map(ResultSet set) {

        try {
            int rate = set.getInt("rate");
            String text = set.getString("text");
            question.new Answer(text, rate);
            return question;
        } catch (SQLException e) {
            return exception(e);
        }
    }
}