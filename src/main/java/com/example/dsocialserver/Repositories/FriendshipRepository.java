/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Friendship;
import com.example.dsocialserver.Models.User;
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
public interface FriendshipRepository extends CrudRepository<Friendship, Object>{
    
    @Query(value="SELECT * FROM friendship WHERE (friendship.user_id = :userId OR friendship.friend_id = :userId) AND friendship.is_active= :isActive ORDER BY friendship.id DESC", nativeQuery = true)
    Page<Friendship> findAllFriendshipByUserId(Pageable pageable, @Param("userId") int userId, @Param("isActive") int isActive);
    
    @Query(value="SELECT * FROM friendship WHERE (friendship.friend_id= :friendId AND friendship.user_id= :userId) OR (friendship.friend_id= :userId AND friendship.user_id= :friendId)", nativeQuery = true)
    Friendship findFriendByUserIdAndFriendId(@Param("friendId") int friendId, @Param("userId") int userId);
    
    @Modifying
    @Query(value="DELETE FROM `friendship` WHERE ((friendship.friend_id= :friendId AND friendship.user_id= :userId) OR (friendship.friend_id= :userId AND friendship.user_id= :friendId)) AND friendship.is_active= :isActive", nativeQuery = true)
    int deleteByUserIdAndFriendId(@Param("friendId") int friendId, @Param("userId") int userId, @Param("isActive") int isActive);
    
}
