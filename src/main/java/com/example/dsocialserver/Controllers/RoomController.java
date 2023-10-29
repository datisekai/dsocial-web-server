/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Models.RoomUser;
import com.example.dsocialserver.Services.RoomService;
import com.example.dsocialserver.Services.RoomUserService;
import com.example.dsocialserver.Types.FriendshipType;
import com.example.dsocialserver.Types.RoomType;
import com.example.dsocialserver.Types.RoomUserType;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomUserService roomUserService;

    @PostMapping()
    public ResponseEntity createFriendRequest(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid RoomType pst, @RequestBody @Valid RoomUserType pstuser) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
            String friendId = pst.getFriendId();
            String name = pst.getName();
            String roomId = pstuser.getRoomId();
//        ----------------------------------
            RoomUser roomuser = roomUserService.findByUserId(Integer.parseInt(userId), Integer.parseInt(friendId), Integer.parseInt(roomId));
            if (roomuser != null) {
                Map<String, Object> room = roomService.createRoom(name, 0, Integer.parseInt(userId), Integer.parseInt(friendId));
                if (!room.isEmpty()) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("message", "Tạo nhóm chat thành công");
                    responseData.put("data", room);
                    return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
                }
                return StatusUntilIndex.showMissing();
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Đã có nhóm chat");
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
