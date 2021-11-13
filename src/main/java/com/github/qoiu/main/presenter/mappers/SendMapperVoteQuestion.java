package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGetQuestionUser;
import com.github.qoiu.main.data.mappers.DbMapperGetUnvotedQuestionsByPlayers;
import com.github.qoiu.main.data.mappers.DbMapperUpdateQuestionVoteMark;
import com.github.qoiu.main.data.mappers.DbMapperUpdateQuestionVoteRate;
import com.github.qoiu.main.presenter.MainPresenterHashTables;
import com.github.qoiu.main.presenter.QuestionTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.github.qoiu.main.StateStatus.*;

public class SendMapperVoteQuestion extends SendMapper.Base {
    private  MainPresenterHashTables.CurrentQuestion question;

    public SendMapperVoteQuestion(DatabaseInterface.Executor db, MainPresenterHashTables.CurrentQuestion question) {
        super(db);
        this.question = question;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        String[] msg = userMessaged.getMessage().split(":");
        if (msg[0].equals(CMD_PLAYER_VOTE))
            try {
                int qId = Integer.parseInt(msg[1]);
                String answer = msg[2];
                if (!answer.equals("//bad"))
                    new DbMapperUpdateQuestionVoteRate(db, qId).map(answer);
                new DbMapperUpdateQuestionVoteMark(db, qId).map(userMessaged.getId());
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                String text = "Произошла ужасная ошибка! Сообщите об этом @yjy_jyj";
                TelegramBtn btn = new TelegramBtn();
                btn.addColumn("Задать свой вопрос", CMD_ADD_QUESTION);
                btn.addColumn("Меню", CMD_MENU);
                return base(userMessaged.getId(), text, btn);
            }
        List<Integer> list = new DbMapperGetUnvotedQuestionsByPlayers(db).map(userMessaged.getId());
        if (list.size() == 0) {
            String text = "Нету вопросов, за которые Вы ещё не голосовали.";
            TelegramBtn btn = new TelegramBtn();
            btn.addColumn("Задать свой вопрос", CMD_ADD_QUESTION);
            btn.addColumn("Меню", CMD_MENU);
            return base(userMessaged.getId(), text, btn);
        } else {
            int qId = list.get(new Random().nextInt(list.size()));
            question.setCurrentQuestion(userMessaged.getId(),qId);
            QuestionTemplate template = new DbMapperGetQuestionUser(db).map(qId);

            String text = "Здесь вы можете проголосовать за самый популярный ответ. Когда мы соберём достаточное количество ответов, этот вопрос будет добавлен в игру." +
                    "\nТакже, Вы можете добавить свой вариант ответа\n\nВопрос от "+template.getAuthor()+"\n"+template.getText();
            TelegramBtn btn = new TelegramBtn();
            for (int i = 0; i < template.getAnswers().size(); i++) {
                Question.Answer answer = template.getAnswers().get(i);
                btn.addColumn("✔" + answer.getText(), CMD_PLAYER_VOTE + ":" + qId + ":" + answer.getText());
            }
            btn.addColumn("\uD83D\uDC4E Плохой вопрос", CMD_PLAYER_VOTE + ":" + qId + "://bad");
            btn.addColumn("Меню", CMD_MENU);
            return base(userMessaged.getId(), text, btn);
        }
    }
}
