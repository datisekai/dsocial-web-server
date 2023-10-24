/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import static com.example.dsocialserver.Models.Pagination.getPagination;
import com.example.dsocialserver.Models.Post;
import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Models.PostReaction;
import com.example.dsocialserver.Repositories.PostCommentRepository;
import com.example.dsocialserver.Repositories.PostImageRepository;
import com.example.dsocialserver.Repositories.PostReactionRepository;
import com.example.dsocialserver.Repositories.PostRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author haidu
 */
@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    public Post findById(Object id) {
        Optional<Post> optional = postRepository.findById(id);
        Post list = null;
        if (optional.isPresent()) {
            list = optional.get();
        }
        return list;
    }

    public Map<String, Object> createPost(String html, int authorid, int group_id, List<PostImage> image) {
        Post po = new Post();
        po.setHtml(html);
        po.setAuthor_id(authorid);
        po.setGroup_id(group_id);
        po.setIs_active(1);
        Post list = postRepository.save(po);

        List<PostImage> listPostImage = new ArrayList<>();
        for (PostImage o : image) {
            PostImage pi = new PostImage();
            pi.setPost_id(list.getId());
            pi.setSrc(o.getSrc());
            listPostImage.add(pi);
        }
        postImageRepository.saveAll(listPostImage);
        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("html", list.getHtml());
        data.put("author_id", list.getAuthor_id());
        data.put("group_id", list.getGroup_id());
        data.put("image", listPostImage);

        return data;
    }

    public Map<String, Object> updatePost(Object id, String html, List<PostImage> image) {
        Optional<Post> optional = postRepository.findById(id);
        Map<String, Object> data = new HashMap<>();
        if (optional.isPresent()) {
            Post po = optional.get();
            // Cập nhật các trường của đối tượng bài viết
            po.setHtml(html);
            // ...
            Post updatedPost = postRepository.save(po);

            List<PostImage> updatedPostImages = new ArrayList<>();
            for (PostImage o : image) {
                Optional<PostImage> optionalPostImage = postImageRepository.findById(o.getId());
                if (optionalPostImage.isPresent()) {
                    PostImage existingPostImage = optionalPostImage.get();
                    existingPostImage.setSrc(o.getSrc());
                    updatedPostImages.add(existingPostImage);
                }
            }
            postImageRepository.saveAll(updatedPostImages);

            data.put("id", updatedPost.getId());
            data.put("html", updatedPost.getHtml());
            data.put("author_id", updatedPost.getAuthor_id());
            data.put("group_id", updatedPost.getGroup_id());
            data.put("image", image);
        }
        return data;
    }

    public boolean deletePost(Object id) {
//        try {
        postRepository.deletePost(Integer.parseInt((String) id));
        return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }

    }

    public Map<String, Object> getPostListUser(int page, int limit, int userId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Post> list = postRepository.findAllByUserId(pageable, userId);
        return reponsDataPost(page, limit, list);
    }

    public Map<String, Object> getPostListGroup(int page, int limit, int groupId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Post> list = postRepository.findAllByGroupId(pageable, groupId);
        return reponsDataPost(page, limit, list);
    }

    public Map<String, Object> getPostList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Post> list = postRepository.findAll(pageable);

        return reponsDataPost(page, limit, list);
    }

    public Map<String, Object> reponsDataPost(int page, int limit, Page<Post> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();

        for (Post o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();
            List<PostImage> listPostImage = postImageRepository.findAllPostImageByPostId(o.getId());
            List<PostReaction> listPostreaction = postReactionRepository.findAllPostReactionByPostId(o.getId());
            List<PostComment> listPostComment = postCommentRepository.findAllPostCommentByPostId(o.getId());

            data.put("id", o.getId());
            data.put("html", o.getHtml());
            data.put("author_id", o.getAuthor_id());
            data.put("group_id", o.getGroup_id());
            data.put("created_at", o.getCreated_at());
            data.put("image", listPostImage);
            data.put("count_reaction", listPostreaction.size());
            data.put("count_comment", listPostComment.size());
            data.put("reaction", listPostreaction);
            data.put("comment", listPostComment);
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

}
