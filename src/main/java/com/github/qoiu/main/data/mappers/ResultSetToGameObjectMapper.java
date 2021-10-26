package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToGameObjectMapper implements DbMapper<GameObject, ResultSet> {

    @Override
    public GameObject map(ResultSet set) {
        GameObject game;
        try {
            String name = set.getString("gameName");
            long hostId = set.getLong("hostDialogId");
            int id = set.getInt("id");

            game = new GameObject(name,hostId,id);
        } catch (SQLException e) {
            e.printStackTrace();
            game = new GameObject("Error",0,0);
        }
        return game;
    }
}
