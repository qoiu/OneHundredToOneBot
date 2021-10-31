package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.PlayerDb;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGameByPlayerId;
import com.github.qoiu.main.data.mappers.DbMapperGetGameByGameId;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperWaitingForPlayersClient extends SendMapper.Base {
    public SendMapperWaitingForPlayersClient(DatabaseBase db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        int gameId =new DbMapperGameByPlayerId(db).map(userMessaged.getId());
        GameObject game = new DbMapperGetGameByGameId(db).map(gameId);
        StringBuilder players = new StringBuilder("Ожидание начала игры...\nСтатус игроков: \n");
        TelegramBtn btn = new TelegramBtn();
        for (PlayerDb user : game.getUserInGames()) {
            if (game.getHostId() != user.getId())
                players
                        .append(user.getName())
                        .append(": готов")
                        .append("\n");
        }
        btn.addColumn("Отменить", "/menu");
        return base(userMessaged.getId(), players.toString(), btn);
    }
}
