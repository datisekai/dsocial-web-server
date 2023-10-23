/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Post;
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
    
    @Query("SELECT p FROM Post p WHERE p.author_id = :id")
    Page<Post> findAllByUserId(Pageable pageable, @Param("id") int id);
    
    @Query("SELECT p FROM Post p WHERE p.group_id = :id")
    Page<Post> findAllByGroupId(Pageable pageable, @Param("id") int id);

    @Query("SELECT p FROM Post p WHERE p.is_active = 1")
    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE postimage, post, postreaction, postcomment FROM postimage "
            + "JOIN post ON postimage.post_id = post.id JOIN postreaction "
            + "ON postreaction.post_id = post.id JOIN postcomment ON "
            + "postcomment.post_id = post.id WHERE post.id = :id", nativeQuery = true)
    void deletePostAndPostImage(@Param("id") int id);
}
