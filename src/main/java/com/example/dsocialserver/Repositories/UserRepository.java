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
    
    @Query(value = "SELECT `user`.* FROM friendship, `user` WHERE (`user`.name LIKE %:q% OR `user`.`other_name` LIKE %:q% OR `user`.address LIKE %:q% OR `user`.email LIKE %:q%) AND `user`.id != :userId AND (friendship.user_id = `user`.id OR friendship.friend_id = `user`.id) AND friendship.is_active=:isActive ORDER BY user.id DESC", nativeQuery = true)
    Page<User> findAllFriendByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q, @Param("isActive") int isActive);

    @Query(value = "SELECT `user`.* FROM friendship, `user` WHERE (`user`.name LIKE %:q% OR `user`.`other_name` LIKE %:q% OR `user`.address LIKE %:q% OR `user`.email LIKE %:q%) AND `user`.id != :userId AND  friendship.friend_id = `user`.id AND friendship.is_active=:isActive ORDER BY user.id DESC", nativeQuery = true)
    Page<User> findAllRequestFriendByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q, @Param("isActive") int isActive);
    
    @Query(value = "SELECT * FROM `user` WHERE (name LIKE %:q% OR other_name LIKE %:q% OR address LIKE %:q% OR email LIKE %:q%) AND id != :userId AND id NOT IN ( SELECT friend_id FROM friendship WHERE user_id = :userId ) AND id NOT IN ( SELECT user_id FROM friendship WHERE friend_id = :userId ) ORDER BY id DESC;", nativeQuery = true)
    Page<User> findAllPeopleByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q);
}
