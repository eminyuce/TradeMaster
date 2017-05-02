package com.habanoz.polbot.core.web.controller;

import com.habanoz.polbot.core.api.PoloniexPublicApi;
import com.habanoz.polbot.core.entity.CurrencyConfig;
import com.habanoz.polbot.core.model.PoloniexTicker;
import com.habanoz.polbot.core.repository.CurrencyConfigRepository;
import com.habanoz.polbot.core.service.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huseyina on 4/9/2017.
 */
@Controller
public class IndexController {

    @Autowired
    private PoloniexPublicApi poloniexPublicApi;

    @RequestMapping({"/", "/index"})
    public String welcome(Map<String, Object> model) {
        //int userId = authenticationFacade.GetUserId();  //Authenticated User
        model.put("poloniexTicker", new PoloniexTicker());
        model.put("poloniexTickers", this.poloniexPublicApi.returnTicker());
        model.put("searchKey", "");
        return "index";
    }

}
