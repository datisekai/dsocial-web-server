/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Utils.CustomResponse;
import com.example.dsocialserver.Models.Friendship;
import com.example.dsocialserver.Models.Groups;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import com.example.dsocialserver.Services.FriendshipService;
import com.example.dsocialserver.Services.UserService;
import com.example.dsocialserver.Types.FriendshipType;
import com.example.dsocialserver.Types.SearchType;
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
@RequestMapping("/friend")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    private final CustomResponse jsonRes = new CustomResponse();

//    @GetMapping()
//    public ResponseEntity getAllMyFriend(@RequestHeader("Authorization") String authorizationHeader,
//            @RequestParam(value = "page", defaultValue = "1") String page,
//            @RequestParam(value = "limit", defaultValue = "10") String limit
//    ) {
//        try {
//            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//            Map<String, Object> gr = friendshipService.getMyFriendshipList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("success", true);
//            responseData.put("data", gr.get("data"));
//            responseData.put("pagination", gr.get("pagination"));
//            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
//        } catch (NumberFormatException e) {
//            return StatusUntilIndex.showInternal(e);
//        }
//    }
    @GetMapping("/requests")
    public ResponseEntity getAllMyFriendRequests(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit
    ) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> gr = friendshipService.getMyFriendshipRequestList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    // lấy danh sách bạn bè của người khác
    @GetMapping("/{userId}")
    public ResponseEntity getAllYourFriend(@PathVariable("userId") String userId,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit
    ) {
        try {
            Map<String, Object> gr = friendshipService.getMyFriendshipList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId));

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //tìm kiếm bạn bè
    @GetMapping("/search")
    public ResponseEntity searchFriend(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------

            Map<String, Object> gr = friendshipService.getSearchMyFriendshipList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId), q);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //tìm kiếm người gửi lời mời kết bạn
    @GetMapping("request/search")
    public ResponseEntity searchNotFriend(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------

            Map<String, Object> gr = friendshipService.getSearchRequestFriendshipList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId), q);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //gửi lời mời kết bạn
    @PostMapping
    public ResponseEntity createFriendRequest(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid FriendshipType pst) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String friendId = pst.getFriendId();
//        ----------------------------------

            Map<String, Object> friendship = friendshipService.createFriendship(Integer.parseInt(userId), Integer.parseInt(friendId));
            if (!friendship.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Gửi lời mời kết bạn thành công");
                responseData.put("data", friendship.get("data"));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //chấp nhận lời mời kết bạn
    @PutMapping("/{frienId}")
    public ResponseEntity submitFriend(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("frienId") String frienId) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------
            Map<String, Object> friendship = friendshipService.updateFriendship(Integer.parseInt(frienId), Integer.parseInt(userId));
            if (!friendship.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Kết bạn thành công");
                responseData.put("data", friendship.get("data"));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //xoa ban be
    @DeleteMapping("/{frienId}")
    public ResponseEntity deleteFriend(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("frienId") String frienId) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> friendship = friendshipService.deleteFriendship(Integer.parseInt(frienId), Integer.parseInt(userId), 1);
            if (!friendship.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Xóa bạn bè thành công");
                responseData.put("data", friendship.get("data"));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    //xoa loi moi ket ban
    @DeleteMapping("request/{frienId}")
    public ResponseEntity deleteRequestFriend(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("frienId") String frienId) throws IOException {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            Map<String, Object> friendship = friendshipService.deleteFriendship(Integer.parseInt(frienId), Integer.parseInt(userId), 0);
            if (!friendship.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Xóa lời mời kết bạn thành công");
                responseData.put("data", friendship.get("data"));
                return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
            }
            return StatusUntilIndex.showMissing();
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
