package com.trade.master.core.repository;

import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.UserBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBotRepository
        extends JpaRepository<UserBot, Integer> {

    @Query("select p from UserBot p where p.user.active=true and p.bot.name=?1 and p.active=true")
    List<UserBot> findByBotQuery(String botName);

    List<UserBot> findByUser(BotUser botUser);
}
