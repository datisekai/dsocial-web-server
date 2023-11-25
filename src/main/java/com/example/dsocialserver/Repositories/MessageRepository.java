/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Message;
import com.example.dsocialserver.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author haidu
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Object> {

    @Query(value = "SELECT * FROM message  WHERE message.author_id = :authorId and message.id= :id ORDER BY `message`.`id` DESC", nativeQuery = true)
    Message findByIdAndUserId(@Param("id") int id, @Param("authorId") int authorId);

    @Query(value = "SELECT * FROM `message` WHERE message.content LIKE %:q% AND author_id = :authorId AND receive_id= :receiveId ORDER BY `message`.`id` ASC", nativeQuery = true)
    Page<Message> findMessage(Pageable pageable, @Param("authorId") int authorId, @Param("receiveId") int receiveId, @Param("q") String q);

    @Query(value = "SELECT temp.last_message FROM ( SELECT subquery.user_id, MAX(message.content) AS last_message FROM ( SELECT receive_id AS user_id FROM message WHERE author_id = :authorId OR receive_id = :authorId UNION SELECT author_id AS user_id FROM message WHERE author_id = :authorId OR receive_id = :authorId ) AS subquery JOIN message ON (subquery.user_id = message.receive_id OR subquery.user_id = message.author_id) WHERE subquery.user_id <> :authorId GROUP BY subquery.user_id ) AS temp ORDER BY temp.user_id DESC;", nativeQuery = true)
    Page<String> findLastMessage(Pageable pageable, @Param("authorId") int authorId);
}
