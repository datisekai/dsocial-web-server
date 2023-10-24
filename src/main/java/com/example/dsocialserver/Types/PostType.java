/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Types;

import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Models.PostReaction;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author haidu
 */
public class PostType {

    public String id;
    
    @NotEmpty
    @NotBlank
    @NotNull
    public String html;
    
    public String groupId;
    
    public String userId;
    
    public List<PostImage> image;
    
    public List<PostReaction> reaction;
    
    public List<PostComment> comment;

    public Date createdAt;
    
    public Date updatedAt;
    
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    

    public List<PostImage> getImage() {
        return image;
    }

    public void setImage(List<PostImage> image) {
        this.image = image;
    }

    public List<PostReaction> getReaction() {
        return reaction;
    }

    public void setReaction(List<PostReaction> reaction) {
        this.reaction = reaction;
    }

    public List<PostComment> getComment() {
        return comment;
    }

    public void setComment(List<PostComment> comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
