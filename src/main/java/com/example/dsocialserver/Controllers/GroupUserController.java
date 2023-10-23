/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Services.GroupUserService;
import com.example.dsocialserver.Types.GroupUserType;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping("/groupuser")
public class GroupUserController {

    @Autowired
    private GroupUserService groupUserService;

    private final CustomResponse jsonRes = new CustomResponse();
    
    @PostMapping()
    public ResponseEntity joinGroupUser(@Valid @RequestBody GroupUserType gr) throws IOException {
        try {
            int groupId= gr.getGroupId();
            int userId= gr.getUserId();

//        ----------------------------------
            GroupUser group = groupUserService.joinGroupUser(groupId, userId);
            if (group != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", group.getId());
                data.put("group_id", group.getGroup_id());
                data.put("user_id", group.getUser_id());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Tham gia nhóm thành công");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{group_id}")
    public ResponseEntity deleteGroup(@PathVariable("group_id") String group_id, @Valid @RequestBody GroupUserType gr) throws IOException {
        try {
            int userId= gr.getUserId();
                boolean group = groupUserService.outGroupUser(Integer.parseInt(group_id), userId);
                if (group) {
                    jsonRes.setRes(true, "Rời nhóm thành công");
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
                }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
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
