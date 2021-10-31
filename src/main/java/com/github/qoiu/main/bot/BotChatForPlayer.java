package com.github.qoiu.main.bot;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotChatForPlayer {
    private final HashMap<String, List<Message>> savedChatMessage = new HashMap<>();

    void addMessage(Message message) {
        String id = message.getFrom().getId().toString();
        if (savedChatMessage.containsKey(id))
            savedChatMessage.put(id, new ArrayList<Message>());
        savedChatMessage.get(id).add(message);
    }

    List<Message> getPlayerMessages(long id) {
        if (savedChatMessage.containsKey(String.valueOf(id)))
            return savedChatMessage.get(String.valueOf(id));
        return new ArrayList<>();
    }
}
