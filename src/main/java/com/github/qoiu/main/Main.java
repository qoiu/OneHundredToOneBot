package com.github.qoiu.main;

import com.github.qoiu.main.bot.Bot;
import com.github.qoiu.main.data.tables.*;
import com.github.qoiu.main.presenter.MainPresenter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("100 to 1");
        primaryStage.setWidth(200);
        primaryStage.setHeight(200);
        primaryStage.show();
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                new IllegalStateException("Closing App");
            }
        });
    }

    private static Bot bot;

    public static void main(String[] args) {
        bot = new Bot(new OutputReader().read());
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        DatabaseBase.start();
        DbGame dbGame = new DbGame();
        DbUsers dbUser = new DbUsers();
        DbQuestions dbQuestions = new DbQuestions();
        addQuestions(dbQuestions);
        DbMessageHistory dbHistory = new DbMessageHistory();
        new MainPresenter(bot, dbGame, dbUser, dbHistory, dbQuestions);
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        bot.onClosing();
        super.stop();
        System.exit(0);
    }

    private static void addQuestions(DbQuestions dbQuestions){
        List<Question> questions = new InputVocReader("100 к 1 ответы на вопрос:", "end").read();
        for (Question question:questions) {
            dbQuestions.addQuestion(question);
        }
        System.out.println("questions added");

    }


}
