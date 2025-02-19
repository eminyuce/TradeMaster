package com.trade.master.core.web.controller;

import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.BotUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by Yuce on 4/11/2017.
 */
@Controller
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(PoloniexTrade.class);

    @Autowired
    private BotUserRepository botUserRepository;

    @RequestMapping(value = "/botusers/allusers")
    public String welcome(Map<String, Object> model) {

        model.put("botUsers", this.botUserRepository.findAll());

        return "allusers";
    }


}
