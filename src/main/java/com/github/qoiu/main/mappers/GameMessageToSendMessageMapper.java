package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.presenter.GameMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class GameMessageToSendMessageMapper implements Mapper<GameMessage, SendMessage> {

    public GameMessageToSendMessageMapper() {
    }

    @Override
    public SendMessage map(GameMessage data) {
        if (data.getButtons().size() > 0) {
            TelegramBtn telega = new TelegramBtn();
            for (GameMessage.Button btn : data.getButtons()) {
                telega.addColumn(btn.getText(), btn.getCommand());
            }
            return SendMessage.builder()
                    .chatId(String.valueOf(data.getFrom()))
                    .text(data.getText())
                    .replyMarkup(telega.getBtnGroup())
                    .build();
        }
        return SendMessage.builder()
                .chatId(String.valueOf(data.getFrom()))
                .text(data.getText())
                .build();
    }
}
