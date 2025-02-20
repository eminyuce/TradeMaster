package com.trade.master.core.model;

import lombok.*;

/**
 * Created by huseyina on 5/16/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CoinDeskPrice {
    private String code;
    private String rate;
    private String description;
    private float rate_float;
}
