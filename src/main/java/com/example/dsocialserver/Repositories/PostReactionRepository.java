/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.PostReaction;
import java.util.List;
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
public interface PostReactionRepository extends CrudRepository<PostReaction, Object>{
    @Modifying
    @Query(value = "DELETE FROM postreaction WHERE postreaction.id= :id", nativeQuery = true)
    int deletePostReactionById(@Param("id") int id);  
//    
//    @Query(value="SELECT postreaction.* FROM postreaction,(SELECT * FROM post WHERE post.id = :postId) as temp WHERE temp.id = postreaction.post_id ORDER BY postreaction.id DESC", nativeQuery = true)
//    List<PostReaction> findAllPostReactionByPostId(@Param("postId") int postId);
}
