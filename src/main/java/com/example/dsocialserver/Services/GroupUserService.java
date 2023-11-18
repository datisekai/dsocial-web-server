/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositories.GroupRepository;
import com.example.dsocialserver.Repositories.GroupUserRepository;
import static com.example.dsocialserver.Services.UserService.getUser;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private GroupRepository groupRepository;

    public Groups findByIdAndUserId(int id, int userId) {
        return groupRepository.findByIdAndUserId(id, userId);
    }

    public Map<String, Object> joinGroupUser(int groupId, int userId) {
        GroupUser gu = new GroupUser();
        gu.setGroup_id(groupId);
        gu.setUser_id(userId);
        GroupUser list = groupUserRepository.save(gu);

        return reponsDataGroupUser(list, userId);
    }

    public Map<String, Object> outGroupUser(int groupId, int userId) {
        GroupUser list = groupUserRepository.findByIdAndUserId(groupId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("data", getUser(list.getUser_groupUsers()));
        groupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
        return result;
    }

    public Map<String, Object> reponsDataGroupUser(GroupUser grUser, int userId) {
        Map<String, Object> data = new HashMap<>();
        boolean isJoined = true;
        data.put("is_joined", isJoined);
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", data);

        return dataResult;
    }
     public Map<String, Object> getUserByGroupId(int page, int limit, int userId, int groupId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<GroupUser> list = groupUserRepository.findAllUserJoinedByGroupId(pageable, groupId);
        return reponsDataUserJoined(page, list);
    }
       public Map<String, Object> reponsDataUserJoined(int page, Page<GroupUser> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (GroupUser gu : list.getContent()) {
            listdata.add(getUserJoinedGroup(gu.getUser_groupUsers(), gu.getCreated_at()));
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> getUserJoinedGroup(User user, Date joinedDate) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("name", user.getName());
        data.put("avatar", user.getAvatar());
        data.put("bio", user.getBio());
        data.put("birthday", user.getBirthday());
        data.put("cover_image", user.getCover_image());
        data.put("other_name", user.getOther_name());
        data.put("address", user.getAddress());
        data.put("joined_date", joinedDate);
        return data;
    }
}
