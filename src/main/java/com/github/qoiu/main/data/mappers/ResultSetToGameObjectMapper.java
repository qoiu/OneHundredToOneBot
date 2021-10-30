package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToGameObjectMapper implements DbMapper<GameObject, ResultSet> {

    @Override
    public GameObject map(ResultSet set) {
        GameObject game;
        String name = "";
        long hostId = 0;
        int id = 0;
        try {
            name = set.getString("gameName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            hostId = set.getLong("hostDialogId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            id = set.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        game = new GameObject(name, hostId, id);
        return game;
    }
}
