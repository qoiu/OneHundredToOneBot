package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DbMapperGetUnansweredQuestionsByPlayers extends DbMapper.Base<Set<Integer>, Set<Long>> {
    public DbMapperGetUnansweredQuestionsByPlayers(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Set<Integer> map(Set<Long> users) {
        StringBuilder extra = new StringBuilder();
        for (Long userId : users) {
            if (extra.length() == 0) {
                extra.append("user = ").append(userId);
            } else {
                extra.append(" OR user = ").append(userId);
            }
        }
        String sql = "SELECT questions.id AS qId, questionAnswered.questionId, questionAnswered.userId" +
                " AS user FROM questions  LEFT JOIN questionAnswered ON qId = questionId ";
        if (!extra.toString().equals("")) sql += "AND (" + extra + ")";
        System.out.println(sql);
        ResultSet rs = db.executeQuery(sql);
        Set<Integer> set = new HashSet<>();
        try {
            while (rs.next()) {
                if (rs.getString("questionId") == null)
                    set.add(rs.getInt("qId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Question question = new DbMQ
        return set;
    }
}
