/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Room;
import com.example.dsocialserver.Models.RoomUser;
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
public interface RoomUserReponsiitory extends CrudRepository<RoomUser, Object> {  
    
    @Query(value = "SELECT * FROM roomuser WHERE (roomuser.user_id = :userId OR roomuser.user_id = :friendId) AND roomuser.room_id = :roomId GROUP BY room_id ORDER BY roomuser.id DESC ", nativeQuery = true)
    RoomUser findByUserId(@Param("userId") int userId, @Param("friendId") int friendId, @Param("roomId") int roomId);
}
