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
import static com.example.dsocialserver.until.MD5.MD5;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import com.example.dsocialserver.until.StatusUntilIndex;
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
import org.springframework.mail.SimpleMailMessage;
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
    public ResponseEntity indexGroup(HttpServletRequest request, HttpSession session
    //            , @RequestHeader("Authorization") String authorization
            ,  @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int limit
    ) {
//        String token = session.getAttribute("authorization").toString();
//        System.out.println(""+isLogin(token));
//        return "pages/group";
        try {
            Page<Group> gr = groupService.getGroupList(page, limit);
            Pagination p= new Pagination();
            p.setTotalPage(gr.getTotalPages());
            p.setCurrentPage(page);
            p.setNextPage(p.getCurrentPage()<p.getTotalPage() ? (p.getCurrentPage() + 1)+"" : null);
            p.setPerPage(limit);
            p.setPrevPage(p.currentPage >1 ? (p.currentPage-1)+"": null);
            Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", "true");
                responseData.put("data",  gr.getContent());
                responseData.put("totalPage", gr.getTotalPages());
                responseData.put("currentPage", page);
                responseData.put("currentPage", page);
                responseData.put("pagination", p);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (Exception e) {
             return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/group/add", method = RequestMethod.POST)
    public ResponseEntity groupAdd(HttpServletRequest request, HttpSession session //            , @RequestHeader("Authorization") String authorization
            //            ,@RequestParam("userId") int userId
            ,
             @RequestParam("avatar") MultipartFile avatarFile, @RequestParam("coverImage") MultipartFile coverImageFile,
            Model model) throws IOException {
        try {
            String avatar = "";
            String coverImage = "";
            String groupName = request.getParameter("groupName");
            if (groupName == null) {
                return StatusUntilIndex.showMissing();
            }
//        ----------------------------------
            if (!avatarFile.isEmpty()) {
                avatar = FileStorageService.generateRandomFileName(avatarFile.getOriginalFilename());
                FileStorageService.saveFile(avatarFile, avatar);
            } else {
                avatar = "https://ui-avatars.com/api/?name=" + groupName;
            }
            if (!coverImage.isEmpty()) {
                coverImage = FileStorageService.generateRandomFileName(coverImageFile.getOriginalFilename());
                FileStorageService.saveFile(coverImageFile, coverImage);
            } else {
                coverImage = "noCoverImage.png";
            }

            Group group = groupService.createGroup(groupName, 68, avatar, coverImage);
            if (group != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", "true");
                responseData.put("mesage", "Tạo nhóm thành công");
                responseData.put("groupId", group.getId());
                responseData.put("name", group.getName());
                responseData.put("avatar", group.getAvatar());
                responseData.put("coverImage", group.getCoverimage());
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/group/update", method = RequestMethod.POST)
    public ResponseEntity groupUpdate(HttpServletRequest request, HttpSession session //            , @RequestHeader("Authorization") String authorization
            //            ,@PathVariable("userId") String userId
            ,
             @RequestParam("avatar") MultipartFile avatarFile, @RequestParam("coverImage") MultipartFile coverImageFile,
            Model model) throws IOException {
        try {
            String avatar = "";
            String coverImage = "";
            String groupName = request.getParameter("groupName");
            if (groupName == null) {
                return StatusUntilIndex.showMissing();
            }
//        ----------------------------------
            if (!avatarFile.isEmpty()) {
                avatar = FileStorageService.generateRandomFileName(avatarFile.getOriginalFilename());
                FileStorageService.saveFile(avatarFile, avatar);
            } else {
                avatar = "https://ui-avatars.com/api/?name=" + groupName;
            }
            if (!coverImageFile.isEmpty()) {
                coverImage = FileStorageService.generateRandomFileName(coverImageFile.getOriginalFilename());
                FileStorageService.saveFile(coverImageFile, coverImage);
            } else {
                coverImage = "noCoverImage.png";
            }

            Group group = groupService.updateGroup(groupName, 4, avatar, coverImage);
            if (group != null) {
//                System.out.println(""+FileStorageService.getRelativeUploadPath("noCoverImage.png"));
                jsonRes.setRes(true, "Update nhóm thành công");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
    @RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    public ResponseEntity groupDelete(HttpServletRequest request, HttpSession session //            , @RequestHeader("Authorization") String authorization
            //            ,@PathVariable("userId") String userId
          ,Model model) throws IOException {
        try {

            Group group = groupService.deleteGroupById(2);
            if (group !=null) {
                jsonRes.setRes(true, "Xóa nhóm thành công");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
