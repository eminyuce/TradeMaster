package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Created by huseyina on 4/19/2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class TradeHistoryTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeHistoryTrackId;
    private Integer userId;
    private Long lastTimeStampInSec;

    public TradeHistoryTrack(Integer userId, Long lastTimeStampInSec) {
        this.userId = userId;
        this.lastTimeStampInSec = lastTimeStampInSec;
    }
}
