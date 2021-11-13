package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.UserMessaged;

public class QuestionTemplate extends Question {
    private static final int ANSWER_IS_NOT_EDITING = -1;
    private int editedAnswer=ANSWER_IS_NOT_EDITING;
    private long authorId;
    private String author;
    public QuestionTemplate() {
        super("Редактировать текст вопроса", 0);
    }

    public QuestionTemplate(UserMessaged userMessaged) {
        super("Редактировать текст вопроса");
        this.authorId = userMessaged.getId();
        this.author = userMessaged.getName();
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void addAnswer(String text){
        this.new Answer(text,0);
    }

    public void removeAnswer(Answer answer){
        getAnswers().remove(answer);
    }

    public void correctAnswer(int id){
        if(id<getAnswers().size())editedAnswer=id;
    }

    public void correctAnswer(String text){
        if(editedAnswer!=ANSWER_IS_NOT_EDITING){
            getAnswers().remove(editedAnswer);
            addAnswer(text);
        }
    }

    public void setTitle(String title){
        setText(title);
    }

}
