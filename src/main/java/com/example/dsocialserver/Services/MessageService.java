/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Services;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Models.User;
import com.example.dsocialserver.Repositories.MessageRepository;
import com.example.dsocialserver.Repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public Message findByIdAndUserId(int messageId, int authorId) {
        Message optional = messageRepository.findByIdAndUserId(messageId, authorId);
        return optional;
    }

    public Map<String, Object> createMessage(int authorId, int receiveId, String content, String type) {
        Message gr = new Message();
        gr.setAuthor_id(authorId);
        gr.setReceive_id(receiveId);
        gr.setContent(content);
        gr.setType(type);
        gr.setIs_active(true);
        gr.setIs_seen(false);

        Message list = messageRepository.save(gr);

        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("author_id", list.getAuthor_id());
        data.put("receive_id", getUserById(list.getReceive_id()));
        data.put("content", list.getContent());
        data.put("type", list.getType());
        data.put("is_active", list.getIs_active());
        data.put("is_seen", list.getIs_seen());
        data.put("created_at", list.getCreated_at());

        return data;
    }

    public Map<String, Object> updateSeenMessage(Object id) {
        Map<String, Object> data = new HashMap<>();
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isPresent()) {
            Message gr = optional.get();
            gr.setIs_seen(true);
            // ...
            Message list = messageRepository.save(gr);

            data.put("id", list.getId());
            data.put("author_id", list.getAuthor_id());
            data.put("receive_id", getUserById(list.getReceive_id()));
            data.put("content", list.getContent());
            data.put("type", list.getType());
            data.put("is_active", list.getIs_active());
            data.put("is_seen", list.getIs_seen());
            data.put("created_at", list.getCreated_at());
        }
        return data;
    }

    public Map<String, Object> updateMessage(String content, Object messageId, String type) {
        Map<String, Object> data = new HashMap<>();
        Optional<Message> optional = messageRepository.findById(messageId);
        if (optional.isPresent()) {
            Message gr = optional.get();
            gr.setContent(content);
            gr.setType(type);
            // ...
            Message list = messageRepository.save(gr);

            data.put("id", list.getId());
            data.put("author_id", list.getAuthor_id());
            data.put("receive_id", getUserById(list.getReceive_id()));
            data.put("content", list.getContent());
            data.put("type", list.getType());
            data.put("is_active", list.getIs_active());
            data.put("is_seen", list.getIs_seen());
            data.put("created_at", list.getCreated_at());
        }
        return data;
    }

    public Map<String, Object> revokeMessage(Object id) {
        Map<String, Object> data = new HashMap<>();
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isPresent()) {
            Message gr = optional.get();
            gr.setIs_active(false);
            // ...
            Message list = messageRepository.save(gr);
            if (list != null) {
                data.put("id", list.getId());
                data.put("author_id", list.getAuthor_id());
                data.put("receive_id", getUserById(list.getReceive_id()));
                data.put("content", list.getContent());
                data.put("type", list.getType());
                data.put("is_active", list.getIs_active());
                data.put("is_seen", list.getIs_seen());
                data.put("created_at", list.getCreated_at());
            }
        }
        return data;
    }

    public Map<String, Object> getUserMessage(int page, int limit, int authorId) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> list = userRepository.findMessageUser(pageable, authorId);
        Page<Message> list2 = messageRepository.findLastMessage(pageable, authorId);
        return reponsUserMessage(page, list, list2);
    }

    public Map<String, Object> getAllMessage(int page, int limit, int authorId, int receiveId, String q) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Message> list = messageRepository.findMessage(pageable, authorId, receiveId, q);
        return reponsDataMessage(page, list);
    }

    public Map<String, Object> reponsUserMessage(int page, Page<User> list, Page<Message> list2) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (User o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();
            data.put("user_send", getUser(o));
            listdata.add(data);
        }
        for (Message o : list2.getContent()) {
            Map<String, Object> data = new HashMap<>();
            data.put("last_message", getMessage(o));
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> reponsDataMessage(int page, Page<Message> list) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        for (Message o : list.getContent()) {
            Map<String, Object> data = new HashMap<>();

            data.put("id", o.getId());
            data.put("author_id", o.getAuthor_id());
            data.put("receive_id", getUserById(o.getReceive_id()));
            if (o.getIs_active()) {
                data.put("content", o.getContent());
            } else {
                data.put("content", "Tin nhắn đã bị thu hồi");
            }
            data.put("type", o.getType());
            data.put("is_active", o.getIs_active());
            data.put("is_seen", o.getIs_seen());
            data.put("created_at", o.getCreated_at());
            data.put("user_send", getUser(o.getUser_messages()));
            listdata.add(data);
        }
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("data", listdata);
        dataResult.put("pagination", getPagination(page, list));

        return dataResult;
    }

    public Map<String, Object> getMessage(Message list) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", list.getId());
        data.put("author_id", list.getAuthor_id());
        data.put("receive_id", getUserById(list.getReceive_id()));
        data.put("content", list.getContent());
        data.put("type", list.getType());
        data.put("is_active", list.getIs_active());
        data.put("is_seen", list.getIs_seen());
        data.put("created_at", list.getCreated_at());
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
}
