package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.Settings;
import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.mappers.DbMapperGetQuestion;
import com.github.qoiu.main.data.mappers.DbMapperGetUnansweredQuestionsByPlayers;
import com.github.qoiu.main.mappers.QuestionDbToQuestionMapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

public class GamePresenter {
    private final List<GamePlayer> list;
    private DatabaseBase db;
    private int gameStatus;
    private Round current;
    private MessageSender sender;
    private PreparedSendMessages messages = new PreparedSendMessages();

    public GamePresenter(DatabaseBase db, List<GamePlayer> list, MessageSender sender) {
        this.db = db;
        this.sender = sender;
        this.list = list;
        current = new Round();
    }

    class Round {
        GamePlayer activePlayer;
        Set<GamePlayer> playersSet = new HashSet<>();
        private Question currentQuestion;
        private int timer = Settings.BASE_TIMER;
        private boolean activePlayerAnswered=false;

        public Round() {
            playersSet.addAll(list);
            int rand = new Random().nextInt(playersSet.size());
            activePlayer = list.get(rand);
            playersSet.remove(activePlayer);

            Set<Long> ids = new HashSet<>();
            for (GamePlayer player : list) {
                ids.add(player.getId());
            }

            List<Integer> qIds = new ArrayList<>(new DbMapperGetUnansweredQuestionsByPlayers(db).map(ids));
            int currentQuestionId = qIds.get(new Random().nextInt(qIds.size()));

            currentQuestion = new QuestionDbToQuestionMapper().map(
                    new DbMapperGetQuestion(db).map(currentQuestionId));
            notifyPlayers();
        }

        private SendMessage msg;
        private void notifyPlayers() {
            for (GamePlayer player : list) {
                msg = messages.playerAnswer(
                        (player != activePlayer) ? activePlayer.getName() + " отвечает на вопрос:" : "Вы отвечаете на вопрос:",
                        player.getId(),
                        currentQuestion,
                        timer
                );
                sender.sendMessage(msg);
            }
        }

    }
}
