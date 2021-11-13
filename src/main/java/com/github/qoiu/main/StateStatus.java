package com.github.qoiu.main;

public class StateStatus {
    public static final int PLAYER_BASE_STATUS = 0;
    public static final int PLAYER_WAITING_ACTION = 1;
    public static final int PLAYER_CHOSE_GAME = 2;
    public static final int PLAYER_WAITING_OTHER_PLAYERS_HOST = 3;
    public static final int PLAYER_WAITING_OTHER_PLAYERS = 4;
    public static final int PLAYER_IN_GAME = 5;
    public static final int PLAYER_ADD_QUESTION = 10;
    public static final int PLAYER_EDIT_QUESTION = 11;
    public static final int PLAYER_SAVE_QUESTION = 12;

    public static final String CMD_START = "/start";
    public static final String CMD_MENU = "/menu";
    public static final String CMD_NEW_GAME = "/newGame";
    public static final String CMD_CONNECTING = "/connecting";
    public static final String CMD_CONNECT = "/connect";
    public static final String CMD_START_GAME = "/startGame";
    public static final String CMD_BASE = "/base";
    public static final String CMD_ADD_QUESTION = "/addQuestion";
    public static final String CMD_ANSWER = "/answer";
    public static final String CMD_EDIT_QUESTION = "/editQuestion";
    public static final String CMD_SAVE_QUESTION = "/saveQuestion";
    public static final String CMD_REMOVE = "/remove";
    public static final String CMD_LEAVE = "/leave";
    public static final String CMD_VOTE = "/vote";


    public static final int USER_NOT_IN_GAME = 0;
    public static final int USER_IS_READY = 1;
    public static final int USER_IN_GAME = 2;

    public static final int GAME_NOT_STARTED = 0;
    public static final int GAME_STARTED = 1;

}
