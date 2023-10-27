/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.PostImage;
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
public interface PostImageRepository extends CrudRepository<PostImage, Object> {
//    @Query(value = "SELECT * FROM postimage WHERE postimage.post_id = :postId ORDER BY postimage.id DESC", nativeQuery = true)
//    List<PostImage> findAllPostImageByPostId(@Param("postId") int postId);
}
