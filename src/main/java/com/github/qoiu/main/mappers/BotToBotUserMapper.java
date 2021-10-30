package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.BotUser;
import org.telegram.telegrambots.meta.api.objects.User;

public interface BotToBotUserMapper extends Mapper<User, BotUser> {

    class Base implements BotToBotUserMapper{
        @Override
        public BotUser map(User data) {
            String name = data.getFirstName();
            if(data.getLastName() != null) name += " " + data.getLastName();
            return new BotUser(name,data.getId());
        }
    }
}
