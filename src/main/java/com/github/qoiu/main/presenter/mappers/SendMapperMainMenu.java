package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperMainMenu extends SendMapper.Base {
    public SendMapperMainMenu(DatabaseBase db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        TelegramBtn btn = new TelegramBtn();
        btn.addCollumn("Новая игра", "/start");
        btn.addCollumn("Присоединиться", "/connecting");
        btn.addCollumn("Участвовать в опросе", "/quiz");
        return base(userMessaged.getId(),
                "Привет, рад тебя видеть!\nТы уже готов начать новую игру?",
                btn);
    }
}
