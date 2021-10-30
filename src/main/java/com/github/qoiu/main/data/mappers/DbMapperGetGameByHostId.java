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
        ResultSet set = db.executeQuery("SELECT gameName,hostDialogId,id FROM game WHERE hostDialogId = " + id);
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
