package com.github.qoiu.main.bot;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.data.GameObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class PreparedSendMessages {

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
        TelegramBtn btn = new TelegramBtn();
        btn.addCollumn("Hello", "/menu");
        return base(id, "Bot is ready!\nWaiting for your commands",btn);
    }

    SendMessage connectionError(String id) {
        return base(id, "Невозможно подключиться");
    }

    SendMessage somethingWrong(String id) {
        return base(id, "Что-то пошло не так");
    }

    SendMessage gameConnected(String id) {
        return base(id, "Something went wrong\n\nBot disconnected");
    }

    SendMessage waitingHostReady(String id) {
        return base(id, "Ожидание начала игры...");
    }

    SendMessage hostWaitingPlayers(long id, GameObject game) {
        TelegramBtn btn = new TelegramBtn();
        StringBuilder players = new StringBuilder("Статус игроков: \n");
        // TODO: 30.10.2021 Add remove players
//        for (PlayerDb user : game.getUserInGames()) {
//            if(game.getHostId()!=user.getId())
//                btn.addCollum("Исключить: "+user.getName(),"/remove:"+user.getId());
//            players
//                    .append(user.getName())
//                    .append(": ")
//                    .append(user.getStatus().toLowerCase())
//                    .append("\n");
//        }
        btn.addCollumn("Начать игру", "/startGame");
        return base(String.valueOf(id), "Вы готовы начать.\n"+ players,btn);
    }

    public SendMessage playerAnswer(String topText, long playerId, Question question,int timer){
        TelegramBtn btn = new TelegramBtn();
        for (Question.Answer answer : question.getAnswers()){
            String text = (answer.isAnswered())?answer.getText():"***";
            btn.addCollumn(text, " ");

        }
        return base(String.valueOf(playerId), topText+"\n"+question.getText() + "\n"+ "Осталось: "+timer+" сек.",btn);
    }

    public SendMessage playerActivePlayerAnswer(long playerId, Question question,int timer){
        TelegramBtn btn = new TelegramBtn();
        for (Question.Answer answer : question.getAnswers()){
            String text = (answer.isAnswered())?answer.getText():"***";
            btn.addCollumn(text, "");

        }
        return base(String.valueOf(playerId), "Вы отвечаете на вопрос:\n"+question + "\n"+ "Осталось: "+timer+" сек.",btn);
    }
}
