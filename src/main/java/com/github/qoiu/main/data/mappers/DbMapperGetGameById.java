package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.DatabaseBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMapperGetGameById extends DbMapper.Base<GameObject, Integer> {

    public DbMapperGetGameById(DatabaseBase db) {
        super(db);
    }

    @Override
    public GameObject map(Integer id) {
        ResultSet set = db.executeQuery("SELECT gameName,hostDialogId,id FROM game WHERE hostDialogId = " + id);
        GameObject game = new ResultSetToGameObjectMapper().map(set);
        try {
            ResultSet userSet = db.executeQuery("SELECT gameId, id, statusGame FROM userInGame WHERE gameId = " + game.getId());
            while (userSet.next()) {
                game.addUserInGames(new ResultSetToUserInGameDbMapper().map(userSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }
}
