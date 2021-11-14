package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.Question;
import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.presenter.QuestionTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;

import static com.github.qoiu.main.StateStatus.*;

public class SendMapperEditQuestion extends SendMapper.Base {
    HashMap<Long, QuestionTemplate> questionTemplate;

    public SendMapperEditQuestion(HashMap<Long, QuestionTemplate> questionTemplate) {
        super(null);
        this.questionTemplate = questionTemplate;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        QuestionTemplate template= questionTemplate.get(userMessaged.getId());
        String[] msg = userMessaged.getMessage().split(":");
        if(msg[0].equals(CMD_ANSWER))
        try {
            int answerId = Integer.parseInt(msg[1]);
            if(answerId<template.getAnswers().size())
            template.removeAnswer(template.getAnswers().get(answerId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String text = "Здесь вы можете изменить данный вопрос и добавлять любое количество ответов. Когда Ваш вопрос одобрят, он сможет появится у других игроков. Введите новый ответ";
        TelegramBtn btn = new TelegramBtn();

        btn.addColumn("✒️"+template.getText(), CMD_EDIT_QUESTION);
        for (int i = 0; i < template.getAnswers().size(); i++) {
            Question.Answer answer = template.getAnswers().get(i);
            btn.addColumn("❌"+answer.getText(), CMD_ANSWER+":"+i);
        }
        btn.addColumn("Меню(вопрос не сохраниться)", CMD_MENU);
        if(template.getAnswers().size()>=6)
            btn.addColumn("Сохранить вопрос",CMD_SAVE_QUESTION);
        return base(userMessaged.getId(),text,btn);
    }
}
