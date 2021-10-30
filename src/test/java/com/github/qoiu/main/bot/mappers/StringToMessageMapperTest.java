package com.github.qoiu.main.bot.mappers;

import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.Assert.*;

public class StringToMessageMapperTest {

    @Test
    public void test(){
        String expected = "some msg";
        SendMessage actual = new StringToMessageMapper("121").map(expected);
        assertEquals(actual.getText(),expected);
    }
}