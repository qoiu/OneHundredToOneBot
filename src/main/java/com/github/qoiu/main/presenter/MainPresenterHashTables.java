package com.github.qoiu.main.presenter;

import com.github.qoiu.main.presenter.game.GameEngine;
import com.github.qoiu.main.presenter.mappers.SendMapper;

public interface MainPresenterHashTables{
    interface GameEngineTable{
        GameEngine getGame(long playerId);
        boolean isGameExist(long playerId);
        void setGameEngine(long playerId, GameEngine engine);
    }

    interface SendMessageTable{
        SendMapper getSendMsg(int playerStatus);
        boolean isMsgExist(int status);
    }

    interface QuestionTemplateTable{
        QuestionTemplate getEditedQuestion(long playerId);
        void setQuestionTemplate(long playerId, QuestionTemplate question);
    }

    interface CurrentQuestion{
        int getCurrentQuestion(long playerId);
        void setCurrentQuestion(long playerId, int value);
    }

    interface Global extends GameEngineTable,SendMessageTable,QuestionTemplateTable,CurrentQuestion{
    }
}
