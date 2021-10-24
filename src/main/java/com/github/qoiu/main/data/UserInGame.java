package com.github.qoiu.main.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.github.qoiu.main.StateStatus.*;

public class UserInGame {
    private long id;
    private int status;
    private String name;

    public UserInGame(long id, int status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public UserInGame(ResultSet set) {
        try {
            this.id = set.getLong("id");
            this.status = set.getInt("statusGame");
            this.name = set.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getStatus(){
        switch (status){
            case USER_NOT_IN_GAME:
                return "Не в игре";
            case USER_IS_READY:
                return "готов";
        }
        return "";
    }

    public long getId() {
        return id;
    }

    public int getStatusId() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
