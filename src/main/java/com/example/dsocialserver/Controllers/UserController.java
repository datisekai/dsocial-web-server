/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
/**
 *
 * @author haidu
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JavaMailSender mailSender;

    private static final String SECRET_KEY = "haiduong";
    
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
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String indexRegister(HttpSession session) {
        if (session.getAttribute("email") != null) {
            return "redirect:/";
        }
        return "pages/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(HttpServletRequest request, HttpSession session, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        
        String contextPath = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
        
//        ----------------------------------

        User isExist = userService.findByEmail(email);
        if (isExist != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email đã được sử dụng");
        }
        
//        ----------------------------------

        String avatar = "https://ui-avatars.com/api/?name=" + name;
        String hashPassword = "";
        try {
            MessageDigest msd = MessageDigest.getInstance("MD5");
            byte[] srcTextBytes = password.getBytes("UTF-8");
            byte[] enrTextBytes = msd.digest(srcTextBytes);

            BigInteger bigInt = new BigInteger(1, enrTextBytes);
            hashPassword = bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi máy chủ nội bộ");
        }
        User user = userService.createUser(email, hashPassword, name, avatar);
        
//        ----------------------------------
        
        String codeEmail=createJWT(user.getId()+"");
        Date currentDate = new Date();
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("haiduong09876@gmail.com");
        message.setTo(email);
        message.setText("Nhấn vào link để xác thực email: " + basePath +"register/authentication/" + codeEmail 
                +"\nCảm ơn bạn đã tham gia website mạng xã hội Dsocial.");
        message.setSubject("["+currentDate+"] Dsocial | Website mạng xã hội");
        mailSender.send(message);
        
        return ResponseEntity.status(HttpStatus.OK).body("Xác thực email đã gửi về email của bạn");
    } 
    
    @RequestMapping(value = "/register/authentication/{codeEmail}", method = RequestMethod.GET)
    public ResponseEntity indexAuthentication(HttpSession session, @PathVariable("codeEmail") String codeEmail) {
        if (session.getAttribute("email") != null) {
            return new ResponseEntity<>("redirect:/", HttpStatus.FOUND);
        }
        Claims id = decodeJWT(codeEmail);
        userService.updateUser(id.getSubject());

        return ResponseEntity.status(HttpStatus.OK).body("Xác thực email thành công");
    }
    
    
}
