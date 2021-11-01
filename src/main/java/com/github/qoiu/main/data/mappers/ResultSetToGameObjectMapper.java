package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToGameObjectMapper implements DbMapper<GameObject, ResultSet> {

    @Override
    public GameObject map(ResultSet set) {
        Logger log = LogManager.getLogger();
        GameObject game;
        String name = "";
        long hostId = 0;
        int id = 0;
        try {
            name = set.getString("gameName");
        } catch (SQLException e) {
            log.warn("ResultSetToGameObjectMapper can't fill field: name");
        }
        try {
            hostId = set.getLong("hostDialogId");
        } catch (SQLException e) {
            log.warn("ResultSetToGameObjectMapper can't fill field: hostId");
        }
        try {
            id = set.getInt("id");
        } catch (SQLException e) {
            System.out.println("Error!!");
            log.warn("ResultSetToGameObjectMapper can't fill field: id");
        }
        log.printf(Level.WARN,"ResultSetToGameObjectMapper");
        game = new GameObject(name, hostId, id);
        return game;
    }
}
