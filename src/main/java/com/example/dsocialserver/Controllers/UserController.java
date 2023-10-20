/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.UserService;
import static com.example.dsocialserver.until.JwtTokenProvider.createJWT;
import static com.example.dsocialserver.until.JwtTokenProvider.decodeJWT;
import static com.example.dsocialserver.until.JwtTokenProvider.generateToken;
import static com.example.dsocialserver.until.JwtTokenProvider.isTokenExpired;
import static com.example.dsocialserver.until.MD5.MD5;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import java.util.Date;
import com.example.dsocialserver.until.StatusUntilIndex;
import static com.example.dsocialserver.until.StatusUntilIndex.showNotAuthorized;
import static com.example.dsocialserver.until.Validator.isValidEmail;
import static com.example.dsocialserver.until.Validator.isValidPassword;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.MailException;

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

    @Autowired
    private Environment environment;

    private final CustomResponse jsonRes = new CustomResponse();

    @RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public ResponseEntity indexme(HttpServletRequest request, HttpSession session) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7); // Loại bỏ phần "Bearer "
            // Bạn có thể sử dụng giá trị của bearerToken ở đây
            if (isTokenExpired(bearerToken) == 0) {
                return showNotAuthorized();
            }
            User user = userService.findById(isTokenExpired(bearerToken));
            Map<String, Object> data = new HashMap<>();
            data.put("email", user.getEmail());
            data.put("name", user.getName());
            data.put("avatar", user.getAvatar());
            data.put("bio", user.getBio());
            data.put("birthday", user.getBirthday());
            data.put("coverimage", user.getCoverimage());
            data.put("othername", user.getOthername());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Authorization");
            responseData.put("data", data);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } else {
            // Trường hợp không tìm thấy hoặc không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(HttpServletRequest request, HttpSession session //            , @RequestHeader("Authorization") String authorization
            ,
             Model model) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            if (email == null || password == null || name == null || email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            if (!isValidEmail(email)) {
                jsonRes.setRes(false, "Sai định dạng email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            if (!isValidPassword(password)) {
                jsonRes.setRes(false, "Password phải trên 5 ký tự");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            String contextPath = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
//        ----------------------------------
            User isExist = userService.findByEmail(email);
            if (isExist != null) {
                jsonRes.setRes(false, "Email đã được sử dụng");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }

//        ----------------------------------
            String avatar = "https://ui-avatars.com/api/?name=" + name;
            String hashPassword = MD5(password);
            User user = userService.createUser(email, hashPassword, name, avatar);

//        ----------------------------------
            if (!"".equals(user.getEmail())) {
                String codeEmail = createJWT(user.getId());
                Date currentDate = new Date();
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("haiduong09876@gmail.com");
                message.setTo(email);
                message.setText("Nhấn vào link để xác thực email: " + basePath + "register/authentication/" + codeEmail
                        + "\nCảm ơn bạn đã tham gia website mạng xã hội Dsocial.");
                message.setSubject("[" + currentDate + "] Dsocial | Website mạng xã hội");
                mailSender.send(message);
                jsonRes.setRes(true, "Xác thực email đã gửi về email của bạn");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/register/authentication/{codeEmail}", method = RequestMethod.GET)
    public ResponseEntity indexAuthentication(HttpSession session, @PathVariable("codeEmail") String codeEmail, HttpServletRequest request) {
        try {
            if (codeEmail == null) {
                return StatusUntilIndex.showMissing();
            }
            Claims id = decodeJWT(codeEmail);
            User user = userService.findById(id.getSubject());
            if (user.getIsactive() != 0) {
                jsonRes.setRes(false, "Email đã được xác thực trước đó");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            user = userService.updateisActiveUser(id.getSubject());
            if (user != null) {
                jsonRes.setRes(true, "Đăng ký thành công");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(HttpSession session,
            HttpServletRequest request, Model model) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            if (!isValidEmail(email)) {
                jsonRes.setRes(false, "Sai định dạng email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            if (!isValidPassword(password)) {
                jsonRes.setRes(false, "Password phải trên 5 ký tự");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            String token = null;
//        ----------------------------------
            String hashPassword = MD5(password);
            User user = userService.findByEmailAndPassword(email, hashPassword);
            if (user == null) {
                jsonRes.setRes(false, "Sai tài khoản hoặc mật khẩu");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            } else {
                if (user.getIsactive() == 0) {
                    jsonRes.setRes(false, "Tài khoản chưa được xác thực");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
                } else {
                    token = generateToken(user.getId(), 60000); //86400000 300000
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Đăng nhập thành công");
                    responseData.put("token", token);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
    public ResponseEntity forgotpassword1(HttpSession session,
            HttpServletRequest request, Model model) {
        try {
            String email = request.getParameter("email");
            if (email == null || email.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            User user = userService.findByEmail(email);
            if (user == null) {
                jsonRes.setRes(false, "Email không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            String codeEmail = generateToken(user.getId(), 300000);
            Date currentDate = new Date();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("haiduong09876@gmail.com");
            message.setTo(email);
            message.setText("Nhấn vào link để đổi mật khẩu: " + environment.getProperty("fe.url") + "/resetpassword?token=" + codeEmail
                    + "\nGặp vấn đề liên hệ với Dsocial.");
            message.setSubject("[" + currentDate + "] Dsocial | Website mạng xã hội");
            mailSender.send(message);
            jsonRes.setRes(true, "Link đổi mật khẩu đã được gửi qua email có hiệu lực trong 5 phút");
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
    public ResponseEntity forgotpassword2(HttpSession session,
            HttpServletRequest request, Model model) {
        try {
            String password = request.getParameter("password");
            String token = request.getParameter("token");
            if (password == null || token == null || password.isEmpty() || token.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            Claims id = decodeJWT(token);
            String hashPassword = MD5(password);
            User user = userService.updatePasswordUser(id.getSubject(), hashPassword);
            if (user == null) {
                jsonRes.setRes(false, "Thời gian đổi mật khẩu đã hết");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            jsonRes.setRes(true, "Đổi mật khẩu thành công");
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
