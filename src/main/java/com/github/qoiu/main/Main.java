package com.github.qoiu.main;

import com.github.qoiu.main.bot.Bot;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.mappers.DbMapperAddQuestion;
import com.github.qoiu.main.mappers.QuestionsToQuestionDbMapper;
import com.github.qoiu.main.presenter.MainPresenter;
import javafx.application.Application;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("100 to 1");
        primaryStage.setWidth(200);
        primaryStage.setHeight(200);
        new JavaFXTrayIconSample(
                new JavaFXTrayIconSample.TrayBtn("AddQuestions", e -> addQuestions()),
                new JavaFXTrayIconSample.TrayBtn("Exit", e -> stop())
        ).start(primaryStage);
    }

    private static Bot bot;
    private static DatabaseBase db;
    private static TelegramBotsApi api;
    private static BotSession session;
    private static MainPresenter presenter;

    public static void main(String... args) {
        try {
            api = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        db = new DatabaseBase("jdbc:sqlite:game.db");
        presenter = new MainPresenter(db);
        startBot();
        new Thread(Main::reconnected).start();
        launch(args);
    }

    private static void startBot() {
        try {
            bot = new Bot(new OutputReader().read());
            if (session != null && session.isRunning()) session.stop();
            session = api.registerBot(bot);
            presenter.setBot(bot);
            System.out.println("Bot: started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            bot=null;
        }
    }

    private static void reconnected() {
        while (true) {
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bot == null) {
                startBot();
                System.out.println("Bot: trying to reconnect");
            } else {
                try {
                    bot.getMe();
                } catch (NullPointerException ignored) {
                } catch (TelegramApiException e) {
                    bot=null;
                    presenter.lostConnection();
                }
            }
        }
    }

    @Override
    public void stop() {
        bot.clearChat();
        bot.onClosing();
        try {
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void addQuestions() {
        List<Question> questions = new InputVocReader("100 к 1 ответы на вопрос:", "end").read();
        for (Question question : questions) {
            new DbMapperAddQuestion(db).map(new QuestionsToQuestionDbMapper().map(question));
        }
        System.out.println("questions added");

    }
}
