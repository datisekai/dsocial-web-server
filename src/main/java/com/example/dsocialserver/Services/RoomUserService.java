/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.RoomUser;
import com.example.dsocialserver.Repositories.RoomUserReponsiitory;
import org.springframework.beans.factory.annotation.Autowired;
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
