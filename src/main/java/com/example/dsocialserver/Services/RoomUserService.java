/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Models.Room;
import com.example.dsocialserver.Models.RoomUser;
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
public class RoomUserService {
    @Autowired
    private RoomUserReponsiitory roomUserReponsiitory;
    
    public RoomUser findByUserId(int userId, int friendId, int roomId){
        RoomUser roomUser = roomUserReponsiitory.findByUserId(userId, friendId, roomId);
        return roomUser;
    }
}
