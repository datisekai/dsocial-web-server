/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Post;
import java.util.List;
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
public interface PostRepository extends CrudRepository<Post, Object> {
    
    @Query(value="SELECT * FROM post WHERE post.author_id = :id", nativeQuery = true)
    Page<Post> findAllByUserId(Pageable pageable, @Param("id") int id);
    
    @Query(value="SELECT * FROM post WHERE post.group_id = :id", nativeQuery = true)
    Page<Post> findAllByGroupId(Pageable pageable, @Param("id") int id);

    @Query(value="SELECT * FROM post WHERE post.is_active = 1", nativeQuery = true)
    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE post, postimage, postreaction, postcomment "
            + "FROM post LEFT JOIN postimage ON post.id = postimage.post_id "
            + "LEFT JOIN postreaction ON post.id = postreaction.post_id LEFT "
            + "JOIN postcomment ON post.id = postcomment.post_id WHERE post.id = :id", nativeQuery = true)
    void deletePost(@Param("id") int id);
}
