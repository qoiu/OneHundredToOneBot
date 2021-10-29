package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.MessageDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperAddMessage extends DbMapper.Base<Integer, MessageDb> {
    public DbMapperAddMessage(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(MessageDb message) {
        return db.executeUpdate("INSERT INTO lostMessages (id, message) VALUES (?,?)",message.getPlayerId(),message.getMessageId());
    }
}
