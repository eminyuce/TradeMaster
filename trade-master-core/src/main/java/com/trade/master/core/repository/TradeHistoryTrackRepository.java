package com.trade.master.core.repository;

import com.trade.master.core.entity.TradeHistoryTrack;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by huseyina on 4/19/2017.
 */

public interface TradeHistoryTrackRepository extends JpaRepository<TradeHistoryTrack, Integer> {
}
