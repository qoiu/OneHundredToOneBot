package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.QuestionDb;
import com.github.qoiu.main.data.UserDb;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;

public class DbMapperGetUnansweredQuestionsByPlayersTest {
    private static DatabaseBase db;

    @BeforeClass
    public static void init(){
        db = new DatabaseBase("jdbc:sqlite:test.db");
    }

    @Test
    public void test(){
        UserDb user = new UserDb(3232,0,"lolka");
        user.setExtra(265);
        new DbMapperAddUserAnswer(db).map(user);
        Set<Integer> result = new DbMapperGetUnansweredQuestionsByPlayers(db).map(Sets.newHashSet(3232L,3443L));

        assertFalse(result.contains(265));
        System.out.println(result);
    }

    @Test
    public void getQuestionTest(){
        QuestionDb question = new DbMapperGetQuestion(db).map(268);
        Assert.assertEquals(question.getText(),"Без чего автомобиль не поедет?");
        Assert.assertEquals(question.getAnswers().size(),6);
    }

}