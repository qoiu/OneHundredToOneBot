package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.presenter.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class GamePlayersToUserMessagedsMapper implements Mapper<List<GamePlayer>,List<UserMessaged>> {
    @Override
    public List<UserMessaged> map(List<GamePlayer> data) {
        List<UserMessaged> players = new ArrayList<>();
        for (GamePlayer player:data) {
            players.add(new GamePlayerToUserMessagedMapper().map(player));
        }
        return players;
    }
}
