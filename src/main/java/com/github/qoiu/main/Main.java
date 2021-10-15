package com.github.qoiu.main;

import com.github.qoiu.main.bot.PreparedSendMessages;
import com.github.qoiu.main.data.MyDb;
import javafx.application.Application;
import javafx.stage.Stage;
import com.github.qoiu.main.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main extends Application {

    private static MyDb db;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("100 to 1");
        primaryStage.show();
    }



    public static void main(String[] args) {
        Bot bot = new Bot(new OutputReader().read());
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        bot.sendMessage(new PreparedSendMessages().isAlive());
        db = new MyDb();
        db.start();
        new MainPresenter(bot,db);
    }

}
