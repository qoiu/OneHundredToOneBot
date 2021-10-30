package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.presenter.GamePlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayersIdMapper implements Mapper<List<GamePlayer>,Set<Long>> {
    @Override
    public Set<Long> map(List<GamePlayer> data) {
        Set<Long> list = new HashSet<>();
        for (GamePlayer player:data) {
            list.add(player.getId());
        }
        return list;
    }
}
