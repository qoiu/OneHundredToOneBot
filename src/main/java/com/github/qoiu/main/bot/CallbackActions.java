package com.github.qoiu.main.bot;

import com.github.qoiu.main.presenter.MainPresenterCallbackInterface;
import org.telegram.telegrambots.meta.api.objects.User;


import static com.github.qoiu.main.StateStatus.*;

public class CallbackActions {
    public CallbackActions(MainPresenterCallbackInterface presenter, BotInterface bot) {
        this.presenter = presenter;
        this.bot = bot;
    }

    private MainPresenterCallbackInterface presenter;
    private PreparedSendMessages messages = new PreparedSendMessages();
    private BotInterface bot;

    public Integer action(User user, String command) {
        String id = String.valueOf(user.getId());
        String[] parsed = command.split(":");
        switch (parsed[0]) {
            case "/start":
                presenter.startGame(user);
                return PLAYER_WAITING_OTHER_PLAYERS_HOST;
            case "/connecting":
                return PLAYER_CHOSE_GAME;
            case "/connect":
                if (parsed.length > 1 && parsed[1] != null) {
                    try {
                        presenter.connectPlayer(Integer.parseInt(parsed[1]),user.getId());
                        return PLAYER_WAITING_OTHER_PLAYERS;
                    } catch (NumberFormatException e) {
                        bot.sendMessage(messages.somethingWrong(id));
                        return PLAYER_BASE_STATUS;
                    }
                }
            case "/startGame":
                presenter.startGameForAll(user);
                return PLAYER_IN_GAME;
        }
        return PLAYER_BASE_STATUS;
    }
}
