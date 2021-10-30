package com.github.qoiu.main.data;

public class MessageDb {
    private final long playerId;
    private final int messageId;

    public MessageDb(long playerId, int messageId) {
        this.playerId = playerId;
        this.messageId = messageId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int getMessageId() {
        return messageId;
    }
}
