package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yuce on 4/27/2017.
 */
@Entity
public class CurrencyOrder implements Serializable {
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getCurrenyOrderId() {
        return currenyOrderId;
    }

    public void setCurrenyOrderId(Integer currenyOrderId) {
        this.currenyOrderId = currenyOrderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTotalBtc() {
        return totalBtc;
    }

    public void setTotalBtc(float totalBtc) {
        this.totalBtc = totalBtc;
    }
}
