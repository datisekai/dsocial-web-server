/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Group;
import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositorys.GroupRepository;
import com.example.dsocialserver.Repositorys.GroupUserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    
    @Autowired
    private GroupUserRepository groupUserRepository;

    public Group createGroup(String grourpName, int userId, String avatar, String coverImage) {
        Group gr = new Group();
        gr.setName(grourpName);
        gr.setUserid(userId);
        gr.setAvatar(avatar);
        gr.setCoverimage(coverImage);
        gr.setIsActive(1);
        return groupRepository.save(gr);
    }

    public Group updateGroup(String groupName, int id, String avatar, String coverImage) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        Group list = null;
        if (optionalGroup.isPresent()) {
            Group gr = optionalGroup.get();
            // Cập nhật các trường của đối tượng user
            gr.setName(groupName);
            gr.setAvatar(avatar);
            gr.setCoverimage(coverImage);
            // ...
            list = groupRepository.save(gr);
        }
        return list;
    }

    public Group deleteGroupById(int id) {
      Optional<Group> optionalGroup = groupRepository.findById(id);
        Group list = null;
        if (optionalGroup.isPresent()) {
            Group gr = optionalGroup.get();
            // Cập nhật các trường của đối tượng user
            gr.setIsActive(0);
            // ...
            list = groupRepository.save(gr);
        }
        return list;
    }
    
    public Page<Group> getGroupList(int page, int limit) {
        Pageable pageable= PageRequest.of(page, limit);
        return groupRepository.findAll(pageable);
    }
    
}
