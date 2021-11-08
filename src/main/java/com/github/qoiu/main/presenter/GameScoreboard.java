package com.github.qoiu.main.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameScoreboard {

    private final Map<GamePlayer, Integer> gameScores;

    public GameScoreboard(List<GamePlayer> list) {
        gameScores = new HashMap<>();
        for (GamePlayer player : list) {
            gameScores.put(player, 0);
        }
    }

    public int getPlayerScores(GamePlayer player) {
        return gameScores.get(player);
    }

    public void addScores(GamePlayer player, int score) {
        gameScores.put(player, gameScores.get(player) + score);
    }

    public boolean isDraw() {
        boolean draw = false;
        int highScore = getHighscore();
        for (int i : gameScores.values()) {
            if (i == highScore) {
                if (draw) return true;
                draw = true;
            }
        }
        return false;
    }

    public GamePlayer getWinner() {
        if(isDraw())return new GamePlayer(0,"Ничья",0);
        int highScore = getHighscore();
        for (GamePlayer pl : gameScores.keySet()) {
            if (gameScores.get(pl) == highScore) return pl;
        }
        return null;
    }

    public String getWinnerText() {
        GamePlayer winner = getWinner();
        if (winner.getName().equals("Ничья")) return "Ничья";
        return "Победил " + winner.getName();
    }

    private int getHighscore() {
        int max = 0;
        for (int i : gameScores.values()) {
            max = Math.max(max, i);
        }
        return max;
    }

    public String getScoreboard() {
        StringBuilder result = new StringBuilder();
        for (GamePlayer player : gameScores.keySet()) {
            result.append(player.getName()).append(" : ").append(gameScores.get(player)).append("\n");
        }
        return result.toString();
    }
}
