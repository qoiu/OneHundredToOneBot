package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.MessageDb;

public class DbMapperAddMessage extends DbMapper.Base<Integer, MessageDb> {
    public DbMapperAddMessage(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(MessageDb message) {
        return db.executeUpdate("INSERT INTO lostMessages (id, message) VALUES (?,?)",message.getPlayerId(),message.getMessageId());
    }
}
