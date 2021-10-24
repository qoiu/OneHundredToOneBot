package com.github.qoiu.main.data.tables;

import javafx.util.Pair;

import java.util.List;

public interface MessageHistoryDatabase {
    List<Pair<Long,Integer>> getDisconnectedMessages();
    void saveMsg(Pair<Long,Integer> pair);
    void deletedMsg(Pair<Long,Integer> pair);
}
