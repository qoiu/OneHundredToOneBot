package com.github.qoiu.main.data.tables;

import com.github.qoiu.main.Question;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbQuestions extends DatabaseBase implements QuestionsDatabase {
    @Override
    public void addQuestion(Question question) {
        if(getQuestion(question.getText())==null){
            try {
                execute("INSERT INTO questions (text) VALUES (?)",question.getText());
                int id = executeQuery("SELECT id FROM questions ORDER BY id DESC LIMIT 1").getInt("id");
                System.out.println(id);
            for (Pair<String,Integer> pair:question.getAnswers()) {
                executeUpdate("INSERT INTO answers (questionId,text,rate) VALUES (?,?,?)",id,pair.getKey(),pair.getValue());

            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Question getQuestion(String question) {
        ResultSet rs =executeQuery("SELECT * FROM questions WHERE text = ?", question);
        try {
            if(!rs.isClosed())return new Question(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
