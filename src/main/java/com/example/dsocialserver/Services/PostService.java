/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Repositories.PostRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post findById(Object id) {
        Optional<Post> optional = postRepository.findById(id);
        Post list = null;
        if (optional.isPresent()) {
            list = optional.get();
        }
        return list;
    }

    public Post createPost(String html, int authorid, int groupid) {
        Post po = new Post();
        po.setHtml(html);
        po.setAuthor_id(authorid);
        po.setGroup_id(groupid);
        po.setIs_active(1);
        return postRepository.save(po);
    }

    public Post updatePost(int id, String html, int authorId, int groupId) {
        Optional<Post> optional = postRepository.findById(id);
        Post list = null;
        if (optional.isPresent()) {
            Post po = optional.get();
            // Cập nhật các trường của đối tượng user
            po.setHtml(html);
            po.setAuthor_id(authorId);
            po.setGroup_id(groupId);
            // ...
            list = postRepository.save(po);
        }
        return list;
    }

    public Post deletePostById(int id) {
        Optional<Post> optional = postRepository.findById(id);
        Post list = null;
        if (optional.isPresent()) {
            Post po = optional.get();
            // Cập nhật các trường của đối tượng user
            po.setIs_active(0);
            // ...
            list = postRepository.save(po);
        }
        return list;
    }

    public Page<Post> getPostList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return postRepository.findAll(pageable);
    }
    
}
