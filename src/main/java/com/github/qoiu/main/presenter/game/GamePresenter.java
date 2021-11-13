package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.mappers.*;
import com.github.qoiu.main.mappers.GamePlayerResultsToUserDbMapper;
import com.github.qoiu.main.mappers.GamePlayerToUserDbMapper;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import com.github.qoiu.main.presenter.GameMessage;
import com.github.qoiu.main.presenter.GamePlayer;
import com.github.qoiu.main.presenter.MessageSender;

import java.util.Set;

import static com.github.qoiu.main.StateStatus.CMD_MENU;

public class GamePresenter implements GameActions{
    private final DatabaseInterface.Executor db;
    private final  MessageSender sender;

    public GamePresenter(DatabaseInterface.Executor db, MessageSender sender) {
        this.db = db;
        this.sender = sender;
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
        sender.clearChat(player.getId());
        String text = results.getWinnerText() + "\nИгра завершена\n";
        GameMessage msg = new GameMessage(player.getId(),text + results);
        msg.addButton("В меню", CMD_MENU);
        sender.sendMessage(msg);
            int win = 0;
            if (player == results.getWinner()) win = 1;
                new DbMapperUpdateUserResults(db).map(
                        new GamePlayerResultsToUserDbMapper(1, win, results.getPlayerScores(player)).map(player));
    }
}
