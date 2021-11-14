package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGetAllUsersScores;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static com.github.qoiu.main.StateStatus.CMD_MENU;

public class SendMapperHighscore extends SendMapper.Base {
    public SendMapperHighscore(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        List<UserDb> userDb = new DbMapperGetAllUsersScores(db).map(null);
        TelegramBtn btn = new TelegramBtn();
        String header = "";
        for (UserDb user : userDb) {
            if (user.getId() == userMessaged.getId()) header = "Ваш рекорд: " + user.getHighScore();
            btn.addRow(
                    btn.new Btn(user.getName(), " "),
                    btn.new Btn("Побед: " + user.getGameWin() + "/" + user.getGamePlay(), " "),
                    btn.new Btn("Рекорд: " + user.getHighScore(), " ")
            );
        }
        btn.addColumn("Меню",CMD_MENU);
        return base(userMessaged.getId(), header, btn);
    }
}
