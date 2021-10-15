package com.github.qoiu.main.data;

import java.sql.*;

public class MyDb implements DbService, User, WorkWithDb {

    private static Connection connection;
    private static Statement stmt;

    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
            stmt = connection.createStatement();
            connection.prepareStatement("UPDATE users SET state = 0").execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(long id, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (id, name, state) VALUES (?,?,0)");
            ps.setString(1, String.valueOf(id));
            ps.setString(2, name);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean isUserExists(long id) {
        String name;
        try {
            name = userSelect("name", id);
        } catch (SQLException e) {
            name = "";
        }
        return !name.isEmpty();
    }

    public String getById(long id) {
        try {
            return userSelect("name", id);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String userSelect(String type, long id) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement("SELECT id,name,state FROM users WHERE id = ?;");
        ps.setLong(1, id);
        return ps.executeQuery().getString(type);
    }

    public void changeState(long id, int state) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET state = ? WHERE id = ?");
            ps.setInt(1, state);
            ps.setLong(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getState(long id) {
        try {
            return Integer.parseInt(userSelect("state", id));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void addUser(long id, String name) {
        add(id, name);
    }

    @Override
    public String getUser(long id) {
        return getById(id);
    }

    @Override
    public void changeUserState(long id, int state) {
        changeState(id, state);
    }

    @Override
    public Integer getUserState(long id) {
        return getState(id);
    }

    @Override
    public void createGameForPlayer(long id) {
        try {
            connection.prepareStatement("DELETE FROM userInGame WHERE userId = " + id).execute();
            connection.prepareStatement("DELETE FROM game WHERE hostDialogId = " + id).execute();
            connection.prepareStatement("INSERT INTO game(gameName,hostDialogId) VALUES (\"newGame\"," + id + ")").execute();
            int gameId =
                    connection.prepareStatement("SELECT id FROM game ORDER BY id DESC LIMIT 1")
                            .executeQuery()
                            .getInt("id");
            addPlayer(gameId, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(int gameId, long playerId) {
        try {
            PreparedStatement  ps = connection.prepareStatement("INSERT INTO userInGame (gameId, userId) VALUES (?,?)");
            ps.setLong(1, gameId);
            ps.setLong(2, playerId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getGameId(long playerId){
        try {
            return connection.prepareStatement("SELECT id FROM game WHERE hostDialogId = " + playerId)
                            .executeQuery()
                            .getInt("id");
        } catch (SQLException e) {
            return 0;
        }
    }
}
