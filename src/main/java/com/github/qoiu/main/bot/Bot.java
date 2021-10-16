package com.github.qoiu.main.bot;

import com.github.qoiu.main.MainPresenterInterface;
import javafx.util.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot implements BotInterface {

    private HashMap<String,Message> savedMessage = new HashMap<>();
    private Set<Long> users = new HashSet<>();
    private List<Message> history = new LinkedList<>();
    private MainPresenterInterface presenter;
    private String token;

    public Bot(String token) {
        this.token = token;
    }


    @Override
    public void onClosing() {
        for (int i=0;i<history.size();i++){
            deleteMsg(history.get(i));
        }
        PreparedSendMessages messages = new PreparedSendMessages();
        for (Long user:users) {
            try {
                Message sent = execute(messages.botDown(user));
                presenter.saveMsg(new Pair<>(sent.getChatId(), sent.getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        super.onClosing();
    }

    @Override
    public void setPresenter(MainPresenterInterface presenter) {
        this.presenter = presenter;
        for (Pair<Long,Integer> elem: presenter.getDisconnectedMessages()) {
            presenter.deletedMsg(elem);
            users.add(elem.getKey());
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(elem.getKey()));
            deleteMessage.setMessageId(elem.getValue());
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
            }
        }
        PreparedSendMessages sendMessages = new PreparedSendMessages();
        for (Long id:users){
            sendMessage(sendMessages.isAlive(id));
        }
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
        if (update.hasMessage()) {
            Message message = update.getMessage();
            users.add(message.getFrom().getId());
            presenter.receiveMessage(message.getFrom(), update.getMessage().getText());
            history.add(message);
            deleteMsg(message);
        } else if (update.hasCallbackQuery()) {
            users.add(update.getCallbackQuery().getFrom().getId());
            presenter.receiveCallback(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getData());
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
           deleteMsg(savedMessage.get(message.getChatId()));
            Message newMessage = execute(message);
            //Epic save for any troubles on server. Save all messages and delete on Start
            presenter.saveMsg(new Pair<>(newMessage.getChatId(), newMessage.getMessageId()));
            history.add(newMessage);
            savedMessage.put(message.getChatId(),newMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void deleteMsg(Message message){
        if(message == null)return;
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        history.remove(message);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startGame(long id) {
       // presenter.createGameForPlayer(id);
    }
}
