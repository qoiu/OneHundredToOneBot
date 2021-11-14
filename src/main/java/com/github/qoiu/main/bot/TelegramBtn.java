package com.github.qoiu.main.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TelegramBtn {

    private final List<List<InlineKeyboardButton>> columns = new ArrayList<>();

    public void addRow(Btn...btn){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Btn button:btn) {
            buttons.add(
                    InlineKeyboardButton.builder()
                            .text(button.getText())
                            .callbackData(button.getCommand())
                            .build());
        }
        columns.add(buttons);

    }

    public class Btn{
        public Btn(String text, String command) {
            this.text = text;
            this.command = command;
        }

        final String text;
        final String command;

        public String getText() {
            return text;
        }

        public String getCommand() {
            return command;
        }
    }

    public void addColumn(String text, String callback){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback)
                .build());
        columns.add(buttons);
    }

    public InlineKeyboardMarkup getBtnGroup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(columns);
        return markup;
    }
}
