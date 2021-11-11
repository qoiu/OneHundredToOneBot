package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.presenter.GamePlayer;

public interface GameInfo {

    public GamePlayer getActivePlayer();

    public Statistic getStatistic();

    public Question getCurrentQuestion();
}
