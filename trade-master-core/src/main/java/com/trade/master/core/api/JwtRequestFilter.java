package com.trade.master.core.api;

import com.trade.master.core.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private MyUserDetailsService userDetailsService;

    final Base64.Decoder decoder = Base64.getUrlDecoder();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chin)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        // LOGGER.info("Authorization Header #:"+authorizationHeader);

        String userName = null;
        ArrayList<String> groupsList = new ArrayList<>();

        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            String[] chunks = token.split("\\.");
            String payload = new String(decoder.decode(chunks[1]));

            try {
                JSONObject jsonObj = new JSONObject(payload);
                // userName= jsonObj.get("firstName").toString()+"
                // "+jsonObj.get("lastName").toString();
                userName = jsonObj.get("employeeNumber").toString();
                Object obj = jsonObj.get("groups");
                JSONArray jArray = jsonObj.getJSONArray("groups");

                // JSONArray jArray = (JSONArray)jsonObject;
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        groupsList.add(jArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        // LOGGER.info("In Filter Before set #:
        // "+SecurityContextHolder.getContext().getAuthentication());
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName, groupsList);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            // HttpSession session = request.getSession(true);

            final SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(usernamePasswordAuthenticationToken);
            SecurityContextHolder.setContext(context);

            // session.setAttribute(HttpSessionSecurityContextRepository.
            // SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            // SecurityContextHolderStrategy securityContextHolderStrategy
            // =SecurityContextHolder .getContextHolderStrategy();
            // SecurityContext contextStratergy =
            // securityContextHolderStrategy.createEmptyContext();
            // contextStratergy.setAuthentication(usernamePasswordAuthenticationToken);
            // securityContextHolderStrategy.setContext(contextStratergy);

            // securityContextRepository.saveContext(contextStratergy, request, response);

            // securityHttpContextRepository.saveContext(context, request, response);

            // LOGGER.info("In Filter After Set #:
            // "+SecurityContextHolder.getContext().getAuthentication());
        }
        chin.doFilter(request, response);

    }
}
