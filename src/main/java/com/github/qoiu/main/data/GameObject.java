package com.github.qoiu.main.data;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private final String name;
    private final long hostId;
    private final int id;
    private List<PlayerDb> userDbs = new ArrayList<>();

    public GameObject(String name, long hostId, int id) {
        this.name = name;
        this.hostId = hostId;
        this.id = id;
    }

    public void addUserInGames(PlayerDb userDb) {
        userDbs.add(userDb);
    }

    public void setUserInGames(List<PlayerDb> userDbs) {
        this.userDbs = userDbs;
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

    public List<PlayerDb> getUserInGames() {
        return userDbs;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof GameObject)
            return (((GameObject) obj).id == this.id &&
                    ((GameObject) obj).name.equals(this.name) &&
                    ((GameObject) obj).hostId == this.hostId);
        return false;
    }
}
