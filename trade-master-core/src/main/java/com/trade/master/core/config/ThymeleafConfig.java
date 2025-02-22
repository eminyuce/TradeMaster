package com.trade.master.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class ThymeleafConfig {

    @Bean(name = "customTemplateEngine")
    public TemplateEngine templateEngine() {
        return new SpringTemplateEngine();
    }
}