package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.GamePlayerResultsToUserDbMapper;
import com.github.qoiu.main.mappers.GamePlayerToUserDbMapper;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import com.github.qoiu.main.presenter.GamePlayer;

import java.util.Set;

public class GamePresenter implements GameActions{
    private final DatabaseInterface.Executor db;

    public GamePresenter(DatabaseInterface.Executor db) {
        this.db = db;
    }

    @Override
    public Question getQuestion(int questionId) {
        return new QuestionDbToQuestionMapper().map(
                new DbMapperGetQuestion(db).map(questionId));
    }

    @Override
    public Set<Integer> getUnansweredQuestions(Set<Long> ids) {
        return new DbMapperGetUnansweredQuestionsByPlayers(db).map(ids);
    }

    @Override
    public void updateQuestionAnswerRate(Question question, Question.Answer trueAnswer) {
        new DbMapperUpdateQuestionAnswerRate(db, question.getId()).map(trueAnswer);
    }

    @Override
    public void addUserAnswer(int questionId, GamePlayer player) {
        new DbMapperAddUserAnswer(db).map(new GamePlayerToUserDbMapper(5).map(player).setExtra(questionId));
    }

    @Override
    public void updateUser(GamePlayer player) {

    }

    @Override
    public void updateUserResult(GameScoreboard results, GamePlayer player) {
            int win = 0;
            if (player == results.getWinner()) win = 1;
                new DbMapperUpdateUserResults(db).map(
                        new GamePlayerResultsToUserDbMapper(1, win, results.getPlayerScores(player)).map(player));
    }
}
