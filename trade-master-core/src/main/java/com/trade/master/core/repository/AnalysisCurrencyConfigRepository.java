package com.trade.master.core.repository;

import com.trade.master.core.entity.AnalysisCurrencyConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by huseyina on 4/7/2017.
 */

public interface AnalysisCurrencyConfigRepository extends JpaRepository<AnalysisCurrencyConfig, String> {
    List<AnalysisCurrencyConfig> findAll();

    List<AnalysisCurrencyConfig> findByEnabledTrue();

}
