package com.github.qoiu.main.data.tables;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserInGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbGame extends DatabaseBase implements GameDatabase {

    @Override
    public void createGameForPlayer(long id, String gameName) {
        try {
            execute("DELETE FROM userInGame WHERE id = " + id);
            execute("DELETE FROM game WHERE hostDialogId = " + id);
            execute("INSERT INTO game(gameName,hostDialogId) VALUES (?,?)", gameName, id);
            int gameId = executeQuery("SELECT id FROM game ORDER BY id DESC LIMIT 1").getInt("id");
           connectPlayer(gameId,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGameId(long playerId) {
        try {
            return executeQuery("SELECT id FROM game WHERE hostDialogId = " + playerId).getInt("id");
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<GameObject> getAllGames() {
        ResultSet set = executeQuery("SELECT gameName,hostDialogId,id FROM game");
        ArrayList<GameObject> list = new ArrayList<>();
        try {
            while (set.next()) {
                GameObject newGO = new GameObject(set);
                ResultSet userSet = executeQuery("SELECT * FROM userInGame NATURAL JOIN users WHERE gameId =" + newGO.getId());
                while (userSet.next()) {
                    newGO.addUserInGames(new UserInGame(userSet));
                }
                list.add(newGO);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GameObject getGame(int gameId) {
        ResultSet set = executeQuery("SELECT gameName,hostDialogId,id FROM game WHERE id = " + gameId);
        GameObject newGO = new GameObject(set);
        ResultSet userSet = executeQuery("SELECT * FROM userInGame NATURAL JOIN users WHERE gameId =" + newGO.getId());
        try {
            while (userSet.next()) {
                newGO.addUserInGames(new UserInGame(userSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newGO;
    }

    @Override
    public void changeStatus(Long id, int state) {
        executeUpdate("UPDATE userInGame SET statusGame = ? WHERE id = ?",state,id);
    }

    @Override
    public void connectPlayer(int gameId, long playerId) {
        execute("INSERT INTO userInGame (gameId, id) VALUES (?,?)", gameId, playerId);
    }


}
