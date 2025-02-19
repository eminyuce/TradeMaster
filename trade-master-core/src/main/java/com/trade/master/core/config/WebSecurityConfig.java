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
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/airportIds/exists/fedexAirportId/*", "/svcCodes/exists/fedexSvcCd/*",
                                "/tankeringFuelPrices/criteriaSearch*", "/tankeringFuelPrices/search*")
                        .permitAll() // Add anything here that has to bypass authentication.
                        // .requestMatchers("/aircraftDesc/**").hasAuthority(adminGroup)
                        .requestMatchers("/aircraftDesc/**").permitAll().requestMatchers("/airport/**").permitAll()
                        .requestMatchers("/countries/**").permitAll().requestMatchers("/states/**").permitAll()
                        .requestMatchers("/tmdStatusList/**").permitAll()
                        .requestMatchers("/documentType/**").permitAll()
                        .requestMatchers("/tmdFx/**").permitAll()
                        .requestMatchers("/tmdProduct/**").permitAll()
                        .requestMatchers("/tmdVacCodes/**").permitAll()
                        .requestMatchers("/tmdICOBusinessUnit/**").permitAll()
                        .requestMatchers("/tmdICOCVRulesLECC/**").permitAll()
                        .requestMatchers("/tmdICOCVRulesACTMapping/**").permitAll()
                        .requestMatchers("/analysisReport/**").permitAll()
                        .requestMatchers("/txDGLQuarantineInvoices/**").permitAll()
                        .requestMatchers("/txDP3QuarantineInvoice/**").permitAll()
                        .requestMatchers("/tmdSettleStatusList/**").permitAll()
                        .requestMatchers("/settleInvoiceHeader/**").permitAll()
                        .requestMatchers("/tmdVat/**").permitAll()
                        .requestMatchers("/tmdSettlementTypeList/**")

                        .permitAll().requestMatchers("/tmdUserEntity/**").permitAll().requestMatchers("/tmdEntity/**")
                        .permitAll().requestMatchers("/tmdUsersPermissions/**").permitAll()
                        .requestMatchers("/tmdCalendar/**").permitAll()
                        .requestMatchers("/browseTransaction/**").permitAll()
                        .requestMatchers("/tmdICOAccount/**").permitAll()
                        .requestMatchers("/tmdICOCostCenter/**").permitAll()
                        .requestMatchers("/tmdICOProject/**").permitAll()
                        .requestMatchers("/tmdICORegionAccountAssignmentlist/**").permitAll()
                        .requestMatchers("/eboReport/**").permitAll()
                        .requestMatchers("/txDP3ImportedTransactions/**").permitAll()
                        .requestMatchers("/creditRequest/**").permitAll()


                        .anyRequest().authenticated())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        // .exceptionHandling((exception)-> exception
        // .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        // )

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