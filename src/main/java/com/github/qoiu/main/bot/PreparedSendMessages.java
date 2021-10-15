package com.github.qoiu.main.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class PreparedSendMessages {

    private SendMessage base(String id, String text){
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .build();
    }

    private SendMessage base(String id, String text, TelegramBtn btn){
        return SendMessage.builder()
                        .chatId(id)
                        .text(text)
                        .replyMarkup(btn.getBtnGroup())
                        .build();
    }

    public SendMessage isAlive() {
        return base("794341050","Bot is ready!\nWaiting for your commands");
    }

    SendMessage waitingPlayers(String id, String gameCode){
        TelegramBtn btn = new TelegramBtn();
        btn.addCollum("Начать игру","/ready");
        return base(id, "Ожидание других игроков.\nКод игры: "+gameCode);
    }

    SendMessage mainMenu(String id){
        TelegramBtn btn = new TelegramBtn();
        btn.addCollum("Новая игра","/start");
        btn.addCollum("Присоединиться","/connect");
        btn.addCollum("Участвовать в опросе","quiz");
        return base(id,"Привет, рад тебя видеть!\nТы уже готов начать новую игру?", btn);
    }

    SendMessage connectionError(String id){
        return base(id,"Невозможно подключиться");
    }
}
