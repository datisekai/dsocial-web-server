/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Groups;
import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositories.PostCommentRepository;
import com.example.dsocialserver.Repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public PostComment findByIdAndAuthorId(int postCommentId, int authorId) {
        PostComment optional = commentRepository.findByIdAndAuthorId(postCommentId, authorId);
        return optional;
    }

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
        data.put("create_at", list.getCreated_at());
        data.put("user_comment", getUserById(authorId));
        return data;
    }

    public Map<String, Object> updatePostComment(int id, String content, int authorId) {
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
            data.put("create_at", list.getCreated_at());
            data.put("user_comment", getUserById(authorId));
        }
        return data;
    }

    public Map<String, Object> deletePostComment(int id, int authorId) {
        PostComment list = commentRepository.findByIdAndAuthorId(id, authorId);
        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("post_id", list.getPost_id());
        data.put("parent_id", list.getParent_id());
        data.put("content", list.getContent());
        data.put("author_id", list.getAuthor_id());
        data.put("create_at", list.getCreated_at());
        data.put("user_comment", getUserById(authorId));
        commentRepository.deletePostComentById( id);
        return data;
    }

    public Map<String, Object> getUserById(Object userId) {
        Optional<User> optional = userRepository.findById(userId);
        Map<String, Object> data = new HashMap<>();
        if (optional.isPresent()) {
            User user = optional.get();
            data.put("id", user.getId());
            data.put("email", user.getEmail());
            data.put("name", user.getName());
            data.put("avatar", user.getAvatar());
            data.put("bio", user.getBio());
            data.put("birthday", user.getBirthday());
            data.put("cover_image", user.getCover_image());
            data.put("other_name", user.getOther_name());
            data.put("address", user.getAddress());
        }

        return data;
    }

//    public Page<PostComment> getPostCommentList(int page, int limit, String author_id) {
//        Pageable pageable = PageRequest.of(page, limit);
//        return commentRepository.findAllPostCommentByAuthorId(pageable, Integer.parseInt(author_id));
//    }
}
