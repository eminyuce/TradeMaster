package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Created by huseyina on 4/19/2017.
 */
@Entity
public class TradeHistoryTrack {
    private Integer userId;
    private Long lastTimeStampInSec;

    public TradeHistoryTrack(Integer userId, Long lastTimeStamp) {
        this.userId = userId;
        this.lastTimeStampInSec = lastTimeStamp;
    }

    public TradeHistoryTrack() {
    }

    @Id
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getLastTimeStampInSec() {
        return lastTimeStampInSec;
    }

    public void setLastTimeStampInSec(Long lastTimeStampInSec) {
        this.lastTimeStampInSec = lastTimeStampInSec;
    }
}
