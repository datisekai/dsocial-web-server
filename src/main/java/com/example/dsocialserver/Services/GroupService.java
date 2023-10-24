/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Groups;
import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Models.PostReaction;
import com.example.dsocialserver.Repositories.GroupRepository;
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

    public Page<Groups> getMyGroupList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        return groupRepository.findAllByUserId(pageable, userId);
    }

    public Page<Groups> getGroupList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return groupRepository.findAll(pageable);
    }
//    public Map<String, Object> reponsDataGroup(int page, int limit, Page<Groups> list) {
//        List<Map<String, Object>> listdata = new ArrayList<>();
//
//        for (Groups o : list.getContent()) {
//            Map<String, Object> data = new HashMap<>();
//            List<PostImage> listPostImage = postImageRepository.findAllPostImageByPostId(o.getId());
//            List<PostReaction> listPostreaction = postReactionRepository.findAllPostReactionByPostId(o.getId());
//            List<PostComment> listPostComment = postCommentRepository.findAllPostCommentByPostId(o.getId());
//
//            data.put("id", o.getId());
//            data.put("html", o.getHtml());
//            data.put("author_id", o.getAuthor_id());
//            data.put("group_id", o.getGroup_id());
//            data.put("created_at", o.getCreated_at());
//            data.put("image", listPostImage);
//            data.put("count_reaction", listPostreaction.size());
//            data.put("count_comment", listPostComment.size());
//            data.put("reaction", listPostreaction);
//            data.put("comment", listPostComment);
//            listdata.add(data);
//        }
//        Map<String, Object> dataResult = new HashMap<>();
//        dataResult.put("data", listdata);
//        dataResult.put("pagination", getPagination(page, list));
//
//        return dataResult;
//    }
}
