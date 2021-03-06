package com.github.qoiu.main.bot;

import com.github.qoiu.main.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import static com.github.qoiu.main.StateStatus.*;

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
        return base(id, "Бот устал и отдыхает");
    }

    SendMessage isAlive(String id) {
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Начать", CMD_MENU);
        return base(id, "Добро пожаловать.\nЭто бот для игры сто к одному.",btn);
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

    public SendMessage playerAnswer(String topText, long playerId, Question question){
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Выйти в меню",CMD_LEAVE);
        for (Question.Answer answer : question.getAnswers()){
            String text = (answer.isAnswered())?answer.getText()+" "+question.getPercentageOfAnswer(answer)+"%":"***";
            btn.addColumn(text, " ");
        }
        return base(String.valueOf(playerId), topText+"\n"+question.getText() + "\n",btn);
    }

}
