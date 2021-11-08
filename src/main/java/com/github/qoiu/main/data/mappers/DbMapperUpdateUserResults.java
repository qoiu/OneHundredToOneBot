package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserDb;

public class DbMapperUpdateUserResults extends DbMapper.Base<Integer, UserDb> {
    public DbMapperUpdateUserResults(DatabaseBase db) {
        super(db);
    }

    @Override
    public Integer map(UserDb user) {
        UserDb dbUser = new DbMapperGetUserById(db).map(user.getId());
        user.setHighScore(Math.max(user.getHighScore(), dbUser.getHighScore()));
        user.setGamePlay(dbUser.getGamePlay() + 1);
        user.setGameWin(dbUser.getGameWin() + user.getGameWin());
        return db.executeUpdate("UPDATE users SET gamesPlayed = ?, gamesWin = ?, highScore = ? WHERE id = ?", user.getGamePlay(), user.getGameWin(), user.getHighScore(), user.getId());
    }
}
