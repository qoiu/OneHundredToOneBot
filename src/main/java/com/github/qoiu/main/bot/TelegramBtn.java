package com.github.qoiu.main.bot;

import javafx.util.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

class TelegramBtn {

    private List<List<InlineKeyboardButton>> collums = new ArrayList<>();

    void addRow(Pair<String,String>...btn){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Pair<String,String> button:btn) {
            buttons.add(
                    InlineKeyboardButton.builder()
                            .text(button.getKey())
                            .callbackData(button.getValue())
                            .build());
        }
        collums.add(buttons);
    }

    void addCollum(String text, String callback){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback)
                .build());
        collums.add(buttons);
    }

    InlineKeyboardMarkup getBtnGroup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(collums);
        return markup;
    }
}
