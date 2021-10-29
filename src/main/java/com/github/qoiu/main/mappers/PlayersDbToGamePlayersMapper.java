package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.PlayerDb;
import com.github.qoiu.main.presenter.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayersDbToGamePlayersMapper implements Mapper<List<PlayerDb>,List<GamePlayer>> {
    @Override
    public List<GamePlayer> map(List<PlayerDb> data) {
        List<GamePlayer> players = new ArrayList<>();
        for (PlayerDb player:data) {
            players.add(new PlayerDbToGamePlayerMapper().map(player));
        }
        return null;
    }
}
