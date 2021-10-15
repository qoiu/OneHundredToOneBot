package com.github.qoiu.main.bot;

import com.github.qoiu.main.MainPresenterInterface;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot implements BotInterface {

    public Bot(String token) {
        this.token = token;
    }

    private MainPresenterInterface presenter;
    private String token;


    @Override
    public void setPresenter(MainPresenterInterface presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getBotUsername() {
        return "@justBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("sss" + update.getMessage());
        if (update.hasMessage()) {
            Message message = update.getMessage();
            presenter.receiveMessage(message.getFrom(), update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            presenter.receiveCallback(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getData());
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startGame(long id) {
       // presenter.createGameForPlayer(id);
    }
}
