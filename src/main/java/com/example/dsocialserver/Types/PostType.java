/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Types;

import com.example.dsocialserver.Models.PostComment;
import com.example.dsocialserver.Models.PostImage;
import com.example.dsocialserver.Models.PostReaction;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 *
 * @author haidu
 */
public class PostType {

    @NotEmpty
    @NotBlank
    @NotNull
    public String html;

    public int authorId;
    
    public int groupId;
    
    public List<PostImage> image;
    
    public List<PostReaction> reaction;
    
    public List<PostComment> comment;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
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
    
}
