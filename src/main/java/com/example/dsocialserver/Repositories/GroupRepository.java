/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Groups;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author haidu
 */
@Repository
public interface GroupRepository extends CrudRepository<Groups, Object> {
    
    @Query("SELECT g FROM Groups g WHERE g.user_id = :userId")
    Page<Groups> findAllByUserId(Pageable pageable, @Param("userId") int userId);
    
    @Query("SELECT g FROM Groups g WHERE g.is_active = 1")
    Page<Groups> findAll(Pageable pageable);
    
    @Modifying
    @Query(value = "DELETE groups, groupuser, post, postimage, postcomment, "
            + "postreaction FROM groups LEFT JOIN groupuser ON groups.id = "
            + "groupuser.group_id LEFT JOIN post ON groups.id = post.group_id "
            + "LEFT JOIN postimage ON post.id = postimage.post_id LEFT JOIN "
            + "postreaction ON post.id = postreaction.post_id LEFT JOIN postcomment "
            + "ON post.id = postcomment.post_id WHERE groups.id = :id", nativeQuery = true)
    void deleteGroups(@Param("id") int id);
}
