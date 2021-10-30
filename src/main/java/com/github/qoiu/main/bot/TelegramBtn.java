package com.github.qoiu.main.bot;

import javafx.util.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TelegramBtn {

    private final List<List<InlineKeyboardButton>> collumns = new ArrayList<>();

    void addRow(Pair<String,String>...btn){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Pair<String,String> button:btn) {
            buttons.add(
                    InlineKeyboardButton.builder()
                            .text(button.getKey())
                            .callbackData(button.getValue())
                            .build());
        }
        collumns.add(buttons);
    }

    public void addCollumn(String text, String callback){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback)
                .build());
        collumns.add(buttons);
    }

    public InlineKeyboardMarkup getBtnGroup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(collumns);
        return markup;
    }
}
