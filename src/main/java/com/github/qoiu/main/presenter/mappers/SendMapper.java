package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendMapper{
    SendMessage map(UserMessaged userMessaged);


    abstract class Base implements SendMapper{
        protected DatabaseBase db;

        public Base(DatabaseBase db) {
            this.db = db;
        }

        protected SendMessage base(long id, String text) {
            return SendMessage.builder()
                    .chatId(String.valueOf(id))
                    .text(text)
                    .build();
        }

        protected SendMessage base(long id, String text, TelegramBtn btn) {
            return SendMessage.builder()
                    .chatId(String.valueOf(id))
                    .text(text)
                    .replyMarkup(btn.getBtnGroup())
                    .build();
        }
    }
}
