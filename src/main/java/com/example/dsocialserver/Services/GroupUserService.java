/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Repositories.GroupRepository;
import com.example.dsocialserver.Repositories.GroupUserRepository;
import static com.example.dsocialserver.Services.UserService.getUser;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public boolean outGroupUser(int id, int userId) {
        int result = groupUserRepository.deleteByGroupIdAndUserId(id, userId);
        return result == 1;
    }
    
     public Map<String, Object> reponsDataGroupUser( GroupUser grUser, int userId) {
            Map<String, Object> data = new HashMap<>();
            boolean isJoined = true;
            System.out.println(">>>>"+grUser);
//            data.put("id", grUser.getGroup_groupUsers().getId());
//            data.put("name", grUser.getGroup_groupUsers().getName());
//            data.put("avatar", grUser.getGroup_groupUsers().getAvatar());
//            data.put("cover_image", grUser.getGroup_groupUsers().getCover_image());
//            data.put("created_at", grUser.getGroup_groupUsers().getCreated_at());
//            data.put("user_own", getUser(grUser.getGroup_groupUsers().getUser_groups()));
//            List<Map<String, Object>> pUser = new ArrayList<>();
//            pUser.add(getUser(grUser.getGroup_groupUsers().getUser_groups()));
//            for (GroupUser gu : grUser.getGroup_groupUsers().getGroupUsers()) {
//                pUser.add(getUser(gu.getUser_groupUsers()));
//                if (!isJoined) {
//                    if (gu.getUser_groupUsers().getId() == userId ) {
//                        isJoined = true;
//                    }
//                }
//            }
//            data.put("users_joined", pUser);
            data.put("is_joined", isJoined);
//        
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", data);

        return dataResult;
    }
}
