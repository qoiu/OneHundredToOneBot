package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMapperGetUnvotedQuestionsByPlayers extends DbMapper.Base<List<Integer>, Long> {
    public DbMapperGetUnvotedQuestionsByPlayers(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public List<Integer> map(Long user) {
        String sql = "SELECT questionsFromUsers.id AS qId, vote.questionId, vote.userId " +
                "AS user FROM questionsFromUsers  LEFT JOIN vote ON qId = questionId AND user = " + user;
        ResultSet rs = db.executeQuery(sql);
        List<Integer> result = new ArrayList<>();
        try {
            while (rs.next()) {
                if (rs.getString("questionId") == null)
                    result.add(rs.getInt("qId"));
            }
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
        return result;
    }
}
