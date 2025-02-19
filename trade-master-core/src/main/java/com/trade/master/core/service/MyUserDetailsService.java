package com.trade.master.core.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MyUserDetailsService {

    public UserDetails loadUserByUsername(String userName, List<String> groups) throws UsernameNotFoundException {

        List<GrantedAuthority> authoritiesList = new ArrayList<>();

        for (String group : groups) {
            authoritiesList.add(new SimpleGrantedAuthority(group));

        }

        return new User(userName, "", authoritiesList);

    }

}