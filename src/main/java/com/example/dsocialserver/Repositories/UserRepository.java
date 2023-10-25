/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author haidu
 */
@Repository
public interface UserRepository extends CrudRepository<User, Object> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT `user`.* FROM friendship, `user` WHERE `user`.name LIKE %:name% AND `user`.id != :userId AND (friendship.user_id = `user`.id OR friendship.friend_id = `user`.id) AND friendship.is_active=1", nativeQuery = true)
    Page<User> findAllSearchFriendshipByUserId(Pageable pageable, @Param("userId") int userId, @Param("name") String name);
}
