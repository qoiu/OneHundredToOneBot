package com.github.qoiu.main.bot;

import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserInGame;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

class PreparedSendMessages {

    private SendMessage base(String id, String text) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .build();
    }

    private SendMessage base(String id, String text, TelegramBtn btn) {
        return SendMessage.builder()
                        .chatId(id)
                        .text(text)
                        .replyMarkup(btn.getBtnGroup())
                        .build();
    }

    SendMessage botDown(String id) {
        return base(id, "Something went wrong\n\nBot disconnected");
    }

    SendMessage isAlive(String id) {
        return base(id, "Bot is ready!\nWaiting for your commands");
    }

    SendMessage waitingPlayers(String id, String gameCode) {
        TelegramBtn btn = new TelegramBtn();
        btn.addCollum("Начать игру", "/ready");
        return base(id, "Ожидание других игроков.\nКод игры: " + gameCode, btn);
    }

    SendMessage mainMenu(String id) {
        TelegramBtn btn = new TelegramBtn();
        btn.addCollum("Новая игра", "/start");
        btn.addCollum("Присоединиться", "/connecting");
        btn.addCollum("Участвовать в опросе", "quiz");
        return base(id, "Привет, рад тебя видеть!\nТы уже готов начать новую игру?", btn);
    }

    SendMessage connectionError(String id) {
        return base(id, "Невозможно подключиться");
    }

    SendMessage somethingWrong(String id) {
        return base(id, "Что-то пошло не так");
    }

    SendMessage listOfGames(String id, List<GameObject> games) {
        if(games.size()>0){
            TelegramBtn btn = new TelegramBtn();
            for (GameObject game:games) {
                btn.addCollum(game.getName(),"/connect:"+game.getId());
            }
            return base(id, "Список игр", btn);
        }else {
            TelegramBtn btn = new TelegramBtn();
            btn.addCollum("Новая игра", "/start");
            return base(id, "Нет начатых игр. \nБудьте первым!",btn);
        }
    }

    SendMessage gameConnected(String id) {
        return base(id, "Something went wrong\n\nBot disconnected");
    }

    SendMessage waitingHostReady(String id) {
        return base(id, "Ожидание начала игры...");
    }

    SendMessage playerInGame(String id) {
        return base(id, "Поздравляю, Вы в игре!");
    }

    SendMessage hostWaitingPlayers(long id, GameObject game) {
        TelegramBtn btn = new TelegramBtn();
        StringBuilder players = new StringBuilder("Статус игроков: \n");
        for (UserInGame user : game.getUserInGames()) {
            if(game.getHostId()!=user.getId())
                btn.addCollum("Исключить: "+user.getName(),"/remove:"+user.getId());
            players
                    .append(user.getName())
                    .append(": ")
                    .append(user.getStatus().toLowerCase())
                    .append("\n");
        }
        btn.addCollum("Начать игру", "/startGame");
        return base(String.valueOf(id), "Вы готовы начать.\n"+ players,btn);
    }
}
