/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Friendship;
import com.example.dsocialserver.Models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author haidu
 */
@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Object> {

    @Query(value = "SELECT * FROM friendship WHERE (friendship.friend_id = :userId OR friendship.user_id = :userId) AND friendship.is_active= 1 ORDER BY friendship.id DESC", nativeQuery = true)
    Page<Friendship> findAllFriendByUserId(Pageable pageable, @Param("userId") int userId);

    @Query(value = "SELECT * FROM friendship WHERE  friendship.friend_id = :userId AND friendship.is_active= 0 ORDER BY friendship.id DESC", nativeQuery = true)
    Page<Friendship> findAllRequestFriendByUserId(Pageable pageable, @Param("userId") int userId);

    @Query(value = "SELECT * FROM friendship WHERE friendship.user_id = :userId AND friendship.is_active= 0 ORDER BY friendship.id DESC", nativeQuery = true)
    Page<Friendship> findAllMyRequestFriendByUserId(Pageable pageable, @Param("userId") int userId);

    @Query(value = "SELECT * FROM friendship WHERE (friendship.friend_id = :userId OR friendship.user_id = :userId) AND friendship.is_active= 1 ORDER BY friendship.id DESC", nativeQuery = true)
    List<Friendship> findAllFriend(@Param("userId") int userId);

    @Query(value = "SELECT * FROM friendship WHERE (friendship.friend_id= :friendId AND friendship.user_id= :userId) OR (friendship.friend_id= :userId AND friendship.user_id= :friendId)", nativeQuery = true)
    Friendship findFriendByUserIdAndFriendId(@Param("friendId") int friendId, @Param("userId") int userId);

    @Modifying
    @Query(value = "DELETE FROM `friendship` WHERE ((friendship.friend_id= :friendId AND friendship.user_id= :userId) OR (friendship.friend_id= :userId AND friendship.user_id= :friendId)) AND friendship.is_active= :isActive", nativeQuery = true)
    int deleteByUserIdAndFriendId(@Param("friendId") int friendId, @Param("userId") int userId, @Param("isActive") int isActive);

}
