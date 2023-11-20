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

    public Map<String, Object> createFriendship(int userId, int friendId) {
        Friendship gr = new Friendship();
        gr.setUser_id(userId);
        gr.setFriend_id(friendId);
        gr.setIs_Active(0);
        friendshipRepository.save(gr);

        return getUserById(friendId);
    }

    public Map<String, Object> updateFriendship(int friendId, int userId) {
        Friendship list = new Friendship();
        if (friendId != userId) {
            Friendship optional = friendshipRepository.findFriendByUserIdAndFriendId(friendId, userId);
            if (optional != null) {
                optional.setIs_Active(1);
                // ...
                list = friendshipRepository.save(optional);
            }
        }
        return getUserById(friendId);
    }

    public Map<String, Object> deleteFriendship(int friendId, int userId, int isActive) {
        Friendship list = friendshipRepository.findFriendByUserIdAndFriendId(friendId, userId);
        Map<String, Object> result = getUserById(friendId);;
        friendshipRepository.deleteByUserIdAndFriendId(friendId, userId, isActive);
        return result;
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

    public Map<String, Object> getMyFriendList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Friendship> list = friendshipRepository.findAllFriendByUserId(pageable, userId);
        return reponsDataFriendship(page, list, userId);
    }

    public Map<String, Object> getRequestFriendList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Friendship> list = friendshipRepository.findAllRequestFriendByUserId(pageable, userId);
        return reponsDataFriendship(page, list, userId);
    }

    public Map<String, Object> getMyRequestFriendList(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Friendship> list = friendshipRepository.findAllMyRequestFriendByUserId(pageable, userId);
        return reponsDataFriendship(page, list, userId);
    }

    public Map<String, Object> getAllFriendList(int userId) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        List<Friendship> list = friendshipRepository.findAllFriend(userId);
        if (!list.isEmpty()) {
            for (Friendship o : list) {
                if (o.getUser_user_friendships().getId() == userId) {
                    listdata.add(getUser(o.getUser_friend_friendships()));
                } else {
                    listdata.add(getUser(o.getUser_user_friendships()));
                }
            }
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        return dataResult;
    }

    public Map<String, Object> reponsDataFriendship(int page, Page<Friendship> list, int userId) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Friendship o : list.getContent()) {
            if (o.getUser_user_friendships().getId() == userId) {
                listdata.add(getUser(o.getUser_friend_friendships()));
            } else {
                listdata.add(getUser(o.getUser_user_friendships()));
            }

        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> reponsDataSearchFriendship(int page, Page<User> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (User o : list.getContent()) {
            listdata.add(getUser(o));
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> reponsDataFriend(Friendship fs) {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", getUser(fs.getUser_user_friendships()));

        return dataResult;
    }

    public Map<String, Object> getUserById(Object userId) {
        Optional<User> optional = userRepository.findById(userId);
        Map<String, Object> data = new HashMap<>();
        if (optional.isPresent()) {
            User user = optional.get();
            data.put("id", user.getId());
            data.put("email", user.getEmail());
            data.put("name", user.getName());
            data.put("avatar", user.getAvatar());
            data.put("bio", user.getBio());
            data.put("birthday", user.getBirthday());
            data.put("cover_image", user.getCover_image());
            data.put("other_name", user.getOther_name());
            data.put("address", user.getAddress());
        }

        return data;
    }
}
