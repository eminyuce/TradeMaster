package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
/**
 * Created by habanoz on 05.04.2017.
 * <p>
 * Used for collecting volume history for analysis. ıf not enabled history data is not collected.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AnalysisCurrencyConfig {
    @Id
    private String currencyPair;
    private Boolean enabled;
}
