package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.tables.DatabaseBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGameById extends DbMapper.Base<GameObject, Integer> {

    public DbMapperGameById(DatabaseBase db) {
        super(db);
    }

    @Override
    public GameObject map(Integer id) {
        ResultSet set = db.executeQuery("SELECT gameName,hostDialogId,id FROM game WHERE hostDialogId = " + id);
        GameObject game = new ResultSetToGameObjectMapper().map(set);
        try {
            ResultSet userSet = db.executeQuery("SELECT userInGame.id, userInGame.statusGame, users.name FROM users INNER JOIN userInGame ON users.id = userInGame.id WHERE gameId =" + game.getId());
            while (userSet.next()) {
                game.setUserInGames(new ResultSetToListUserInGameMapper().map(userSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }
}
