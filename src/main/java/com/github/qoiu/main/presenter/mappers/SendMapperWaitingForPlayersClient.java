package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.*;
import com.github.qoiu.main.data.mappers.DbMapperGameIdByPlayerId;
import com.github.qoiu.main.data.mappers.DbMapperGetGameByGameId;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import static com.github.qoiu.main.StateStatus.*;

public class SendMapperWaitingForPlayersClient extends SendMapper.Base {
    public SendMapperWaitingForPlayersClient(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        int gameId =new DbMapperGameIdByPlayerId(db).map(userMessaged.getId());
        GameObject game = new DbMapperGetGameByGameId(db).map(gameId);
        StringBuilder players = new StringBuilder("Ожидание начала игры...\nСтатус игроков: \n");
        TelegramBtn btn = new TelegramBtn();
        for (PlayerDb user : game.getUserInGames()) {
                players
                        .append(user.getName())
                        .append(": готов")
                        .append("\n");
        }
        btn.addColumn("Отменить", CMD_MENU);
        return base(userMessaged.getId(), players.toString(), btn);
    }
}
