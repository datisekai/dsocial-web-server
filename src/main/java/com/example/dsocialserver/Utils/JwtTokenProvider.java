/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Utils;

import static com.example.dsocialserver.Utils.StatusUntilIndex.showNotAuthorized;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author haidu
 */
@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "haiduong"; // Thay thế bằng secret key thực của bạn
//    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(long subject, long EXPIRATION_TIME) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(subject+"")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public static String createJWT(long subject) {
        return Jwts.builder()
                .setSubject(subject+"")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }
    
    public static int isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            if(claims.getExpiration().before(new Date())== false){
                return Integer.parseInt(decodeJWT(token).getSubject());
            }
            return 0;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            return 0; // Nếu có lỗi khi parse token, coi như token đã hết hạn
        }
    }
    public static Claims getIDByBearer(String authorizationHeader){
         String bearerToken = authorizationHeader.substring(7);
         return decodeJWT(bearerToken);
    }
}
