package com.github.qoiu.main;

import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private String text;
    private List<Pair<String, Integer>> answers = new ArrayList<>();

    Question(String text) {
        this.text = text;
    }

    public Question(ResultSet set) {
        try{
        text = set.getString("text");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text;
    }

    public List<Pair<String, Integer>> getAnswers() {
        return answers;
    }

    public void addAnswers(ResultSet set){

    }


    void addAnswer(String c, Integer integer) {

        answers.add(new Pair<>(c, integer));
    }
}
