package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.presenter.QuestionTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;

public class SendMapperQuestionChangeTitle extends SendMapper.Base {
    HashMap<Long, QuestionTemplate> questionTemplate;

    public SendMapperQuestionChangeTitle(HashMap<Long, QuestionTemplate> questionTemplate) {
        super(null);
        this.questionTemplate = questionTemplate;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        String text = "Введите вопрос";
        return base(userMessaged.getId(),text,btn);
    }
}
