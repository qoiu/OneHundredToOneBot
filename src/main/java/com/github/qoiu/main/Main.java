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

    public static void main(String... args) {
        bot = new Bot(new OutputReader().read());
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        db = new DatabaseBase("jdbc:sqlite:game.db");
        new MainPresenter(bot, db);
        launch(args);
    }

    @Override
    public void stop()  {
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
