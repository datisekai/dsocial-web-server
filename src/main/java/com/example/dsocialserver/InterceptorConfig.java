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
        patterns.add("/user/login");
        patterns.add("/user/register");
        patterns.add("/user/register/authentication/{codeEmail}");
        patterns.add("/user/forgotpassword");
        patterns.add("/user/resetpassword");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // Apply the interceptor to all paths
                .excludePathPatterns(patterns); // Exclude the login path from the interceptor
    }
}
