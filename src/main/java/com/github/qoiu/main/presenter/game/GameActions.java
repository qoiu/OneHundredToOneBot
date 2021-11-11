package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.presenter.GamePlayer;

import java.util.Set;

public interface GameActions {
    Question getQuestion(int questionId);
    Set<Integer> getUnansweredQuestions(Set<Long> ids);

    void updateQuestionAnswerRate(Question question, Question.Answer trueAnswer);
    void addUserAnswer(int questionId, GamePlayer player);
    void updateUser(GamePlayer player);
    void updateUserResult(GameScoreboard results, GamePlayer player);
}
