/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author haidu
 */
@Data
@Entity
@Table(name = "user")
public class User {

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;
    
    @Column(length = 255)
    private String password;

    @Column(nullable = false)
    private String name;

    private String other_name;
    private String bio;
    private Date birthday;

    @Column(nullable = false)
    private String avatar;
    
    private String cover_image;
    private int is_active;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated_at;
    
    @OneToMany(mappedBy = "user_postComments", fetch = FetchType.LAZY)
    private List<PostComment> postComments = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_postReactions", fetch = FetchType.LAZY)
    private List<PostReaction> postReactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_posts", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_groupUsers", fetch = FetchType.LAZY)
    private List<GroupUser> groupUsers = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_user_friendships", fetch = FetchType.LAZY)
    private List<Friendship> user_friendships = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_friend_friendships", fetch = FetchType.LAZY)
    private List<Friendship> friend_friendships = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_messages", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_messageReactions", fetch = FetchType.LAZY)
    private List<MessageReaction> messageReactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user_roomUsers", fetch = FetchType.LAZY)
    private List<RoomUser> roomUsers = new ArrayList<>();
    
//    @OneToOne(mappedBy = "user")
//    private Groups groups;
    // Getters and setters
    // Constructors
    // Other helper methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
   
}
