/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author haidu
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Autowired
    public InterceptorConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List patterns= new ArrayList();
        patterns.add("/login");
        patterns.add("/register");
        patterns.add("/register/authentication/{codeEmail}");
        patterns.add("/forgotpassword");
        patterns.add("/resetpassword");
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(patterns)
                .addPathPatterns("/**") // Apply the interceptor to all paths
                ; // Exclude the login path from the interceptor
    }
}
