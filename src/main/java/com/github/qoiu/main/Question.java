package com.github.qoiu.main;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Answer> answers = new ArrayList<>();

    public Question(String text,int id) {
        this.text = text;
        this.id = id;
    }

    public Question(String text) {
        this.text = text;
        this.id = 0;
    }

    public int getPercentageOfAnswer(Question.Answer answer){
        double amount=0D;
        for (Question.Answer answ:answers) {
            amount+=answ.rate;
        }
        amount=answer.rate/amount*100;
        return (int) amount;
    }

    public int getPercentageOfAnswer(int id){
        return getPercentageOfAnswer(answers.get(id));
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }


    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public  class Answer {
        private final String text;
        private int rate;
        private boolean answered = false;

        public Answer(String text, int rate) {
            this.text = text;
            this.rate = rate;
            answers.add(this);
        }

        public void setAnswered(){
            answered = true;
        }

        public boolean isAnswered() {
            return answered;
        }

        public int getRate() {
            return rate;
        }

        public String getText() {
            return text;
        }

        public void upgradeRate() {
            rate+=1;
        }
    }
}
