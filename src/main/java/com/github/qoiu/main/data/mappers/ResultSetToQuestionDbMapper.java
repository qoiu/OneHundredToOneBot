package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.QuestionDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToQuestionDbMapper extends DbMapper.Result<QuestionDb> {

    public ResultSetToQuestionDbMapper(String sql) {
        super(sql);
    }

    @Override
    public QuestionDb map(ResultSet set) {

        try {
            int id = set.getInt("id");
            String text = set.getString("text");
            return new QuestionDb(id,text);
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
