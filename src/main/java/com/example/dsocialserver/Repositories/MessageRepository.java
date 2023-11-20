/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Message;
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
public interface MessageRepository extends CrudRepository<Message, Object>{
    @Query(value="SELECT * FROM message  WHERE message.author_id = :authorId and message.id= :id ORDER BY `message`.`id` DESC", nativeQuery = true)
    Message findByIdAndUserId( @Param("id") int id,  @Param("authorId") int authorId);
    
    @Query(value="SELECT * FROM `message` WHERE message.content LIKE %:q% AND author_id = :authorId AND receive_id= :receiveId ORDER BY `message`.`id` DESC",nativeQuery = true)
    Page<Message> findMessageByRoomId(Pageable pageable, @Param("authorId") int authorId, @Param("receiveId") int receiveId, @Param("q") String q); 
}
