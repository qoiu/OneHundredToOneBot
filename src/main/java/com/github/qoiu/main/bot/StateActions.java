package com.github.qoiu.main.bot;

import com.github.qoiu.main.StateStatus;

public class StateActions {

    public Integer action(long id,int state, BotInterface bot){
        switch (state){
            case StateStatus.PLAYER_BASE_STATUS:
                return StateStatus.PLAYER_WAITING_ACTION;
        }
        return StateStatus.PLAYER_BASE_STATUS;
    }
}
