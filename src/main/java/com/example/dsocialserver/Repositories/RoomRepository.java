/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.Room;
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
public interface RoomRepository extends CrudRepository<Room, Object>{
        @Query(value="SELECT room.* FROM `roomuser`, room WHERE roomuser.user_id= :userId AND roomuser.room_id=room.id ORDER BY `roomuser`.`id` DESC",nativeQuery = true)
    Page<Room> findRoomByuserId(Pageable pageable, @Param("userId") int userId); 
}
