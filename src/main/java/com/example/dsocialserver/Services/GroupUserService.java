/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Repositories.GroupUserRepository;
import java.util.HashMap;
import java.util.Map;
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

    public Map<String, Object> joinGroupUser(int group_id, int user_id) {
        GroupUser gu = new GroupUser();
        gu.setGroup_id(group_id);
        gu.setUser_id(user_id);
        GroupUser list = groupUserRepository.save(gu);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("group_id", list.getGroup_id());
        data.put("user_id", list.getUser_id());
        
        return data;
    }

    public boolean outGroupUser(int id) {
        try {
            groupUserRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
