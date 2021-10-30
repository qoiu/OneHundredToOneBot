package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserMessaged;

public class UserMessagedToUserDb implements Mapper<UserMessaged, UserDb> {

    private final int status;

    public UserMessagedToUserDb(int status) {
        this.status = status;
    }

    @Override
    public UserDb map(UserMessaged data) {
        return new UserDb(data.getId(),status,data.getName());
    }
}
