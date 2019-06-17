package com.example.SilkWay.handler;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);

        boolean admin = false;
        boolean user = false;
        boolean company = false;
        boolean superAdmin = false;
        logger.info("AT onAuthenticationSuccess(...) function!");


        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("COMPANY".equals(auth.getAuthority())){
                company = true;
            }
            else if ("USER".equals(auth.getAuthority())){
                user = true;
            }
            else if ("ADMIN".equals(auth.getAuthority())){
                admin = true;
            }
            else if ("SUPER_ADMIN".equals(auth.getAuthority())){
                superAdmin = true;
            }

        }

        if(company){
            response.sendRedirect("/homeCompany");
        }
        else if (user){
            response.sendRedirect("/home");
        }

        else if(admin){
            response.sendRedirect("/homeAdmin");
        }

        else if(superAdmin){
            response.sendRedirect("superAdmin");
        }
        else{
            response.sendRedirect("/");
        }
        /*if ("USER".equals(auth.getAuthority())){
                user = true;
            }
        if(user){
            response.sendRedirect("/user/home");
        }else{
            response.sendRedirect("/");
        }-->*/

    }
}