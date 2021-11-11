package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.presenter.GamePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface ScoreboardResults{
    int getPlayerScores(GamePlayer player);
}

public interface GameScoreboard extends ScoreboardResults{
    String getWinnerText();

    GamePlayer getWinner();

    boolean isDraw();

    void addScores(GamePlayer player, int score);

    int getPlayerScores(GamePlayer player);

    void update(GameScoreboard scoreboard);

    class Base implements GameScoreboard {

        private final Map<GamePlayer, Integer> gameScores;

        public Base(List<GamePlayer> list) {
            gameScores = new HashMap<>();
            for (GamePlayer player : list) {
                gameScores.put(player, 0);
            }
        }

        public int getPlayerScores(GamePlayer player) {
            if (gameScores.containsKey(player)) return gameScores.get(player);
            return 0;
        }

        @Override
        public void update(GameScoreboard scoreboard) {
            for (GamePlayer player : gameScores.keySet()) {
                int score = gameScores.get(player) + scoreboard.getPlayerScores(player);
                gameScores.put(player,score);
            }
        }

        public void addScores(GamePlayer player, int score) {
            gameScores.put(player, gameScores.get(player) + score);
        }

        public boolean isDraw() {
            boolean draw = false;
            int highScore = getHighScore();
            for (int i : gameScores.values()) {
                if (i == highScore) {
                    if (draw) return true;
                    draw = true;
                }
            }
            return false;
        }

        public GamePlayer getWinner() {
            if (isDraw()) return new GamePlayer(0, "Ничья", 0);
            int highScore = getHighScore();
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

        private int getHighScore() {
            int max = 0;
            for (int i : gameScores.values()) {
                max = Math.max(max, i);
            }
            return max;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (GamePlayer player : gameScores.keySet()) {
                result.append(player.getName()).append(" : ").append(gameScores.get(player)).append("\n");
            }
            return result.toString();
        }
    }
}
