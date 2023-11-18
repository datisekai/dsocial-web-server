/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Utils.CustomResponse;
import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Services.GroupUserService;
import com.example.dsocialserver.Types.GroupOwnType;
import com.example.dsocialserver.Types.GroupUserType;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@CrossOrigin
@RestController
@RequestMapping("/group-user")
public class GroupUserController {

    @Autowired
    private GroupUserService groupUserService;

    private final CustomResponse jsonRes = new CustomResponse();
    
     @GetMapping("/{groupId}")
    public ResponseEntity getUserByGroupId(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> gr = groupUserService.getUserByGroupId(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId), Integer.parseInt(groupId));
            Map<String, Object> responseData = new HashMap<>();
             responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
    
    @PostMapping()
    public ResponseEntity joinGroupUser(@RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody GroupUserType gr) throws IOException {
        try {
            String groupId = gr.getGroupId();
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();

//        ----------------------------------
            Map<String, Object> groupUser = groupUserService.joinGroupUser(Integer.parseInt(groupId), Integer.parseInt(userId));
            if (!groupUser.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Tham gia nhóm thành công");
                responseData.put("data", groupUser.get("data"));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/kick")
    public ResponseEntity kickUserGroupUser(@Valid @RequestBody GroupOwnType gr, @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        try {
            String userId_own = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String groupId = gr.getGroupId();
            String userId = gr.getUserId();
            Groups isOwn = groupUserService.findByIdAndUserId(Integer.parseInt(groupId), Integer.parseInt(userId_own));
            if (isOwn != null) {
                Map<String, Object> friend = groupUserService.outGroupUser(Integer.parseInt(groupId), Integer.parseInt(userId));
                if (!friend.isEmpty()) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Đuổi thành viên thành công");
                    responseData.put("data", friend.get("data"));
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            return StatusUntilIndex.showNotAuthorized();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping()
    public ResponseEntity outGroupUser(@Valid @RequestBody GroupUserType gr,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        try {
            String groupId = gr.getGroupId();
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> friend = groupUserService.outGroupUser(Integer.parseInt(groupId), Integer.parseInt(userId));
            if (!friend.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Rời nhóm thành công");
                    responseData.put("data", friend.get("data"));
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
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
