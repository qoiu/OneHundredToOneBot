package com.github.qoiu.main.bot;

import com.github.qoiu.main.mappers.BotUserToUserMessagedMapper;
import com.github.qoiu.main.presenter.MainPresenterInterface;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot implements BotInterface {

    private final HashMap<String, Message> savedMessage = new HashMap<>();
    private BotChatForPlayer chat;
    private Set<Long> users = new HashSet<>();
    private final List<Message> history = new LinkedList<>();
    private MainPresenterInterface presenter;
    private String token;

    public Bot(String token) {
        this(new DefaultBotOptions());
        this.token = token;
        chat = new BotChatForPlayer();
    }

    private Bot(DefaultBotOptions options) {
        super(options);
    }

    public void clearChat(){
        if(history.size()>0)
        for (int i = 0; i <history.size() ; i++) {
            deleteMsg(history.get(i));
        }
        PreparedSendMessages messages = new PreparedSendMessages();
        List<Long> list= new ArrayList<>(Arrays.asList(users.toArray(new Long[0]))) ;
        for (int i = 0; i < list.size(); i++) {
            Long user = list.get(i);
            try {
                Message sent = execute(messages.botDown(String.valueOf(user)));
                presenter.saveMsg(new BotMessage(sent.getChatId(), sent.getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        for (Long user : list) {
            clearChat(user);
        }
    }

    @Override
    public void setPresenter(MainPresenterInterface presenter) {
        this.presenter = presenter;
        for (BotMessage elem : presenter.getDisconnectedMessages()) {
            presenter.deletedMsg(elem);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(elem.getChatId()));
            deleteMessage.setMessageId(elem.getMessageId());
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
            }
        }
        PreparedSendMessages sendMessages = new PreparedSendMessages();
        users = presenter.getAllUsers();
        for (Long id : users) {
            sendMessage(sendMessages.isAlive(String.valueOf(id)));
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
            User user = message.getFrom();
            users.add(user.getId());
            presenter.receiveMessage(new BotUserToUserMessagedMapper.Base().map(user, message.getText()));
            history.add(message);
            deleteMsg(message);
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            if (query.getData() != null && query.getData().startsWith("/")) {
                users.add(update.getCallbackQuery().getFrom().getId());
                presenter.receiveCallback(new BotUserToUserMessagedMapper.Base().map(query.getFrom(), query.getData()));
            }
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        if (message != null)
            try {
                deleteMsg(savedMessage.get(message.getChatId()));
                Message newMessage = execute(message);
                //Epic save for any troubles on server. Save all messages and delete on Start
                presenter.saveMsg(new BotMessage(newMessage.getChatId(), newMessage.getMessageId()));
                history.add(newMessage);
                savedMessage.put(message.getChatId(), newMessage);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    public void sendChatMessage(BotChatMessage chatMessage) {
        String msg = "*" + chatMessage.getFrom() + "* : \n" + chatMessage.getText();
        SendMessage message = SendMessage.builder()
                .parseMode("MarkdownV2")
                .text(msg)
                .chatId(String.valueOf(chatMessage.getTo()))
                .build();
        try {
            Message sent = execute(message);
            history.add(sent);
            chat.addMessage(sent, chatMessage.getTo());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearChat(long id) {
        for (Message message : chat.getPlayerMessages(id)) {
            deleteMsg(message);
        }
        chat.clearChat(id);
    }


    private void deleteMsg(Message message) {
        if (message == null) return;
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
}
