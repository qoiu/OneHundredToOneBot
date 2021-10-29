package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.QuestionDb;
import com.github.qoiu.main.data.UserDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToQuestionDbMapper implements DbMapper<QuestionDb, ResultSet> {
    @Override
    public QuestionDb map(ResultSet set) {

        try {
            int id = set.getInt("id");
            String text = set.getString("text");
            return new QuestionDb(id,text);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
