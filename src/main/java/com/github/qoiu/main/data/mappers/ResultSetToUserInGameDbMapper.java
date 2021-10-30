package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.PlayerDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserInGameDbMapper implements DbMapper<PlayerDb, ResultSet> {
    @Override
    public PlayerDb map(ResultSet set) {
        long id = 0;
        long gameId = 0;
        int statusGame = 0;
        String name = "";
        try {
            id = set.getLong("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            gameId = set.getLong("gameId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statusGame = set.getInt("statusGame");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            name = set.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PlayerDb(id, gameId, statusGame, name);
    }
}
