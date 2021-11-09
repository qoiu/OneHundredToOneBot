package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.*;
import com.github.qoiu.main.data.mappers.DbMapperGetGameByHostId;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperWaitingForPlayersHost extends SendMapper.Base{
    public SendMapperWaitingForPlayersHost(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        GameObject game = new DbMapperGetGameByHostId(db).map(userMessaged.getId());
        StringBuilder players = new StringBuilder("Статус игроков: \n");
        TelegramBtn btn = new TelegramBtn();
        for (PlayerDb user : game.getUserInGames()) {
            if(game.getHostId()!=user.getId())
                btn.addColumn("Исключить: "+user.getName(),"/remove:"+user.getId());
            players
                    .append(user.getName())
                    .append(": готов")
                    .append("\n");
        }
        btn.addColumn("Отменить", "/menu");
        btn.addColumn("Начать игру", "/startGame");
        return base(userMessaged.getId(), "Вы готовы начать?\n"+ players, btn);
    }
}
