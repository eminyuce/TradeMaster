package com.trade.master.core.repository;

import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by huseyina on 4/7/2017.
 */

@CacheConfig(cacheNames = "currencyConfigs")
public interface CurrencyConfigRepository extends JpaRepository<CurrencyConfig, Integer> {
    @Cacheable
    @Override
    List<CurrencyConfig> findAll();

    @Cacheable
    List<CurrencyConfig> findByBotUser(BotUser botUser);

    @Cacheable
    CurrencyConfig findByBotUserAndCurrencyPair(BotUser botUser, String currencyPair);

    @Override
    @CacheEvict(allEntries = true)
    CurrencyConfig save(CurrencyConfig var1);

    @Override
    @CacheEvict(allEntries = true)
    void delete(CurrencyConfig var1);
}
