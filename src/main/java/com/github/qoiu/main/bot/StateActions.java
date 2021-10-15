package com.github.qoiu.main.bot;

import com.github.qoiu.main.StateStatus;

public class StateActions {

    public Integer action(long id,int state, BotInterface bot){
        PreparedSendMessages messages = new PreparedSendMessages();
        switch (state){
            case StateStatus.BASE_STATUS:
                bot.sendMessage(messages.mainMenu(String.valueOf(id)));
                return StateStatus.WAITING_ACTION;
        }
        return StateStatus.BASE_STATUS;
    }
}
