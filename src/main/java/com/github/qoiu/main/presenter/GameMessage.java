package com.github.qoiu.main.presenter;

import java.util.ArrayList;
import java.util.List;

public class GameMessage {
    private final long from;
    private final String text;
    private String header;
    private final List<Button> buttons;

    public GameMessage(long from, String text) {
        this.from = from;
        this.text = text;
        buttons=new ArrayList<>();
    }

    public GameMessage(long from, String text, String header) {
        this.from = from;
        this.text = text;
        this.header = header;
        buttons=new ArrayList<>();
    }

    public long getFrom() {
        return from;
    }

    public void addButton(String text,String command){
        buttons.add(new Button(text, command));
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public String getText() {
        return text;
    }

    public String getHeader() {
        return header;
    }

    public class Button{
        private String text;
        private String command;

        public Button(String text, String command) {
            this.text = text;
            this.command = command;
        }

        public String getText() {
            return text;
        }

        public String getCommand() {
            return command;
        }
    }
}
