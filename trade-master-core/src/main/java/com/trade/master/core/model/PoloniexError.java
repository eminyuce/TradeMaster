package com.trade.master.core.model;

import lombok.*;

/**
 * Created by huseyina on 4/7/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexError {
    private String error;
}
