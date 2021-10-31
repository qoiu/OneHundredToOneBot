package com.github.qoiu.main.bot;

public class BotChatMessage {
    private final String from;
    private final String text;
    private long to;

    public BotChatMessage(String from, String text, long to) {
        this.from = from;
        this.text = text;
        this.to = to;
    }

    public BotChatMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public long getTo() {
        return to;
    }
}
