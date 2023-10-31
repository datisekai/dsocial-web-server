/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Friendship;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import com.example.dsocialserver.Models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dsocialserver.Repositories.FriendshipRepository;
import com.example.dsocialserver.Repositories.UserRepository;
import static com.example.dsocialserver.Services.UserService.getUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> createFriendship(int userId, int friendshipId) {
        Friendship gr = new Friendship();
        gr.setUser_id(userId);
        gr.setFriend_id(friendshipId);
        gr.setIs_Active(0);
        Friendship list = friendshipRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("user_id", list.getUser_id());
        data.put("friendship_id", list.getFriend_id());
        data.put("is_active", list.getIs_Active());

        return data;
    }

    public Map<String, Object> updateFriendship(int friendId, int userId) {
        Map<String, Object> data = new HashMap<>();
        if (friendId != userId) {
            Friendship optional = friendshipRepository.findFriendByUserIdAndFriendId(friendId, userId);
            if (optional != null) {
                optional.setIs_Active(1);
                // ...
                Friendship list = friendshipRepository.save(optional);
                data.put("id", list.getId());
                data.put("user_id", list.getUser_id());
                data.put("friendship_id", list.getFriend_id());
                data.put("is_active", list.getIs_Active());
            }
        }
        return data;
    }

    public boolean deleteFriendship(int friendId, int userId, int isActive) {
        int result = friendshipRepository.deleteByUserIdAndFriendId(friendId, userId, isActive);
        return result == 1;
    }

    public Map<String, Object> getSearchMyFriendshipList(int page, int limit, int userId, String name) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> list = userRepository.findAllFriendByName(pageable, userId, name, 1);
        return reponsDataSearchFriendship(page, list);
    }

    public Map<String, Object> getSearchRequestFriendshipList(int page, int limit, int userId, String name) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> list = userRepository.findAllRequestFriendByName(pageable, userId, name, 0);
        return reponsDataSearchFriendship(page, list);
    }

    public Map<String, Object> getMyFriendshipList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Friendship> list = friendshipRepository.findAllFriendshipByUserId(pageable, userId, 1);
        return reponsDataFriendship(page, list);
    }

    public Map<String, Object> getMyFriendshipRequestList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Friendship> list = friendshipRepository.findAllRequestFriendshipByUserId(pageable, userId, 0);
        return reponsDataFriendship(page, list);
    }

    public Map<String, Object> reponsDataFriendship(int page, Page<Friendship> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Friendship o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();

            data.put("id", o.getId());
            data.put("user_request", getUser(o.getUser_user_friendships()));
            data.put("friend", getUser(o.getUser_friend_friendships()));
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> reponsDataSearchFriendship(int page, Page<User> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (User o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();
            listdata.add(getUser(o));
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }
}
