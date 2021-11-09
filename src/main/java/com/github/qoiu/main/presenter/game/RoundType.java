package com.github.qoiu.main.presenter.game;

import com.github.qoiu.main.Question;

import java.util.Comparator;

public interface RoundType {
    int setScores(Question question, Question.Answer answer);
    String getName();
    Comparator<Question.Answer> getComparator();

    abstract class Base implements RoundType{
        @Override
        public int setScores(Question question, Question.Answer answer) {
            return question.getPercentageOfAnswer(answer);
        }

        @Override
        public Comparator<Question.Answer> getComparator() {
            return (o1, o2) -> o2.getRate()-o1.getRate();
        }
    }

}

class RoundUsual extends RoundType.Base{
    @Override
    public String getName() {
        return "Обычная игра";
    }
}

class RoundDouble extends RoundType.Base{

    @Override
    public int setScores(Question question, Question.Answer answer) {
        return super.setScores(question,answer)*2;
    }

    @Override
    public String getName() {
        return "Двойная игра";
    }
}

class RoundTriple extends RoundType.Base{

    @Override
    public int setScores(Question question, Question.Answer answer) {
        return super.setScores(question,answer)*3;
    }

    @Override
    public String getName() {
        return "Тройная игра";
    }
}

class RoundReversed implements RoundType{

    @Override
    public int setScores(Question question, Question.Answer answer) {
        return question.getPercentageOfAnswer(question.getAnswers().size() - question.getAnswers().indexOf(answer)-1);
    }

    @Override
    public String getName() {
        return "Игра наоборот";
    }

    @Override
    public Comparator<Question.Answer> getComparator() {
        return Comparator.comparingInt(Question.Answer::getRate);
    }
}
