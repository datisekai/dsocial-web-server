/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Types.PostType;
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

    @Query(value = "SELECT post.* FROM post,friendship WHERE post.html LIKE %:name% AND post.group_id = 0 AND (friendship.user_id =post.author_id OR friendship.friend_id =post.author_id) AND friendship.is_active=1 ORDER BY post.id DESC", nativeQuery = true)
    Page<Post> findAllByName(Pageable pageable, @Param("name") String name);

    @Query(value = "SELECT * FROM post WHERE post.author_id = :userId AND post.group_id = 0 ORDER BY post.id DESC", nativeQuery = true)
    Page<Post> findAllByUserId(Pageable pageable, @Param("userId") int userId);

    @Query(value = "SELECT * FROM post WHERE post.group_id = :groupId ORDER BY post.id DESC", nativeQuery = true)
    Page<Post> findAllByGroupId(Pageable pageable, @Param("groupId") int groupId);

    @Query(value = "SELECT * FROM Post WHERE Post.is_active = 1 ORDER BY post.id DESC", nativeQuery = true)
    Page<Post> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM Post WHERE post.id = :postId AND post.author_id = :userId ORDER BY post.id DESC", nativeQuery = true)
    Post findByIdAndAuthorId(@Param("postId") int postId, @Param("userId") int userId);

    @Query(value = "SELECT post.* FROM Post,(SELECT groups.id FROM groups WHERE groups.user_id= :userId) AS temp WHERE post.id = :postId AND post.group_id= temp.id  ORDER BY post.id DESC", nativeQuery = true)
    Post findByUserIdBoss(@Param("postId") int postId, @Param("userId") int userId);
    
    @Modifying
    @Query(value = "DELETE post, postimage, postreaction, postcomment "
            + "FROM post LEFT JOIN postimage ON post.id = postimage.post_id "
            + "LEFT JOIN postreaction ON post.id = postreaction.post_id LEFT "
            + "JOIN postcomment ON post.id = postcomment.post_id WHERE post.id = :id", nativeQuery = true)

    int deletePost(@Param("id") int id);
}
