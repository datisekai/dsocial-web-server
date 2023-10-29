/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Utils.CustomResponse;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.UserService;
import com.example.dsocialserver.Types.ForgotpasswordType;
import com.example.dsocialserver.Types.LoginType;
import com.example.dsocialserver.Types.RegisterType;
import com.example.dsocialserver.Types.ResetpasswordType;
import com.example.dsocialserver.Types.UserType;
import static com.example.dsocialserver.Utils.FileStorageService.generateRandomFileName;
import static com.example.dsocialserver.Utils.FileStorageService.saveFile;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.JwtTokenProvider.createJWT;
import static com.example.dsocialserver.Utils.JwtTokenProvider.decodeJWT;
import static com.example.dsocialserver.Utils.JwtTokenProvider.generateToken;
import static com.example.dsocialserver.Utils.JwtTokenProvider.isTokenExpired;
import static com.example.dsocialserver.Utils.MD5.MD5;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import static com.example.dsocialserver.Utils.StatusUntilIndex.showNotAuthorized;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    private final CustomResponse jsonRes = new CustomResponse();

    // lấy thông tin người khác
    @GetMapping("/info/{userId}")
    public ResponseEntity getInfoUser(@PathVariable("userId") String userId) {
        try {
            Map<String, Object> user = userService.getInfoUser(userId);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Authorization");
            responseData.put("data", user);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity getInfoMe(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7); // Loại bỏ phần "Bearer "
            // Bạn có thể sử dụng giá trị của bearerToken ở đây
            if (isTokenExpired(bearerToken) == 0) {
                return showNotAuthorized();
            }
            Map<String, Object> user = userService.getInfoUser(isTokenExpired(bearerToken));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Authorization");
            responseData.put("data", user);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } else {
            // Trường hợp không tìm thấy hoặc không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(
            @RequestBody @Valid RegisterType user,
            HttpServletRequest request) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();
            String name = user.getName();
            // ----------------------------------
            User isExist = userService.findByEmail(email);
            if (isExist != null) {
                jsonRes.setRes(false, "Email đã được sử dụng");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }

            // ----------------------------------
            String avatar = "https://ui-avatars.com/api/?name=" + name;
            String hashPassword = MD5(password);
            User u = userService.createUser(email, hashPassword, name, avatar);

            // ----------------------------------
            if (u != null) {
                String codeEmail = createJWT(u.getId());
                Date currentDate = new Date();
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("haiduong09876@gmail.com");
                message.setTo(u.getEmail());
                message.setText("Nhấn vào link để xác thực email: " + environment.getProperty("fe.url")
                        + "/confirm-email?token=" + codeEmail
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
    public ResponseEntity login(@Valid @RequestBody LoginType us) {
        try {
            String email = us.getEmail();
            String password = us.getPassword();
            String token = null;
            // ----------------------------------
            String hashPassword = MD5(password);
            User user = userService.findByEmailAndPassword(email, hashPassword);
            if (user == null) {
                jsonRes.setRes(false, "Sai tài khoản hoặc mật khẩu");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            if (user.getIs_active() == 0) {
                jsonRes.setRes(false, "Tài khoản chưa được xác thực");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            token = generateToken(user.getId(), 86400000); // 86400000 300000
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Đăng nhập thành công");
            responseData.put("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));

        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity getforgotpassword(@Valid @RequestBody ForgotpasswordType us) {
        try {
            String email = us.getEmail();
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
            message.setText("Nhấn vào link để đổi mật khẩu: " + environment.getProperty("fe.url")
                    + "/resetpassword?token=" + codeEmail
                    + "\nGặp vấn đề liên hệ với Dsocial.");
            message.setSubject("[" + currentDate + "] Dsocial | Website mạng xã hội");
            mailSender.send(message);
            jsonRes.setRes(true, "Link đổi mật khẩu đã được gửi qua email có hiệu lực trong 5 phút");
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
    /// register/a

    @PostMapping("/resetpassword")
    public ResponseEntity resetpassword(@Valid @RequestBody ResetpasswordType us) {
        try {
            String password = us.getPassword();
            String token = us.getToken();

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

    @PutMapping("/edit-profile")
    public ResponseEntity editProfile(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid UserType pst) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String name = pst.getName();
            String otherName = pst.getOtherName();
            String bio = pst.getBio();
            Date birthday = pst.getBirthday();
            String avatar = pst.getAvatar();
            String coverImage = pst.getCoverImage();
            String address = pst.getAddress();
            if (avatar == null || avatar.isEmpty() || "".equals(avatar.trim())) {
                avatar = "https://ui-avatars.com/api/?name=" + name;
            }

//        ----------------------------------
            Map<String, Object> user = userService.updateUser(Integer.parseInt(userId), name, otherName, bio, birthday, avatar, coverImage, address);
            if (!user.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Cập nhật hồ sơ thành công");
                responseData.put("data", user);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //upload ảnh
    @PostMapping("/upload")
    public ResponseEntity createFriendRequest(@RequestParam("file") MultipartFile file) throws IOException {
        try {
//        ---------------------------------
            if (!"".equals(file.getOriginalFilename())) {
                saveFile(file, generateRandomFileName(file.getOriginalFilename()));
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", " Upload thành công");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();

        } catch (IOException e) {
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
