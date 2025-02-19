package com.trade.master.core.web.controller;

import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.repository.BotUserRepository;
import com.trade.master.core.repository.CurrencyConfigRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Yuce on 4/11/2017.
 */
@Controller

public class AjaxController {

    @Autowired
    private BotUserRepository botUserRepository;

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;


    @RequestMapping(value = "/GetAjaxUsers", method = RequestMethod.GET)
    @ResponseBody
    public String GetUserCurrencies(@RequestParam("botUserId") int botUserId) throws IOException {
        BotUser botUser = botUserRepository.getReferenceById(botUserId);

        // int userId=1;
        List<CurrencyConfig> userCurrencyConfigs = currencyConfigRepository.findByBotUser(botUser);
        List<CurrencyConfig> currencyConfigs = userCurrencyConfigs.stream().filter(r -> r.getBuyable() || r.getSellable()).collect(Collectors.toList());

        String currencyConfigsJson = new ObjectMapper().writeValueAsString(currencyConfigs);
        Map<String, String> payload = new HashMap<>();
        payload.put("userCurrencies", currencyConfigsJson);

        return new ObjectMapper().writeValueAsString(payload);
    }

}
