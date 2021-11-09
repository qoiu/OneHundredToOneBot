package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.GameObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetGameByGameId extends DbMapper.Base<GameObject, Integer> {

    public DbMapperGetGameByGameId(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public GameObject map(Integer id) {
        String sql = "SELECT gameName,hostDialogId,id FROM game WHERE id = " + id;
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
        }
        return game;
    }
}
