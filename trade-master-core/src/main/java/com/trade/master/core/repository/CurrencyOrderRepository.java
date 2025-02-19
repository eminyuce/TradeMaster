package com.trade.master.core.repository;

import com.trade.master.core.entity.CurrencyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Yuce on 4/27/2017.
 */
public interface CurrencyOrderRepository extends JpaRepository<CurrencyOrder, Integer> {
    List<CurrencyOrder> findByUserIdAndActive(Integer userId, boolean isActive);

    CurrencyOrder findByUserIdAndOrderNumber(Integer userId, String orderNumber);

    CurrencyOrder findByUserIdAndOrderNumberAndActive(Integer userId, String orderNumber, boolean isActive);

    //List<CurrencyOrder> findByUserIdAndActiveAndOrderDateGreaterThanOrderDateByOrderDateAsc(Integer userId, boolean isActive, Date orderDate);

}
