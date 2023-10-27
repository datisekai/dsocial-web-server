/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.CustomResponse;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Services.PostService;
import com.example.dsocialserver.Types.PostType;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    private final CustomResponse jsonRes = new CustomResponse();

    // lấy ra tất cả bài viết ở home page
    @GetMapping()
    public ResponseEntity getAllPost(@RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Map<String, Object> post = postService.getPostList(Integer.parseInt(page) - 1, Integer.parseInt(limit));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", post.get("data"));
            responseData.put("pagination", post.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    // lấy ra tất cả bài viết của group
    @GetMapping("/group/{groupId}")
    public ResponseEntity getAllPostGroup(@PathVariable("groupId") String groupId, @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Map<String, Object> post = postService.getPostListGroup(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(groupId));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", post.get("data"));
            responseData.put("pagination", post.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    // lấy ra tất cả bài viết của mình
//    @GetMapping("/me")
//    public ResponseEntity getAllPostMe(@RequestHeader("Authorization") String authorizationHeader,
//            @RequestParam(value = "page", defaultValue = "1") String page,
//            @RequestParam(value = "limit", defaultValue = "10") String limit) {
//        try {
//            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//            Map<String, Object> post = postService.getPostListUser(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));
//            if (post != null) {
//                Map<String, Object> responseData = new HashMap<>();
//                responseData.put("success", true);
//                responseData.put("data", post.get("data"));
//                responseData.put("pagination", post.get("pagination"));
//                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
//            }
//            return StatusUntilIndex.showMissing();
//        } catch (NumberFormatException e) {
//            return StatusUntilIndex.showInternal(e);
//        }
//    }
    // lấy ra tất cả bài viết của nguoi dung
    @GetMapping("/user/{userId}")
    public ResponseEntity getAllPostUser(@PathVariable("userId") String userId,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit) {
        try {
            Map<String, Object> post = postService.getPostListUser(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", post.get("data"));
            responseData.put("pagination", post.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PostMapping
    public ResponseEntity createPost(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid PostType pst) throws IOException {
        try {
            String html = pst.getHtml();
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            List<PostImage> imagage = pst.getImage();
            String groupId = pst.getGroupId();

            if (groupId == null || "".equals(groupId)) {
                groupId = "0";
            }
//        ----------------------------------

            Map<String, Object> post = postService.createPost(html, Integer.parseInt(authorId), Integer.parseInt(groupId), imagage);
            if (post != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Thêm bài viết thành công");
                responseData.put("data", post);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody @Valid PostType pst) throws IOException {
        try {
            String html = pst.getHtml();
            List<PostImage> imagage = pst.getImage();
//        ----------------------------------

            Map<String, Object> post = postService.updatePost(id, html, imagage);
            if (post != null) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Cập nhật bài viết thành công");
                responseData.put("data", post);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (Exception e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity groupDelete(@PathVariable("id") String id) throws IOException {
        try {
            boolean post = postService.deletePost(id);
            if (post == true) {
                jsonRes.setRes(true, "Xóa bài viết thành công");
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
            }
            return StatusUntilIndex.showMissing();
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
