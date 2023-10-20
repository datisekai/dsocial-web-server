/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Repositorys.PostRepository;
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
        Optional<Post> optionalUser = postRepository.findById(id);
        Post post = optionalUser.get();
        return post;
    }

    public Post createGroup(String html, int authorid, int groupid) {
        Post po = new Post();
        po.setHtml(html);
        po.setAuthorid(authorid);
        po.setGroupid(groupid);
        po.setIsactive(1);
        return postRepository.save(po);
    }

    public Post updateGroup(int id, String html, int authorId, int groupId) {
        Optional<Post> optional = postRepository.findById(id);
        Post list = null;
        if (optional.isPresent()) {
            Post po = optional.get();
            // Cập nhật các trường của đối tượng user
            po.setHtml(html);
            po.setAuthorid(authorId);
            po.setGroupid(groupId);
            // ...
            list = postRepository.save(po);
        }
        return list;
    }

    public Post deleteGroupById(int id) {
        Optional<Post> optionalGroup = postRepository.findById(id);
        Post list = null;
        if (optionalGroup.isPresent()) {
            Post po = optionalGroup.get();
            // Cập nhật các trường của đối tượng user
            po.setIsactive(0);
            // ...
            list = postRepository.save(po);
        }
        return list;
    }

    public Page<Post> getGroupList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return postRepository.findByIsactiveNot(pageable, 0);
    }
}
