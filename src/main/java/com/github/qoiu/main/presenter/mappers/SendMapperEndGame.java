package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.data.UserMessaged;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.qoiu.main.StateStatus.CMD_MENU;

public class SendMapperEndGame extends SendMapper.Base {
    String results;

    public SendMapperEndGame(String results) {
        super(null);
        this.results=results;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        btn.addColumn("В меню", CMD_MENU);
        return base(userMessaged.getId(),
                "Игра завершена\n" + results,
                btn);
    }
}
