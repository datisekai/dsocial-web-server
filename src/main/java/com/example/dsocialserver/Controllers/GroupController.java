/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.Groups;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.Types.GroupsType;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    private final CustomResponse jsonRes = new CustomResponse();

    // lấy ra tất cả nhóm
    @GetMapping()
    public ResponseEntity getAllGroups(@RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Page<Groups> gr = groupService.getGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
            if (gr != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("data", gr.getContent());
                responseData.put("pagination", getPagination(page, limit, gr));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    // lấy ra những nhóm của người dùng
    @GetMapping("/joined")
    public ResponseEntity getAllMyGroups(@RequestHeader("Authorization") String authorizationHeader, 
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit
    ) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Page<Groups> gr = groupService.getMyGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));
            if (gr != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("data", gr.getContent());
                responseData.put("pagination", getPagination(page, limit, gr));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PostMapping()
    public ResponseEntity createGroup(@RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody GroupsType gr) throws IOException {
        try {
            String name = gr.getName();
            String avatar = gr.getAvatar();
            String coverImage = gr.getCoverImage();
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();

//        ----------------------------------
            Map<String, Object> group = groupService.createGroup(name, Integer.parseInt(userId), avatar, coverImage);
            if (group != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Tạo nhóm thành công");
                responseData.put("data", group);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGroup(@PathVariable("id") String id,
            @Valid @RequestBody GroupsType gr) throws IOException {
        try {
            String name = gr.getName();
            String avatar = gr.getAvatar();
            String coverImage = gr.getCoverImage();
//        ----------------------------------

            Map<String, Object> group = groupService.updateGroup(name, id, avatar, coverImage);
            if (group != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Cập nhật nhóm thành công");
                responseData.put("data", group);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id") String id) throws IOException {
        try {

            boolean group = groupService.deleteGroupById(id);
            if (group) {
                jsonRes.setRes(true, "Xóa nhóm thành công");
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
