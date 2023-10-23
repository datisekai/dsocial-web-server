/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.PostComment;
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
public interface PostCommentRepository extends CrudRepository<PostComment, Object> {
    
    @Query("SELECT pc FROM PostComment pc WHERE pc.author_id = :authorId")
Page<PostComment> findAllPostCommentByAuthorId(Pageable pageable, @Param("authorId") int authorId);
    
    @Modifying
    @Query(value = "DELETE FROM postcomment WHERE postcomment.id= :id", nativeQuery = true)
    void deletePostComentById(@Param("id") int id);
}
