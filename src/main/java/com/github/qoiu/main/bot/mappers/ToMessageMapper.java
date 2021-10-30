package com.github.qoiu.main.bot.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ToMessageMapper<T> {
    SendMessage map(T data);

    abstract class Base<T> implements ToMessageMapper<T>{
        private String id;

        public Base(String id) {
            this.id = id;
        }

        SendMessage base(String text) {
            return SendMessage.builder()
                    .chatId(id)
                    .text(text)
                    .build();
        }

        SendMessage base(String text, TelegramBtn btn) {
            return SendMessage.builder()
                    .chatId(id)
                    .text(text)
                    .replyMarkup(btn.getBtnGroup())
                    .build();
        }
    }
}
