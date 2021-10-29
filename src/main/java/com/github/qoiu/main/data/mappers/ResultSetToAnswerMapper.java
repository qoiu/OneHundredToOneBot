package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.QuestionDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToAnswerMapper extends DbMapper.Base<QuestionDb, ResultSet> {
    private final QuestionDb question;
    public ResultSetToAnswerMapper(DatabaseBase db, QuestionDb questionDb) {
        super(db);
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
            e.printStackTrace();
            return null;
        }
    }
}
