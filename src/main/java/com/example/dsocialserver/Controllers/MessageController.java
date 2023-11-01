/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Services.MessageService;
import com.example.dsocialserver.Types.MessageType;
import com.example.dsocialserver.Utils.CustomResponse;
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
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    private final CustomResponse jsonRes = new CustomResponse();
    
    @GetMapping("/{roomId}")
    public ResponseEntity getMessage(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("roomId") String roomId,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit
    ) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> gr = messageService.getAllMessage(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(roomId),"");
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
    
    @GetMapping("/search/{roomId}")
    public ResponseEntity getSearchMessage(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("roomId") String roomId,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q
    ) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> gr = messageService.getAllMessage(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(roomId), q);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
    
    @PostMapping()
    public ResponseEntity createMessage(@RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody MessageType gr) throws IOException {
        try {
            String content = gr.getContent();
            String roomId = gr.getRoomId();
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();

//        ----------------------------------
            Map<String, Object> message = messageService.createMessage(Integer.parseInt(userId), Integer.parseInt(roomId), content);
            if (!message.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Tạo tin nhắn thành công");
                responseData.put("data", message);
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @PutMapping("/{messageId}")
    public ResponseEntity updateGroup(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("messageId") String messageId,
            @Valid @RequestBody MessageType gr) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String content = gr.getContent();

//        ----------------------------------
            Message isPermission = messageService.findByIdAndUserId(Integer.parseInt(messageId), Integer.parseInt(userId));
            if (isPermission != null) {
                Map<String, Object> message = messageService.updateMessage(content, messageId);
                if (!message.isEmpty()) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Cập nhật tin nhắn thành công");
                    responseData.put("data", message);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            return StatusUntilIndex.showNotAuthorized();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity deleteGroup(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("messageId") String messageId) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Message isPermission = messageService.findByIdAndUserId(Integer.parseInt(messageId), Integer.parseInt(userId));
            if (isPermission != null) {
                boolean message = messageService.revokeMessage(messageId);
                if (message) {
                    jsonRes.setRes(true, "Xóa tin nhắn thành công");
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
