/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.UserService;
import static com.example.dsocialserver.Utils.JwtTokenProvider.createJWT;
import static com.example.dsocialserver.Utils.JwtTokenProvider.decodeJWT;
import static com.example.dsocialserver.Utils.JwtTokenProvider.generateToken;
import static com.example.dsocialserver.Utils.JwtTokenProvider.isTokenExpired;
import static com.example.dsocialserver.Utils.MD5.MD5;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import static com.example.dsocialserver.Utils.StatusUntilIndex.showNotAuthorized;
import static com.example.dsocialserver.Utils.Validator.isValidEmail;
import static com.example.dsocialserver.Utils.Validator.isValidPassword;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    private final CustomResponse jsonRes = new CustomResponse();

    @GetMapping("/me")
    public ResponseEntity getInfoUser(HttpServletRequest request) {
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
            data.put("coverimage", user.getCover_image());
            data.put("othername", user.getOther_name());
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

    @PostMapping("/register")
    public ResponseEntity register(
            @RequestBody @Valid User user,
            HttpServletRequest request
    ) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();
            String name = user.getName();
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
            User u = userService.createUser(email, hashPassword, name, avatar);

//        ----------------------------------
            if (!"".equals(u.getEmail())) {
                String codeEmail = createJWT(u.getId());
                Date currentDate = new Date();
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("haiduong09876@gmail.com");
                message.setTo(u.getEmail());
                message.setText("Nhấn vào link để xác thực email: " + basePath + "user/register/authentication/" + codeEmail
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

    @GetMapping("/register/authentication/{codeEmail}")
    public ResponseEntity getCodeEmail(@PathVariable("codeEmail") String codeEmail) {
        try {
            if (codeEmail == null) {
                return StatusUntilIndex.showMissing();
            }
            Claims id = decodeJWT(codeEmail);
            User user = userService.findById(id.getSubject());
            if (user.getIs_active() != 0) {
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

    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request) {
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
                if (user.getIs_active() == 0) {
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

    @PostMapping("/forgotpassword")
    public ResponseEntity getforgotpassword(HttpServletRequest request) {
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

    @PostMapping("/resetpassword")
    public ResponseEntity resetpassword(HttpServletRequest request) {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
