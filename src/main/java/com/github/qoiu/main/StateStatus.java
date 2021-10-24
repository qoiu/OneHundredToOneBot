package com.github.qoiu.main;

public class StateStatus {
    public static final int PLAYER_BASE_STATUS = 0;
    public static final int PLAYER_WAITING_ACTION = 1;
    public static final int PLAYER_CHOSE_GAME = 2;
    public static final int PLAYER_WAITING_OTHER_PLAYERS_HOST = 3;
    public static final int PLAYER_WAITING_OTHER_PLAYERS = 4;
    public static final int PLAYER_IN_GAME = 5;
    public static final int PLAYER_CONNECTION_ERROR = 6;


    public static final int USER_NOT_IN_GAME=0;
    public static final int USER_IS_READY=1;
    public static final int USER_IN_GAME=2;

}
