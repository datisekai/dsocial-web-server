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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * @author haidu
 */
@Data
@Entity
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, insertable=false, updatable=false)
    private int user_id;

    @Column(nullable = false, insertable=false, updatable=false)
    private int friend_id;

    private int is_Active;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    private User user_user_friendships;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id", insertable=false, updatable=false)
    private User user_friend_friendships;
    // Getters and setters
    // Constructors
    // Other helper methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getIs_Active() {
        return is_Active;
    }

    public void setIs_Active(int is_Active) {
        this.is_Active = is_Active;
    }

}
