package com.github.qoiu.main.data;

import java.util.Objects;

import static com.github.qoiu.main.StateStatus.*;

public class UserDb {
    private final long id;
    private int status;
    private final String name;

    public UserDb(long id, int status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        switch (status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof UserInGameDb)return(((UserInGameDb)o).getId()==this.id);
        if (o == null || getClass() != o.getClass()) return false;

        UserDb that = (UserDb) o;
        return id == that.id && status == that.status && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, name);
    }
}
