/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.PostReaction;
import com.example.dsocialserver.Utils.CustomResponse;
import com.example.dsocialserver.Services.PostReactionService;
import com.example.dsocialserver.Types.PostReactionType;
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
@RequestMapping("/post-reaction")
public class PostReactionController {

    @Autowired
    private PostReactionService postReactionService;

    private final CustomResponse jsonRes = new CustomResponse();

    @PostMapping
    public ResponseEntity createPostReaction(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid PostReactionType pst) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String postId = pst.getPostId();
            String icon = pst.getIcon();
//        ----------------------------------

            Map<String, Object> postReaction = postReactionService.createPostReaction(Integer.parseInt(postId), Integer.parseInt(authorId), icon);
            if (!postReaction.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Thêm cảm xúc thành công");
                responseData.put("data", postReaction);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{postReactionId}")
    public ResponseEntity updatePostReaction(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("postReactionId") String postReactionId,
            @RequestBody @Valid PostReactionType pst) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String icon = pst.getIcon();
//        ----------------------------------
            PostReaction isPermission = postReactionService.findByIdAndAuthorId(Integer.parseInt(postReactionId), Integer.parseInt(authorId));
            if (isPermission != null) {
                Map<String, Object> postReaction = postReactionService.updatePostReaction(Integer.parseInt(postReactionId), icon);
                if (!postReaction.isEmpty()) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Cập nhật cảm xúc thành công");
                    responseData.put("data", postReaction);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            return StatusUntilIndex.showNotAuthorized();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{postReactionId}")
    public ResponseEntity deletePostReaction(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("postReactionId") String postReactionId) throws IOException {
        try {
            String authorId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            PostReaction isPermission = postReactionService.findByIdAndAuthorId(Integer.parseInt(postReactionId), Integer.parseInt(authorId));
            if (isPermission != null) {
                boolean postReaction = postReactionService.deletePostReaction(postReactionId);
                if (postReaction == true) {
                    jsonRes.setRes(true, "Xóa cảm xúc thành công");
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(jsonRes));
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
