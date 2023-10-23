/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Types;

import jakarta.validation.constraints.*;

/**
 *
 * @author haidu
 */
public class GroupsType {
    
    @NotEmpty
    @NotBlank
    @NotNull
    public String name;
    
    @NotEmpty
    @NotBlank
    @NotNull
    public String avatar;
    
    @NotEmpty
    @NotBlank
    @NotNull
    public String coverImage;
    
    public int userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    } 
}
