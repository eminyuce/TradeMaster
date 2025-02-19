package com.trade.master.core.repository;

import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yuce on 4/9/2017.
 */
@CacheConfig(cacheNames = "botUsers")
@Repository
public interface BotUserRepository
        extends JpaRepository<BotUser, Integer> {

    @Cacheable
    @Override
    List<BotUser> findAll();

    @Cacheable
    List<BotUser> findByActive(boolean isActive);

    @CacheEvict(allEntries = true)
    @Override
    BotUser save(BotUser botUser);

    @CacheEvict(allEntries = true)
    @Override
    void delete(BotUser botUser);

    @Cacheable
    BotUser findByUserAndBuId(User user, Integer buId);
}
