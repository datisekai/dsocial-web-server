/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Models.Pagination;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.Services.UserService;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import static com.example.dsocialserver.Utils.StatusUntilIndex.showNotAuthorized;
import static com.example.dsocialserver.Utils.Validator.isNumeric;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    private final CustomResponse jsonRes = new CustomResponse();

    @GetMapping()
    public ResponseEntity getAllGroups(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Page<Groups> gr = groupService.getGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.getContent());
            responseData.put("pagination", getPagination(page, limit, gr));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));

        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PostMapping()
    public ResponseEntity createGroup(@Valid @RequestBody Groups gr, BindingResult bl) throws IOException {
        try {
            if(bl.hasErrors()){
                return showNotAuthorized();
            }
            String name = gr.getName();
            String avatar = gr.getAvatar();
            String coverImage = gr.getCover_image();
            int userId = gr.getUser_id();

//            String name = request.getParameter("name");
//            String avatar = request.getParameter("avatar");
//            String coverImage = request.getParameter("coverImage");
//            String userId = request.getParameter("userId");
//        ----------------------------------

            Groups group = groupService.createGroup(name, 68, avatar, coverImage);
            if (group != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", group.getId());
                data.put("name", group.getName());
                data.put("avatar", group.getAvatar());
                data.put("coverImage", group.getCover_image());
                data.put("userId", group.getUser_id());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Tạo nhóm thành công");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGroup(HttpServletRequest request, @PathVariable("id") String id) throws IOException {
        try {
            String name = request.getParameter("name");
            String avatar = request.getParameter("avatar");
            String coverImage = request.getParameter("coverImage");
            String userId = request.getParameter("userId");
            if (name == null || avatar == null || coverImage == null || userId == null || name.isEmpty() || avatar.isEmpty() || coverImage.isEmpty() || userId.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            if (!isNumeric(userId) || !isNumeric(id)) {
                return StatusUntilIndex.showMissing();
            }
            Groups gr = groupService.findById(id);

            if (gr == null) {
                return StatusUntilIndex.showMissing();
            }
            if (gr.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }

            User user = userService.findById(userId);
            if (user == null) {
                return StatusUntilIndex.showMissing();
            }
            if (user.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
//        ----------------------------------

            Groups group = groupService.updateGroup(name, Integer.parseInt(id), avatar, coverImage, Integer.parseInt(userId));
            if (group != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", group.getId());
                data.put("name", group.getName());
                data.put("avatar", group.getAvatar());
                data.put("coverImage", group.getCover_image());
                data.put("userId", group.getUser_id());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Cập nhật nhóm thành công");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGroup(HttpServletRequest request, @PathVariable("id") String id) throws IOException {
        try {
            if (id == null || id.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            groupService.deleteGroupById(Integer.parseInt(id));
            Groups g = groupService.findById(id);
            if (g != null) {
                if (g.getIs_active() != 0) {
                    Groups group = groupService.deleteGroupById(Integer.parseInt(id));
                    if (group != null) {
                        jsonRes.setRes(true, "Xóa nhóm thành công");
                        return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
                    }
                }
                jsonRes.setRes(false, "Nhóm không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
