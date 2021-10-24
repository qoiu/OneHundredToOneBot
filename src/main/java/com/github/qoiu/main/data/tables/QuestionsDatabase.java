package com.github.qoiu.main.data.tables;

import com.github.qoiu.main.Question;

public interface QuestionsDatabase {
    void addQuestion(Question question);
    Question getQuestion(String question);
}
