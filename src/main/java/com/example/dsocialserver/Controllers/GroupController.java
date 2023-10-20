/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.Group;
import com.example.dsocialserver.Models.Pagination;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.until.FileStorageService;
import static com.example.dsocialserver.until.JwtTokenProvider.createJWT;
import static com.example.dsocialserver.until.JwtTokenProvider.isLogin;
import static com.example.dsocialserver.until.JwtTokenProvider.isTokenExpired;
import static com.example.dsocialserver.until.MD5.MD5;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import com.example.dsocialserver.until.StatusUntilIndex;
import static com.example.dsocialserver.until.StatusUntilIndex.showNotAuthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author haidu
 */
@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private FileStorageService fileStorageService;

    private final CustomResponse jsonRes = new CustomResponse();

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ResponseEntity indexGroup(HttpServletRequest request, HttpSession session) {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String page = request.getParameter("page");
                String limit = request.getParameter("limit");
                if (page == null || limit == null) {
                    return StatusUntilIndex.showMissing();
                }
                if ("".equals(page)) {
                    page = "1";
                }
                if ("".equals(limit)) {
                    limit = "10";
                }
                Page<Group> gr = groupService.getGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
                Pagination p = new Pagination();
                p.setTotalPage(gr.getTotalPages());
                p.setCurrentPage(Integer.parseInt(page));
                p.setNextPage(p.getCurrentPage() < p.getTotalPage() ? (p.getCurrentPage() + 1) + "" : null);
                p.setPerPage(gr.getNumberOfElements());
                p.setPrevPage(p.getCurrentPage() > 1 ? (p.getCurrentPage() - 1) + "" : null);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("data", gr.getContent());
                responseData.put("pagination", p);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
            }
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/group/add", method = RequestMethod.POST)
    public ResponseEntity groupAdd(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String avatar = request.getParameter("avatar");
                String coverImage = request.getParameter("coverImage");
                String groupName = request.getParameter("groupName");
                if (groupName == null || avatar == null || coverImage == null || groupName.isEmpty() || avatar.isEmpty() || coverImage.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
//        ----------------------------------

                Group group = groupService.createGroup(groupName, 68, avatar, coverImage);
                if (group != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("groupId", group.getId());
                    data.put("groupName", group.getName());
                    data.put("avatar", group.getAvatar());
                    data.put("coverImage", group.getCoverimage());
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Cập nhật nhóm thành công");
                    responseData.put("data", data);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/group/update", method = RequestMethod.POST)
    public ResponseEntity groupUpdate(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String avatar = request.getParameter("avatar");
                String coverImage = request.getParameter("coverImage");
                String groupName = request.getParameter("groupName");
                String groupId = request.getParameter("groupId");
                if (groupName == null || avatar == null || coverImage == null || groupId == null
                        || groupName.isEmpty() || avatar.isEmpty() || coverImage.isEmpty() || groupId.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
//        ----------------------------------

                Group group = groupService.updateGroup(groupName, Integer.parseInt(groupId), avatar, coverImage);
                if (group != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("groupId", group.getId());
                    data.put("groupName", groupName);
                    data.put("avatar", group.getAvatar());
                    data.put("coverImage", group.getCoverimage());
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Tạo nhóm thành công");
                    responseData.put("data", data);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    public ResponseEntity groupDelete(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String groupId = request.getParameter("groupId");
                if (groupId == null || groupId.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
                Group g = groupService.findById(groupId);
                if (g != null) {
                    if (g.getIsActive() != 0) {
                        Group group = groupService.deleteGroupById(Integer.parseInt(groupId));
                        if (group != null) {
                            jsonRes.setRes(true, "Xóa nhóm thành công");
                            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
                        }
                    }
                    jsonRes.setRes(false, "Nhóm không tồn tại");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
                }
                return StatusUntilIndex.showMissing();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
            }
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
