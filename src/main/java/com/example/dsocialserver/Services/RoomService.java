/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Room;
import com.example.dsocialserver.Models.RoomUser;
import com.example.dsocialserver.Repositories.RoomRepository;
import com.example.dsocialserver.Repositories.RoomUserReponsiitory;
import static com.example.dsocialserver.Services.UserService.getUser;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        public Map<String, Object> getAllRoom(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Room> list = roomRepository.findRoomByuserId(pageable, userId);
        return reponsDataMessage(page, list);
    }
      public Map<String, Object> reponsDataMessage(int page, Page<Room> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Room o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();

            data.put("id", o.getId());
            data.put("name", o.getName());
            data.put("last_message_id", o.getLast_message_id());
            List<Map<String, Object>> pUser = new ArrayList<>();
            for(RoomUser i: o.getRoomUsers()){
                pUser.add(getUser(i.getUser_roomUsers()));
            }
            data.put("users_joined", pUser);
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }
}
