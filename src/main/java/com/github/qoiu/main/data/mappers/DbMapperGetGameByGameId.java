package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetGameByGameId extends DbMapper.Base<GameObject, Integer> {

    public DbMapperGetGameByGameId(DatabaseBase db) {
        super(db);
    }

    @Override
    public GameObject map(Integer id) {
        ResultSet set = db.executeQuery("SELECT gameName,hostDialogId,id FROM game WHERE id = " + id);
        GameObject game = new ResultSetToGameObjectMapper().map(set);
        try {
            ResultSet userSet = db.executeQuery("SELECT gameId, id, statusGame,users.name FROM userInGame NATURAL JOIN users WHERE gameId = " + game.getId());
            while (userSet.next()) {
                game.addUserInGames(new ResultSetToUserInGameDbMapper().map(userSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }
}
