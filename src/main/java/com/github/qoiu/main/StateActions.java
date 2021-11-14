package com.github.qoiu.main;

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
        mapCallback.put(CMD_START,PLAYER_WAITING_ACTION);
        mapCallback.put(CMD_MENU,PLAYER_WAITING_ACTION);
        mapCallback.put(CMD_NEW_GAME,PLAYER_WAITING_OTHER_PLAYERS_HOST);
        mapCallback.put(CMD_CONNECTING,PLAYER_CHOSE_GAME);
        mapCallback.put(CMD_CONNECT,PLAYER_WAITING_OTHER_PLAYERS);
        mapCallback.put(CMD_START_GAME,PLAYER_IN_GAME);
        mapCallback.put(CMD_BASE,PLAYER_BASE_STATUS);//you get it when parse of command throw NumberFormatException
        mapCallback.put(CMD_ADD_QUESTION,PLAYER_ADD_QUESTION);
        mapCallback.put(CMD_ANSWER,PLAYER_ADD_QUESTION);
        mapCallback.put(CMD_EDIT_QUESTION,PLAYER_EDIT_QUESTION);
        mapCallback.put(CMD_SAVE_QUESTION,PLAYER_SAVE_QUESTION);
        mapCallback.put(CMD_VOTE,PLAYER_VOTE);
        mapCallback.put(CMD_PLAYER_VOTE,PLAYER_VOTE);
        mapCallback.put(CMD_HIGHSCORE,PLAYER_HIGHSCOE);
    }

    public void initMessages(){
        mapMessage.put(PLAYER_BASE_STATUS,PLAYER_WAITING_ACTION);
    }

    public Integer actionCallback(String command){
        command=command.split(":")[0];
        return mapCallback.getOrDefault(command, PLAYER_BASE_STATUS);
    }

}
