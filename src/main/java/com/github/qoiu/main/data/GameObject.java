package com.github.qoiu.main.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private long hostId;
    private int id;
    private List<UserInGame> userInGames = new ArrayList<>();

    public GameObject(String name, long hostId, int id) {
        this.name = name;
        this.hostId = hostId;
        this.id = id;
    }

    public GameObject(ResultSet set) {
        try {
            this.name = set.getString("gameName");
            this.hostId = set.getLong("hostDialogId");
            this.id = set.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUserInGames(UserInGame userInGame) {
        userInGames.add(userInGame);
    }


    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public int getId() {
        return id;
    }

    public List<UserInGame> getUserInGames() {
        return userInGames;
    }
}
