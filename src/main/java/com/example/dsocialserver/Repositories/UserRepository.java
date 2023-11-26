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

    @Query(value = "SELECT * from user where user.password= :password", nativeQuery = true)
    User finByPassword(@Param("password") String password);

    User findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT `user`.* FROM friendship, `user` WHERE (`user`.name LIKE %:q% OR `user`.`other_name` LIKE %:q% OR `user`.address LIKE %:q% OR `user`.email LIKE %:q%) AND `user`.id != :userId AND (friendship.user_id = `user`.id OR friendship.friend_id = `user`.id) AND friendship.is_active=:isActive ORDER BY user.id DESC", nativeQuery = true)
    Page<User> findAllFriendByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q, @Param("isActive") int isActive);

    @Query(value = "SELECT `user`.* FROM friendship, `user` WHERE (`user`.name LIKE %:q% OR `user`.`other_name` LIKE %:q% OR `user`.address LIKE %:q% OR `user`.email LIKE %:q%) AND `user`.id != :userId AND  friendship.friend_id = `user`.id AND friendship.is_active=:isActive ORDER BY user.id DESC", nativeQuery = true)
    Page<User> findAllRequestFriendByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q, @Param("isActive") int isActive);

    @Query(value = "SELECT * FROM `user` WHERE (name LIKE %:q% OR other_name LIKE %:q% OR address LIKE %:q% OR email LIKE %:q%) AND id != :userId ORDER BY id DESC", nativeQuery = true)
    Page<User> findAllPeopleByName(Pageable pageable, @Param("userId") int userId, @Param("q") String q);

    @Query(value = "SELECT * FROM user, (SELECT DISTINCT user_id FROM ( SELECT receive_id AS user_id FROM message WHERE message.author_id = :authorId OR receive_id = :authorId UNION SELECT message.author_id AS user_id FROM message WHERE message.author_id = :authorId OR receive_id = :authorId ) AS subquery WHERE user_id <> :authorId ORDER BY user_id DESC) AS temp WHERE temp.user_id=user.id", nativeQuery = true)
    Page<User> findMessageUser(Pageable pageable, @Param("authorId") int authorId);

}
