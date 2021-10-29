package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.PlayerDb;
import com.github.qoiu.main.presenter.GamePlayer;

public class PlayerDbToGamePlayerMapper implements Mapper<PlayerDb, GamePlayer> {
    @Override
    public GamePlayer map(PlayerDb data) {
        return new GamePlayer(data.getId(),data.getName(),data.getPlayerStatus());
    }
}
