package com.trade.master.core.repository;

import com.trade.master.core.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository
        extends JpaRepository<Bot, Integer> {
    Bot findByName(String name);
}
