package com.github.qoiu.main.data;

import java.util.ArrayList;
import java.util.List;

public class QuestionDb {
    private final int id;
    private final String text;
    private List<Answer> answers;

    public QuestionDb(int id, String text) {
        this.id = id;
        this.text = text;
        answers=new ArrayList<>();
    }

    public void setAnswers(List<Answer> list){
        this.answers = list;
    }
    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public class Answer {
        private final String text;
        private final int rate;


        public Answer(String text, int rate) {
            this.text = text;
            this.rate = rate;
            answers.add(this);
        }

        public int getRate() {
            return rate;
        }

        public String getText() {
            return text;
        }
    }
}
