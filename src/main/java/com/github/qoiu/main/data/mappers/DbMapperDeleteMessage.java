package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.MessageDb;

public class DbMapperDeleteMessage extends DbMapper.Base<Integer, MessageDb> {
    public DbMapperDeleteMessage(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Integer map(MessageDb msg) {
        db.execute("DELETE FROM lostMessages WHERE  id=? AND message =?", msg.getPlayerId(), msg.getMessageId());
        return 0;
    }
}
