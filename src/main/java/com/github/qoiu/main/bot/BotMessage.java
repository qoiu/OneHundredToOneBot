package com.github.qoiu.main.bot;

public class BotMessage {
    private final long chatId;
    private final int messageId;

    public BotMessage(long chatId, int messageId) {
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public int getMessageId() {
        return messageId;
    }
}
