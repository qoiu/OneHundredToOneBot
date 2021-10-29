package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.MessageDb;
import com.github.qoiu.main.data.DatabaseBase;

public class DbMapperDeleteMessage extends DbMapper.Base<Integer, MessageDb> {
    public DbMapperDeleteMessage(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(MessageDb msg) {
        db.execute("DELETE FROM lostMessages WHERE  id=? AND message =?", msg.getPlayerId(), msg.getMessageId());
        return 0;
    }
}
