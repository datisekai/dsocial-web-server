/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.Group;
import com.example.dsocialserver.Models.Pagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.Services.PostService;
import com.example.dsocialserver.Services.UserService;
import static com.example.dsocialserver.until.JwtTokenProvider.isLogin;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import com.example.dsocialserver.until.StatusUntilIndex;
import static com.example.dsocialserver.until.StatusUntilIndex.showNotAuthorized;
import static com.example.dsocialserver.until.Validator.isNumeric;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
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

/**
 *
 * @author haidu
 */
@Controller
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupService groupService;

    private final CustomResponse jsonRes = new CustomResponse();

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public ResponseEntity indexPost(HttpServletRequest request, HttpSession session) {
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
                Page<Post> po = postService.getGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
                    Pagination p = new Pagination();
                    p.setTotalPage(po.getTotalPages());
                    p.setCurrentPage(Integer.parseInt(page));
                    p.setNextPage(p.getCurrentPage() < p.getTotalPage() ? (p.getCurrentPage() + 1) + "" : null);
                    p.setPerPage(po.getNumberOfElements());
                    p.setPrevPage(p.getCurrentPage() > 1 ? (p.getCurrentPage() - 1) + "" : null);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("data", po.getContent());
                    responseData.put("pagination", p);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));         
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer Token");
            }
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @RequestMapping(value = "/post/add", method = RequestMethod.POST)
    public ResponseEntity postAdd(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String html = request.getParameter("html");
                String authorId = request.getParameter("authorId");
                String groupId = request.getParameter("groupId");
                if (html == null || authorId == null || groupId == null || html.isEmpty() || authorId.isEmpty() || groupId.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
                if (!isNumeric(authorId) || !isNumeric(groupId)){
                    return StatusUntilIndex.showMissing();
                }
                User user = userService.findById(authorId);
                if(user==null){
                    return StatusUntilIndex.showMissing();
                }
                if(user.getIsactive()==0){
                    return StatusUntilIndex.showMissing();
                }
                Group group = groupService.findById(groupId);
                if(group==null){
                    return StatusUntilIndex.showMissing();
                }
                if(group.getIsActive()==0){
                    return StatusUntilIndex.showMissing();
                }
//        ----------------------------------

                Post post = postService.createGroup(html, Integer.parseInt(authorId), Integer.parseInt(groupId));
                if (post != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", post.getId());
                    data.put("html", post.getHtml());
                    data.put("authorId", post.getAuthorid());
                    data.put("groupId", post.getGroupid());
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Thêm bài viết thành công");
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

    @RequestMapping(value = "/post/update", method = RequestMethod.POST)
    public ResponseEntity groupUpdate(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String id = request.getParameter("id");
                String html = request.getParameter("html");
                String authorId = request.getParameter("authorId");
                String groupId = request.getParameter("groupId");
                if (html == null || authorId == null || groupId == null || html.isEmpty() || authorId.isEmpty() || groupId.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
                
                if (!isNumeric(authorId) || !isNumeric(groupId) || !isNumeric(id)){
                    return StatusUntilIndex.showMissing();
                }
                Post p =postService.findById(id);
                if(p == null){
                    return StatusUntilIndex.showMissing();
                }
                if(p.getIsactive()==0){
                    return StatusUntilIndex.showMissing();
                }
                User user = userService.findById(authorId);
                if(user==null){
                    return StatusUntilIndex.showMissing();
                }
                if(user.getIsactive()==0){
                    return StatusUntilIndex.showMissing();
                }
                Group group = groupService.findById(groupId);
                if(group==null){
                    return StatusUntilIndex.showMissing();
                }
                if(group.getIsActive()==0){
                    return StatusUntilIndex.showMissing();
                }
//        ----------------------------------

                Post post = postService.updateGroup(Integer.parseInt(id), html, Integer.parseInt(authorId), Integer.parseInt(groupId));
                if (post != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", post.getId());
                    data.put("html", post.getHtml());
                    data.put("authorId", post.getAuthorid());
                    data.put("groupId", post.getGroupid());
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Cập nhật bài viết thành công");
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

    @RequestMapping(value = "/post/delete", method = RequestMethod.POST)
    public ResponseEntity groupDelete(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String isLogin = isLogin(request);
            if ("tokenExpired".equals(isLogin)) {
                return showNotAuthorized();
            } else if ("tokenNotExpired".equals(isLogin)) {
                String id = request.getParameter("id");
                if (id == null || id.isEmpty()) {
                    return StatusUntilIndex.showMissing();
                }
                Post p = postService.findById(id);
                if (p != null) {
                    if (p.getIsactive() != 0) {
                        Post group = postService.deleteGroupById(Integer.parseInt(id));
                        if (group != null) {
                            jsonRes.setRes(true, "Xóa bài viết thành công");
                            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
                        }
                    }
                    jsonRes.setRes(false, "Bài viết không tồn tại");
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
