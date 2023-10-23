/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Models;
import jakarta.persistence.*;
import java.util.Date;
/**
 *
 * @author haidu
 */
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSeen;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;

    // Getters and setters
}
