/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver;

import static com.example.dsocialserver.Utils.JwtTokenProvider.isTokenExpired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author haidu
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Check if the user is logged in
        boolean isLoggedIn = true;// Your login check logic here
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7); // Loại bỏ phần "Bearer "
            // Bạn có thể sử dụng giá trị của bearerToken ở đây
            if (isTokenExpired(bearerToken) == 0) {
                isLoggedIn = false;
            }
        }

        if (!isLoggedIn) {
            // User is not logged in, redirect to login page or return appropriate response
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            response.sendRedirect("/login");
            return false; // Stop further processing
        }

        return true; // User is logged in, continue processing the request
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // No action needed
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // No action needed
    }
}
