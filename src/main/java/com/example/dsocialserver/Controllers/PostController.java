/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Models.Pagination;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.Services.PostService;
import com.example.dsocialserver.Services.UserService;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import static com.example.dsocialserver.Utils.Validator.isNumeric;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    private final CustomResponse jsonRes = new CustomResponse();

    @GetMapping()
    public ResponseEntity getAllPost(@RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Page<Post> getListPost = postService.getPostList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", getListPost.getContent());
            responseData.put("pagination", getPagination(page, limit, getListPost));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PostMapping
    public ResponseEntity createPost(HttpServletRequest request, HttpSession session,
            Model model) throws IOException {
        try {
            String html = request.getParameter("html");
            String authorId = request.getParameter("authorId");
            String groupId = request.getParameter("groupId");
            if (html == null || authorId == null || groupId == null || html.isEmpty() || authorId.isEmpty() || groupId.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            if (!isNumeric(authorId) || !isNumeric(groupId)) {
                return StatusUntilIndex.showMissing();
            }
            User user = userService.findById(authorId);
            if (user == null) {
                return StatusUntilIndex.showMissing();
            }
            if (user.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
            Groups group = groupService.findById(groupId);
            if (group == null) {
                return StatusUntilIndex.showMissing();
            }
            if (group.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
//        ----------------------------------

            Post post = postService.createPost(html, Integer.parseInt(authorId), Integer.parseInt(groupId));
            if (post != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", post.getId());
                data.put("html", post.getHtml());
                data.put("authorId", post.getAuthor_id());
                data.put("groupId", post.getGroup_id());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Thêm bài viết thành công");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(HttpServletRequest request, @PathVariable("id") String id) throws IOException {
        try {
            String html = request.getParameter("html");
            String authorId = request.getParameter("authorId");
            String groupId = request.getParameter("groupId");
            if (html == null || authorId == null || groupId == null || html.isEmpty() || authorId.isEmpty() || groupId.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }

            if (!isNumeric(authorId) || !isNumeric(groupId) || !isNumeric(id)) {
                return StatusUntilIndex.showMissing();
            }
            Post p = postService.findById(id);
            if (p == null) {
                return StatusUntilIndex.showMissing();
            }
            if (p.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
            User user = userService.findById(authorId);
            if (user == null) {
                return StatusUntilIndex.showMissing();
            }
            if (user.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
            Groups group = groupService.findById(groupId);
            if (group == null) {
                return StatusUntilIndex.showMissing();
            }
            if (group.getIs_active() == 0) {
                return StatusUntilIndex.showMissing();
            }
//        ----------------------------------

            Post post = postService.updatePost(Integer.parseInt(id), html, Integer.parseInt(authorId), Integer.parseInt(groupId));
            if (post != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", post.getId());
                data.put("html", post.getHtml());
                data.put("authorId", post.getAuthor_id());
                data.put("groupId", post.getGroup_id());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Cập nhật bài viết thành công");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity groupDelete(HttpServletRequest request, @PathVariable("id") String id) throws IOException {
        try {
            if (id == null || id.isEmpty()) {
                return StatusUntilIndex.showMissing();
            }
            Post p = postService.findById(id);
            if (p != null) {
                if (p.getIs_active() != 0) {
                    Post post = postService.deletePostById(Integer.parseInt(id));
                    if (post != null) {
                        jsonRes.setRes(true, "Xóa bài viết thành công");
                        return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
                    }
                }
                jsonRes.setRes(false, "Bài viết không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
        } catch (MailException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
