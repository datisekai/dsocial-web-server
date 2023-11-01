/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Repositories.MessageRepository;
import static com.example.dsocialserver.Services.UserService.getUser;
import static com.example.dsocialserver.Utils.Pagination.getPagination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Map<String, Object> getAllMessage(int page, int limit, int authorId, String q) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Message> list = messageRepository.findMessageByRoomId(pageable, authorId, q);
        return reponsDataMessage(page, list);
    }

    public Map<String, Object> reponsDataMessage(int page, Page<Message> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Message o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();

            data.put("id", o.getId());
            data.put("author_id", o.getAuthor_id());
            data.put("room_id", o.getRoom_id());
            data.put("content", o.getContent());
            data.put("is_active", o.getIs_active());
            data.put("is_seen", o.getIs_seen());
            data.put("user_send", getUser(o.getUser_messages()));
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }
}
