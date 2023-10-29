/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.PostComment;
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
public interface PostCommentRepository extends CrudRepository<PostComment, Object> {

//    @Query(value="SELECT * FROM postcomment WHERE postcomment.author_id = :authorId ORDER BY postcomment.id DESC", nativeQuery = true)
//    Page<PostComment> findAllPostCommentByAuthorId(Pageable pageable, @Param("authorId") int authorId);

    @Modifying
    @Query(value = "DELETE FROM postcomment WHERE postcomment.id= :id", nativeQuery = true)
    int deletePostComentById(@Param("id") int id);
    
    @Query(value="SELECT * FROM postcomment  WHERE postcomment.author_id = :userId and postcomment.id= :id ORDER BY `postcomment`.`id` DESC", nativeQuery = true)
    PostComment findByIdAndAuthorId( @Param("id") int id,  @Param("userId") int userId);
    
//    @Query(value = "SELECT postcomment.* FROM postcomment,(SELECT * FROM post WHERE post.id = :postId) as temp WHERE temp.id = postcomment.post_id ORDER BY postcomment.id DESC", nativeQuery = true)
//    List<PostComment> findAllPostCommentByPostId(@Param("postId") int postId);
}
