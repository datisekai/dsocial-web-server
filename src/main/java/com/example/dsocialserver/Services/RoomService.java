/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Room;
import com.example.dsocialserver.Models.RoomUser;
import com.example.dsocialserver.Repositories.RoomRepository;
import com.example.dsocialserver.Repositories.RoomUserReponsiitory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomUserReponsiitory roomUserReponsiitory;

    public Map<String, Object> createRoom(String name, int lastMessageId, int userId, int friendId) {
        Room room = new Room();
        room.setName(name);
        room.setLast_message_id(lastMessageId);
        Room listRoom = roomRepository.save(room);

        RoomUser rur = new RoomUser();
        rur.setUser_id(userId);
        rur.setRoom_id(listRoom.getId());
        RoomUser listRoomUserUser = roomUserReponsiitory.save(rur);

        RoomUser ruFr = new RoomUser();
        ruFr.setUser_id(friendId);
        ruFr.setRoom_id(listRoom.getId());
        RoomUser listRoomUserFriend = roomUserReponsiitory.save(ruFr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", listRoom.getId());
        data.put("name", listRoom.getName());
        data.put("last_message_id", listRoom.getLast_message_id());
        data.put("user_id", listRoomUserUser.getUser_id());
        data.put("friend_id", listRoomUserFriend.getUser_id());

        return data;
    }
}
