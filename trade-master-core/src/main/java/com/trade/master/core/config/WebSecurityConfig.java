package com.trade.master.core.config;

import com.trade.master.core.api.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

    @Autowired
    private LoginSucessHandler loginSucessHandler;


    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/allusers", "/collectiveorder/*", "/currencyconfig/*", "/edituserinfo",
                                "/editbotinfo", "/editbotuserinfo/*", "/mybalances/*", "/mycurrencies/*",
                                "/orders/*")
                        .hasRole("BOT")
                        .requestMatchers("/tradehistory/*", "/analyse")
                        .hasRole("ANALYSIS")
                        .requestMatchers("/", "/home", "/DataTables/**", "/css/**", "/js/**", "/images/**", "/webjars/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .successHandler(loginSucessHandler)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // return (web) -> web.ignoring().requestMatchers("/**");
        return (web) -> web.ignoring().requestMatchers("/v2/api-docs", "/v3/api-docs/**", "/swagger-ui.html",
                "/configuration/ui", "/swagger-resources/**", "/swagger-ui/**", "/configuration/**", "/webjars/**",
                "/actuator/**", "/favicon.ico");
    }

}