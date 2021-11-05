package com.github.qoiu.main.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        private int rate;


        public Answer(String text, int rate) {
            this.text = text;
            this.rate = rate;
            answers.add(this);
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getRate() {
            return rate;
        }

        public String getText() {
            return text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Answer answer = (Answer) o;
            return rate == answer.rate && Objects.equals(text, answer.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, rate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionDb that = (QuestionDb) o;
        if(answers.size()!=that.answers.size())return false;
        for (int i = 0; i < answers.size(); i++) {
            if (!answers.get(i).equals(that.getAnswers().get(i)))return false;
        }
        return id == that.id && Objects.equals(text, that.text);
    }
}
