package com.trade.master.core.web.controller;

import com.trade.master.core.model.AnalysisConfig;
import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.BotRepository;
import com.trade.master.core.robot.PolBot;
import com.trade.master.core.robot.PoloniexPatienceStrategyBot;
import com.trade.master.core.service.ProfitAnalysisService;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huseyina on 5/29/2017.
 */
@Controller
public class ProfitabilityAnalysisController {
    private static final Logger logger = LoggerFactory.getLogger(ProfitabilityAnalysisController.class);

    @Autowired
    private ProfitAnalysisService profitAnalysisService;
    @Autowired
    private BotRepository botRepository;


    @RequestMapping(value = "/analyse", params = {"analyse"})
    public String analyse(AnalysisConfig analysisConfig, Map model) {

        Map<String, Object> resultMap = profitAnalysisService.execute(analysisConfig, 1.0f);
        List<PoloniexTrade> historyList = (List<PoloniexTrade>) resultMap.get("history");
        List<PoloniexTrade> buys = new ArrayList<>();
        List<PoloniexTrade> sells = new ArrayList<>();
        for (PoloniexTrade poloniexTrade : historyList)
            if (poloniexTrade.getType().equalsIgnoreCase(PolBot.BUY_ACTION))
                buys.add(poloniexTrade);
            else sells.add(poloniexTrade);

        StrBuilder sbBuys = new StrBuilder("[");
        for (PoloniexTrade trade : buys)
            sbBuys.append("{date:").append(trade.getDate().getTime()).append(",value:").append(trade.getRate().doubleValue()).append("},");
        sbBuys.append("]");

        StrBuilder sbSels = new StrBuilder("[");
        for (PoloniexTrade trade : sells)
            sbSels.append("{date:").append(trade.getDate().getTime()).append(",value:").append(trade.getRate().doubleValue()).append("},");
        sbSels.append("]");

        model.put("result", resultMap);
        model.put("buys", sbBuys.toString());
        model.put("sells", sbSels.toString());
        model.put("bots", botRepository.findAll());
        model.put("analysisConfig", analysisConfig);

        return "profitanalysis";
    }

    @RequestMapping(value = "/analyse")
    public String analyse(Map model) {
        AnalysisConfig analysisConfig = new AnalysisConfig("BTC_DGB", 0, 10, 0, 10, 30, 300L, PoloniexPatienceStrategyBot.class.getSimpleName());
        model.put("analysisConfig", analysisConfig);
        model.put("bots", botRepository.findAll());

        return "profitanalysis";
    }
}
