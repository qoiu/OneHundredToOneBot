package com.github.qoiu.main.mappers;

import com.github.qoiu.main.bot.BotUser;
import com.github.qoiu.main.data.UserMessaged;
import org.telegram.telegrambots.meta.api.objects.User;

public interface BotUserToUserMessagedMapper {
    UserMessaged map(BotUser user,String message);
    UserMessaged map(User user, String message);

    class Base implements BotUserToUserMessagedMapper{
        @Override
        public UserMessaged map(BotUser user, String message) {
            return new UserMessaged(user.getName(), user.getId(), message);
        }

        @Override
        public UserMessaged map(User user, String message) {
            return map(new BotToBotUserMapper.Base().map(user), message);
        }
    }
}
