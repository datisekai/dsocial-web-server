/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Friendship;
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
public interface FriendshipRepository extends CrudRepository<Friendship, Object>{
    @Query(value="SELECT * FROM friendship,(SELECT * FROM user WHERE user.name LIKE '% :name %' AND user.id != :userId) as temp WHERE (temp.id = friendship.user_id  OR temp.id= friendship.friend_id) AND friendship.is_active=1", nativeQuery = true)
    Page<Friendship> findAllSearchFriendshipByUserId(Pageable pageable, @Param("userId") int userId, @Param("name") String name);
    
    @Query(value="SELECT * FROM friendship WHERE friendship.user_id = :userId AND friendship.is_active=1", nativeQuery = true)
    Page<Friendship> findAllFriendshipByUserId(Pageable pageable, @Param("userId") int userId);
    
    @Query(value="SELECT * FROM friendship WHERE friendship.user_id = :userId AND friendship.is_active=0", nativeQuery = true)
    Page<Friendship> findAllFriendshipRequestByUserId(Pageable pageable, @Param("userId") int userId);
}
