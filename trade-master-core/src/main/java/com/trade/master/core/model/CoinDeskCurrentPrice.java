package com.trade.master.core.model;

import lombok.*;

import java.util.Map;

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
public class CoinDeskCurrentPrice {
    private Map<String, String> time;
    private String disclaimer;
    private Map<String, CoinDeskPrice> bpi;

}
