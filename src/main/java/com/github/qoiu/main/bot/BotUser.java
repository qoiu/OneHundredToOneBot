package com.github.qoiu.main.bot;

public class BotUser {
    private String name;
    private Long id;

    public BotUser(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}