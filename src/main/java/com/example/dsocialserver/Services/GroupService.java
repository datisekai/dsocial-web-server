/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Repositories.GroupRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    
     public Groups findById(Object id) {
        Optional<Groups> optional = groupRepository.findById(id);
        Groups list= null;
        if (optional.isPresent()) {
            list = optional.get();
        }
        return list;
    }
     
    public Groups createGroup(String grourpName, int userId, String avatar, String coverImage) {
        Groups gr = new Groups();
        gr.setName(grourpName);
        gr.setUser_id(userId);
        gr.setAvatar(avatar);
        gr.setCover_image(coverImage);
        gr.setIs_active(1);
        return groupRepository.save(gr);
    }

    public Groups updateGroup(String name, int id, String avatar, String coverImage, int userId) {
        Optional<Groups> optional = groupRepository.findById(id);
        Groups list = null;
        if (optional.isPresent()) {
            Groups gr = optional.get();
            // Cập nhật các trường của đối tượng user
            gr.setName(name);
            gr.setAvatar(avatar);
            gr.setCover_image(coverImage);
            gr.setUser_id(userId);
            // ...
            list = groupRepository.save(gr);
        }
        return list;
    }

    public Groups deleteGroupById(int id) {
      Optional<Groups> optional = groupRepository.findById(id);
        Groups list = null;
        if (optional.isPresent()) {
            Groups gr = optional.get();
            // Cập nhật các trường của đối tượng user
            gr.setIs_active(0);
            // ...
            list = groupRepository.save(gr);
        }
        return list;
    }
    
    public Page<Groups> getGroupList(int page, int limit) {
        Pageable pageable= PageRequest.of(page, limit);
        return groupRepository.findAll(pageable);
    }
    
}
