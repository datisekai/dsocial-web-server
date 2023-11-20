/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Utils.CustomResponse;
import com.example.dsocialserver.Services.PostCommentService;
import com.example.dsocialserver.Types.PostCommentType;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@CrossOrigin
@RestController
@RequestMapping("/post-comment")
public class PostCommentController {

    @Autowired
    private PostCommentService commentService;

    private final CustomResponse jsonRes = new CustomResponse();

    @PostMapping
    public ResponseEntity createPostComment(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid PostCommentType pst) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String postId = pst.getPostId();
            String parentId = pst.getParentId();
            String content = pst.getContent();

            if (parentId == null || "".equals(parentId)) {
                parentId = "0";
            }
//        ----------------------------------

            Map<String, Object> postComment = commentService.createPostComment(Integer.parseInt(postId), Integer.parseInt(authorId), content, Integer.parseInt(parentId));
            if (!postComment.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Thêm bình luận thành công");
                responseData.put("data", postComment);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{postCommentId}")
    public ResponseEntity updatePostComment(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("postCommentId") String postCommentId,
            @RequestBody @Valid PostCommentType pst) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String content = pst.getContent();
//        ----------------------------------
            PostComment isPermission = commentService.findByIdAndAuthorId(Integer.parseInt(postCommentId), Integer.parseInt(authorId));
            if (isPermission != null) {
                Map<String, Object> postComment = commentService.updatePostComment(Integer.parseInt(postCommentId), content, Integer.parseInt(authorId));
                if (!postComment.isEmpty()) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Cập nhật bình luận thành công");
                    responseData.put("data", postComment);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            return StatusUntilIndex.showNotAuthorized();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{postCommentId}")
    public ResponseEntity deletePostComment(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("postCommentId") String postCommentId) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            PostComment isPermission = commentService.findByIdAndAuthorId(Integer.parseInt(postCommentId), Integer.parseInt(authorId));          
            if (isPermission != null) {
                Map<String, Object> post = commentService.deletePostComment(Integer.parseInt(postCommentId), Integer.parseInt(authorId));
                if (!post.isEmpty()) {
                     Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Xóa bình luận thành công");
                    responseData.put("data", post);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            return StatusUntilIndex.showNotAuthorized();
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
