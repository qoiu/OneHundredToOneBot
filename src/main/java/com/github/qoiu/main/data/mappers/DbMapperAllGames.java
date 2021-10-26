package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.tables.DatabaseBase;

import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMapperAllGames extends DbMapper.Base<List<GameObject>, Null> {

    public DbMapperAllGames(DatabaseBase db) {
        super(db);
    }

    @Override
    public List<GameObject> map(Null object) {
        ResultSet set = db.executeQuery("SELECT gameName,hostDialogId,id FROM game");
        ArrayList<GameObject> list = new ArrayList<>();
        try {
            while (set.next()) {
                GameObject newGO = new GameObject(set);
                list.add(newGO);
            }
            for (GameObject game: list) {
                ResultSet userSet = db.executeQuery("SELECT gameId, id, statusGame FROM userInGame WHERE gameId = " + game.getId());
                while (userSet.next()) {
                    game.addUserInGames(new ResultSetToUserInGameDbMapper().map(userSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
