package com.trade.master.core.entity;

import jakarta.persistence.Entity;
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
    private Integer userId;
    private Long lastTimeStampInSec;
}
