package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.DatabaseBase;

import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class return list of all games even if it's empty
 */
public class DbMapperAllGames extends DbMapper.Base<List<GameObject>, Null> {

    public DbMapperAllGames(DatabaseBase db) {
        super(db);
    }

    @Override
    public List<GameObject> map(Null object) {
        String sql = "SELECT gameName,hostDialogId,id FROM game";
        ResultSet set = db.executeQuery(sql);
        ArrayList<GameObject> list = new ArrayList<>();
        try {
            while (set.next()) {
                GameObject newGO = new ResultSetToGameObjectMapper(sql).map(set);
                list.add(newGO);
            }
            for (GameObject game : list) {
                sql = "SELECT gameId, id, statusGame, users.name FROM userInGame NATURAL JOIN users WHERE gameId = " + game.getId();
                ResultSet userSet = db.executeQuery(sql);
                try {
                    while (userSet.next()) {
                        game.addUserInGames(new ResultSetToUserInGameDbMapper(sql).map(userSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
