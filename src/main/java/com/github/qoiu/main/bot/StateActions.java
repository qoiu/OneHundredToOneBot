package com.github.qoiu.main.bot;

import java.util.HashMap;
import java.util.Map;

import static com.github.qoiu.main.StateStatus.*;

public class StateActions {

    private final Map<Integer,Integer> mapMessage = new HashMap<>();
    private final Map<String,Integer> mapCallback = new HashMap<>();

    public StateActions() {
        initCallback();
        initMessages();
  }

    private void initCallback(){
        mapCallback.put("/start",PLAYER_WAITING_ACTION);
        mapCallback.put("/menu",PLAYER_WAITING_ACTION);
        mapCallback.put("/newGame",PLAYER_WAITING_OTHER_PLAYERS_HOST);
        mapCallback.put("/connecting",PLAYER_CHOSE_GAME);
        mapCallback.put("/connect",PLAYER_WAITING_OTHER_PLAYERS);
        mapCallback.put("/startGame",PLAYER_IN_GAME);
        mapCallback.put("/base",PLAYER_BASE_STATUS);//you get it when parse of command throw NumberFormatException
    }

    public void initMessages(){
        mapMessage.put(PLAYER_BASE_STATUS,PLAYER_WAITING_ACTION);
    }

    public Integer actionCallback(String command){
        command=command.split(":")[0];
        return mapCallback.containsKey(command)?mapCallback.get(command):PLAYER_BASE_STATUS;
    }

}
