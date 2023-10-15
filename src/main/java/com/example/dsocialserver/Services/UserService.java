/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositorys.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User updateUser(Object id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User list = null;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Cập nhật các trường của đối tượng user
            user.setIsactive(1);
            // ...
            list= userRepository.save(user);
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
}
