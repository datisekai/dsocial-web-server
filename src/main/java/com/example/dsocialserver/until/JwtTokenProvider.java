/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.until;

import static com.example.dsocialserver.until.StatusUntilIndex.showNotAuthorized;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author haidu
 */
@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "haiduong"; // Thay thế bằng secret key thực của bạn
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public static String createJWT(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }
    
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Nếu có lỗi khi parse token, coi như token đã hết hạn
        }
    }
    public static ResponseEntity isLogin(String token) {
        if (token!=null) {
            if(isTokenExpired(token)){
                return showNotAuthorized();
            }else{
                return ResponseEntity.ok("isLogin"); 
            }
        }
        return showNotAuthorized();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Handle invalid or expired JWT tokens
            return false;
        }
    }
}
