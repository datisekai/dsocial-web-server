/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.PostReaction;
import com.example.dsocialserver.Repositories.PostReactionRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class PostReactionService {

    @Autowired
    private PostReactionRepository postReactionRepository;

    public PostReaction findByIdAndAuthorId(int postReactionId, int authorId){
        PostReaction optional = postReactionRepository.findByIdAndAuthorId(postReactionId, authorId);
        return optional;
    }
    
    public Map<String, Object> createPostReaction(int postId, int authorId, String icon) {
        PostReaction gr = new PostReaction();
        gr.setPost_id(postId);
        gr.setAuthor_id(authorId);
        gr.setIcon(icon);
        PostReaction list = postReactionRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("post_id", list.getPost_id());
        data.put("icon", list.getIcon());
        data.put("author_id", list.getAuthor_id());

        return data;
    }

    public Map<String, Object> updatePostReaction(int id, String icon) {
        Map<String, Object> data = new HashMap<>();
        Optional<PostReaction> optional = postReactionRepository.findById(id);
        if (optional.isPresent()) {
            PostReaction gr = optional.get();
            gr.setIcon(icon);
            // ...
            PostReaction list = postReactionRepository.save(gr);
            data.put("id", list.getId());
            data.put("post_id", list.getPost_id());
            data.put("icon", list.getIcon());
            data.put("author_id", list.getAuthor_id());
        }
        return data;
    }

    public boolean deletePostReaction(Object id) {
        int result = postReactionRepository.deletePostReactionById(Integer.parseInt((String) id));
        return result == 1;
    }
}
