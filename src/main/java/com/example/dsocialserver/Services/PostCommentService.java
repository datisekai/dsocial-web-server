/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Repositories.PostCommentRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class PostCommentService {

    @Autowired
    private PostCommentRepository commentRepository;

    public Map<String, Object> createPostComment(int postId, int authorId, String content, int parentId) {
        PostComment gr = new PostComment();
        gr.setPost_id(postId);
        gr.setAuthor_id(authorId);
        gr.setContent(content);
        gr.setParent_id(parentId);
        PostComment list = commentRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("post_id", list.getPost_id());
        data.put("parent_id", list.getParent_id());
        data.put("content", list.getContent());
        data.put("author_id", list.getAuthor_id());

        return data;
    }

    public Map<String, Object> updatePostComment(int id, String content) {
        Map<String, Object> data = new HashMap<>();
        Optional<PostComment> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            PostComment gr = optional.get();
            gr.setContent(content);
            // ...
            PostComment list = commentRepository.save(gr);
            data.put("id", list.getId());
            data.put("post_id", list.getPost_id());
            data.put("parent_id", list.getParent_id());
            data.put("content", list.getContent());
            data.put("author_id", list.getAuthor_id());
        }
        return data;
    }

    public boolean deletePostComment(Object id) {
        int result = commentRepository.deletePostComentById(Integer.parseInt((String) id));
        return result == 1;
    }

//    public Page<PostComment> getPostCommentList(int page, int limit, String author_id) {
//        Pageable pageable = PageRequest.of(page, limit);
//        return commentRepository.findAllPostCommentByAuthorId(pageable, Integer.parseInt(author_id));
//    }
}
