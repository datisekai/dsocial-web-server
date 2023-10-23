/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Repositories.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class GroupUserService {

    @Autowired
    private GroupUserRepository groupUserRepository;

    public GroupUser joinGroupUser(int group_id, int user_id) {
        GroupUser gu = new GroupUser();
        gu.setGroup_id(group_id);
        gu.setUser_id(user_id);
        return groupUserRepository.save(gu);
    }

    public boolean outGroupUser(int group_id, int user_id) {
        try {
            groupUserRepository.deleteByGroupIdAndUserId(group_id, user_id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
