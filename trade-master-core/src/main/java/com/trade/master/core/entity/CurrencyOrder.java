package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yuce on 4/27/2017.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer currenyOrderId;

    private String orderNumber;
    private String currencyPair;
    private String orderType;
    private Integer userId;
    private Date orderDate;
    private boolean active;
    private float price;
    private float amount;
    private float totalBtc;
}