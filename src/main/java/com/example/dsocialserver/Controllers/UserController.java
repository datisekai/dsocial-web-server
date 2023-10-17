/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.UserService;
import com.example.dsocialserver.until.JwtTokenProvider;
import static com.example.dsocialserver.until.JwtTokenProvider.createJWT;
import static com.example.dsocialserver.until.JwtTokenProvider.decodeJWT;
import static com.example.dsocialserver.until.JwtTokenProvider.generateToken;
import static com.example.dsocialserver.until.JwtTokenProvider.isLogin;
import static com.example.dsocialserver.until.MD5.MD5;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import static com.example.dsocialserver.until.ParseJSon.ParseJSonCustomResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import com.example.dsocialserver.until.StatusUntilIndex;
import java.util.HashMap;
import java.util.Map;
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
    private JwtTokenProvider jwtTokenProvider;
    private final CustomResponse jsonRes = new CustomResponse();
    private String codeForgotPassword= "";
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String indexRegister(HttpServletRequest request, HttpSession session
    //            , @RequestHeader("Authorization") String authorization
    ) {
//        String token = session.getAttribute("authorization").toString();
//        System.out.println(""+isLogin(token));
        return "pages/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(HttpServletRequest request, HttpSession session //            , @RequestHeader("Authorization") String authorization
            ,
             @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        try {
            String contextPath = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
            String token = null;
//        ----------------------------------
            User isExist = userService.findByEmail(email);
            if (isExist != null) {
                jsonRes.setRes(false, "Email đã được sử dụng");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSonCustomResponse(jsonRes));
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
            } else {
                return StatusUntilIndex.showMissing();
            }
            jsonRes.setRes(true, "Xác thực email đã gửi về email của bạn");
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSonCustomResponse(jsonRes));

        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/register/authentication/{codeEmail}", method = RequestMethod.GET)
    public ResponseEntity indexAuthentication(HttpSession session,
            @PathVariable("codeEmail") String codeEmail) {
        try {
            String token = null;
            Claims id = decodeJWT(codeEmail);
            User user = userService.findById(id.getSubject());
            if (user.getIsactive() != 0) {
                jsonRes.setRes(false, "Email đã được xác thực trước đó");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSonCustomResponse(jsonRes));
            } else {
                user = userService.updateisActiveUser(id.getSubject());
                token = generateToken(user.getId(),86400000);
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", "true");
            responseData.put("mesage", "Đăng ký thành công");
            responseData.put("token", token);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            session.setAttribute("authorization", token);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String indexLogin(HttpSession session //            , @RequestHeader("Authorization") String authorization
    ) {
        return "pages/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(HttpSession session,
            HttpServletRequest request, @RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        try {
            String token = null;
//        ----------------------------------
            String hashPassword = MD5(password);
            User user = userService.findByEmailAndPassword(email, hashPassword);
            if (user == null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", "false");
                responseData.put("mesage", "Sai tài khoản hoặc mật khẩu");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(responseData));
            } else {
                if (user.getIsactive() == 0) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", "false");
                    responseData.put("mesage", "Tài khoản chưa được xác thực");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(responseData));
                } else {
                    token = generateToken(user.getId(),86400000);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", "true");
                    responseData.put("mesage", "Đăng nhập thành công");
                    responseData.put("token", token);
                    responseData.put("email", user.getEmail());
                    responseData.put("name", user.getName());
                    responseData.put("avatar", user.getAvatar());
                    responseData.put("bio", user.getBio());
                    responseData.put("birthday", user.getBirthday());
                    responseData.put("coverimage", user.getCoverimage());
                    responseData.put("othername", user.getOthername());
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", token);

                    session.setAttribute("authorization", token);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/forgotpassword1", method = RequestMethod.GET)
    public String indexForgotPassword1(HttpSession session //            , @RequestHeader("Authorization") String authorization
    ) {
        return "pages/forgot1";
    }

    @RequestMapping(value = "/forgotpassword1", method = RequestMethod.POST)
    public ResponseEntity forgotpassword1(HttpSession session,
            HttpServletRequest request, @RequestParam("email") String email, Model model) {
        try {
            String contextPath = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
            User user = userService.findByEmail(email);
            if (user == null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", "false");
                responseData.put("mesage", "Gmail không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(responseData));
            } else {
                if (user.getIsactive() == 0) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", "false");
                    responseData.put("mesage", "Tài khoản chưa được xác thực");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(responseData));
                } else {
                    String codeEmail = generateToken(user.getId(), 300000);
                    Date currentDate = new Date();
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("haiduong09876@gmail.com");
                    message.setTo(email);
                    message.setText("Nhấn vào link để đổi mật khẩu: " + basePath + "forgotpassword2/" + codeEmail
                            + "\nGặp vấn đề liên hệ với Dsocial.");
                    message.setSubject("[" + currentDate + "] Dsocial | Website mạng xã hội");
                    mailSender.send(message);
                    jsonRes.setRes(true, "Link đổi mật khẩu đã được gửi qua email có hiệu lực trong 5 phút");
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSonCustomResponse(jsonRes));
                }
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/forgotpassword2/{codeEmail}", method = RequestMethod.GET)
    public String indexForgotPassword2(HttpSession session //            , @RequestHeader("Authorization") String authorization
            ,
             @PathVariable("codeEmail") String codeEmail
    ) {
        codeForgotPassword=codeEmail;
        return "pages/forgot2";
    }

    @RequestMapping(value = "/forgotpassword2", method = RequestMethod.POST)
    public ResponseEntity forgotpassword2(HttpSession session,
            HttpServletRequest request, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        try {
            String token= null;
            Claims id = decodeJWT(codeForgotPassword);
            String hashPassword = MD5(password);
            User user = userService.updatePasswordUser(id.getSubject(), hashPassword);
            if (user != null) {
                token = generateToken(user.getId(), 86400000);
            } else {
                jsonRes.setRes(false, "Thời gian đổi mật khẩu đã hết");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSonCustomResponse(jsonRes));
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", "true");
            responseData.put("mesage", "Đổi mật khẩu thành công");
            responseData.put("token", token);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            session.setAttribute("authorization", token);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}