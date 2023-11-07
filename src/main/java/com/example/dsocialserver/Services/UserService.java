/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import static com.example.dsocialserver.Utils.Pagination.getPagination;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositories.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findByPassword(String password) {
        return userRepository.finByPassword(password);
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Map<String, Object> getInfoUser(Object id) {
        Map<String, Object> data = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User list = optionalUser.get();
            data = getUser(list);
        }
        return data;
    }

    public User findById(Object id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User list = null;
        if (optionalUser.isPresent()) {
            list = optionalUser.get();
        }
        return list;
    }

    public User updateisActiveUser(Object id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User list = null;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Cập nhật các trường của đối tượng user
            user.setIs_active(1);
            // ...
            list = userRepository.save(user);
        }
        return list;
    }

    public User updatePasswordUser(Object id, String password) {
        Optional<User> optionalUser = userRepository.findById(id);
        User list = null;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Cập nhật các trường của đối tượng user
            user.setPassword(password);
            // ...
            list = userRepository.save(user);
        }
        return list;
    }

    public User createUser(String email, String password, String name, String avatar) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setAvatar(avatar);
        return userRepository.save(user);
    }
    
    public Map<String, Object> updateUser(int id, String name, String otherName, String bio, Date birthday, String avatar, String coverImage, String address){
        Optional<User> optional = userRepository.findById(id);
        Map<String, Object> data = new HashMap<>();
        if (optional.isPresent()) {
            User u = optional.get();
            // Cập nhật các trường của đối tượng bài viết
            u.setName(name);
            u.setOther_name(otherName);
            u.setBio(bio);
            u.setBirthday(birthday);
            u.setAvatar(avatar);
            u.setCover_image(coverImage);
            u.setAddress(address);
            // ...
            User updatedUser = userRepository.save(u);         
            data = getUser(updatedUser);
        }
        return data;
    }
    public Map<String, Object> getPeopleList(int page, int limit,int userId, String name) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> list = userRepository.findAllPeopleByName(pageable, userId, name);
        return reponsDataPeopleList(page, list);
    }
    public Map<String, Object> reponsDataPeopleList(int page, Page<User> list) {
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

    public static Map<String, Object> getUser(User user) {
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
        return data;
    }
}
