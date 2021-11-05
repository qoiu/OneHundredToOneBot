package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.DatabaseBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetGameByHostId extends DbMapper.Base<GameObject, Long> {

    public DbMapperGetGameByHostId(DatabaseBase db) {
        super(db);
    }

    @Override
    public GameObject map(Long id) {
        String sql = "SELECT gameName,hostDialogId,id FROM game WHERE hostDialogId = " + id;
        ResultSet set = db.executeQuery(sql);
        GameObject game = new ResultSetToGameObjectMapper(sql).map(set);
        if(game==null)return null;
        try {
            sql = "SELECT gameId, id, statusGame,users.name FROM userInGame NATURAL JOIN users WHERE gameId = " + game.getId();
            ResultSet userSet = db.executeQuery(sql);
            while (userSet.next()) {
                game.addUserInGames(new ResultSetToUserInGameDbMapper(sql).map(userSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return game;
    }
}
