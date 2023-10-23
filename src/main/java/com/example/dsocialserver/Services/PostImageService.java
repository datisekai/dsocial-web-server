/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Repositories.PostImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class PostImageService {
//
//    @Autowired
//    private PostImageRepository postImageRepository;
//
//    public PostImage findById(Object id) {
//        Optional<PostImage> optional = postImageRepository.findById(id);
//        Post list = null;
//        if (optional.isPresent()) {
//            list = optional.get();
//        }
//        return list;
//    }
//
//    public Post createPost(String html, int authorid) {
//        Post po = new Post();
//        po.setHtml(html);
//        po.setAuthor_id(authorid);
//        po.setGroup_id(0);
//        po.setIs_active(1);
//        return postImageRepository.save(po);
//    }
//
//    public Post updatePost(Object id, String html) {
//        Optional<PostImage> optional = postImageRepository.findById(id);
//        Post list = null;
//        if (optional.isPresent()) {
//            Post po = optional.get();
//            // Cập nhật các trường của đối tượng user
//            po.setHtml(html);
//            // ...
//            list = postImageRepository.save(po);
//        }
//        return list;
//    }
//
//    public Post deletePostById(Object id) {
//        Optional<PostImage> optional = postImageRepository.findById(id);
//        Post list = null;
//        if (optional.isPresent()) {
//            Post po = optional.get();
//            // Cập nhật các trường của đối tượng user
//            po.setIs_active(0);
//            // ...
//            list = postImageRepository.save(po);
//        }
//        return list;
//    }
//
//    public Page<PostImage> getPostList(int page, int limit) {
//        Pageable pageable = PageRequest.of(page, limit);
//        return postImageRepository.findAll(pageable);
//    }
    
}
