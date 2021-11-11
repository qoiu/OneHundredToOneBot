package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.presenter.GamePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Statistic {
    int getAmount(GamePlayer player);
    void decreaseTry(GamePlayer activePlayer);
    boolean checkTryAmount();

    class Base implements Statistic{
        private Map<GamePlayer, Integer> tryAmount;
        private final int MAX_TRY = 3;


        public Base(List<GamePlayer> players) {
            this.tryAmount = new HashMap<>();
            for (GamePlayer player : players) {
                tryAmount.put(player, MAX_TRY);
            }
        }

        public int getAmount(GamePlayer player) {
            if (tryAmount.containsKey(player)) return tryAmount.get(player);
            return 0;
        }


        public boolean checkTryAmount() {
            for (GamePlayer player : tryAmount.keySet()) {
                if (tryAmount.get(player) != 0) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();
            for (GamePlayer player : tryAmount.keySet()) {
                output.append(player.getName()).append("\n");
                for (int i = 0; i < MAX_TRY; i++) {
                    output.append((tryAmount.get(player) > i) ? "✔" : "❌");
                }
                output.append("\n");
            }
            return output.toString();
        }

        public void decreaseTry(GamePlayer activePlayer) {
            tryAmount.put(activePlayer, tryAmount.get(activePlayer) - 1);
        }
    }
}
