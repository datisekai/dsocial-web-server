/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.Groups;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Models.PostReaction;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositories.GroupRepository;
import static com.example.dsocialserver.Services.UserService.getUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Groups findById(Object id) {
        Optional<Groups> optional = groupRepository.findById(id);
        Groups list = null;
        if (optional.isPresent()) {
            list = optional.get();
        }
        return list;
    }

    public Map<String, Object> createGroup(String grourpName, int userId, String avatar, String coverImage) {
        Groups gr = new Groups();
        gr.setName(grourpName);
        gr.setUser_id(userId);
        gr.setAvatar(avatar);
        gr.setCover_image(coverImage);
        gr.setIs_active(1);
        Groups list = groupRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("name", list.getName());
        data.put("avatar", list.getAvatar());
        data.put("cover_image", list.getCover_image());
        data.put("user_id", list.getUser_id());

        return data;
    }

    public Map<String, Object> updateGroup(String name, Object id, String avatar, String coverImage) {
        Map<String, Object> data = new HashMap<>();
        Optional<Groups> optional = groupRepository.findById(id);
        if (optional.isPresent()) {
            Groups gr = optional.get();
            gr.setName(name);
            gr.setAvatar(avatar);
            gr.setCover_image(coverImage);
            // ...
            Groups list = groupRepository.save(gr);
            data.put("id", list.getId());
            data.put("name", list.getName());
            data.put("avatar", list.getAvatar());
            data.put("cover_image", list.getCover_image());
            data.put("user_id", list.getUser_id());
        }
        return data;
    }

    public boolean deleteGroup(Object id) {
//      Optional<Groups> optional = groupRepository.findById(id);
//        Groups list = null;
//        if (optional.isPresent()) {
//            Groups gr = optional.get();
//            // Cập nhật các trường của đối tượng user
//            gr.setIs_active(0);
//            // ...
//            list = groupRepository.save(gr);
//        }
        try {
            groupRepository.deleteGroups(Integer.parseInt((String) id));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public Map<String, Object> getSearchGroupList(int page, int limit, String name) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Groups> list = groupRepository.findAllByName(pageable, name);
        return reponsDataGroup(page, list);
    }

    public Map<String, Object> getMyGroupList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Groups> list = groupRepository.findAllByUserId(pageable, userId);
        return reponsDataGroup(page, list);
    }

    public Map<String, Object> getGroupList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Groups> list = groupRepository.findAll(pageable);
        return reponsDataGroup(page, list);
    }

    public Map<String, Object> reponsDataGroup(int page, Page<Groups> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Groups o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();

            data.put("id", o.getId());
            data.put("name", o.getName());
            data.put("avatar", o.getAvatar());
            data.put("cover_image", o.getCover_image());
            data.put("created_at", o.getCreated_at());
            data.put("user_id_boss", o.getUser_id());
            List<Map<String, Object>> pUser = new ArrayList<>();
//            pUser.add(getUser(o.getUser()));
            for (GroupUser gu : o.getGroupUsers()) {
                pUser.add(getUser(gu.getUser_groupUsers()));
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
