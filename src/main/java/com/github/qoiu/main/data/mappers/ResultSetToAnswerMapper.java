package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.QuestionDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToAnswerMapper extends DbMapper.Result<QuestionDb, ResultSet> {
    private final QuestionDb question;

    public ResultSetToAnswerMapper(QuestionDb questionDb,String sql) {
        super(sql);
        this.question = questionDb;
    }


    @Override
    public QuestionDb map(ResultSet set) {

        try {
            int rate = set.getInt("rate");
            String text = set.getString("text");
            question.new Answer(text,rate);
            return question;
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
