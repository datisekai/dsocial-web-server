/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Repositories.MessageRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author haidu
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    
    public Message findByIdAndUserId(int groupId, int userId) {
        Message optional = messageRepository.findByIdAndUserId(groupId, userId);
        return optional;
    }
    
    public Map<String, Object> createMessage(int authorId, int roomId, String content) {
        Message gr = new Message();
        gr.setAuthor_id(authorId);
        gr.setRoom_id(roomId);
        gr.setContent(content);
        gr.setIs_active(true);
        gr.setIs_seen(false);

        Message list = messageRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("author_id", list.getAuthor_id());
        data.put("room_id", list.getRoom_id());
        data.put("content", list.getContent());
        data.put("is_active", list.getIs_active());
        data.put("is_seen", list.getIs_seen());

        return data;
    }

    public Map<String, Object> updateMessage(String content, Object id) {
        Map<String, Object> data = new HashMap<>();
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isPresent()) {
            Message gr = optional.get();
            gr.setContent(content);
            // ...
            Message list = messageRepository.save(gr);

            data.put("id", list.getId());
            data.put("author_id", list.getAuthor_id());
            data.put("room_id", list.getRoom_id());
            data.put("content", list.getContent());
            data.put("is_active", list.getIs_active());
            data.put("is_seen", list.getIs_seen());
        }
        return data;
    }

    public boolean revokeMessage(Object id) {
        int result = 0;
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isPresent()) {
            Message gr = optional.get();
            gr.setIs_active(false);
            // ...
            Message list = messageRepository.save(gr);
            if (list != null) {
                result = 1;
            }
        }
        return result == 1;
    }
}
