/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.GroupUser;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author haidu
 */
@Repository
public interface GroupUserRepository extends CrudRepository<GroupUser, Object>{
    
    void deleteAllByIdIn(List ids);
    
    @Query(value="SELECT * FROM `groupuser` WHERE groupuser.group_id=:groupId AND groupuser.user_id=:userId", nativeQuery = true)
    GroupUser findByIdAndUserId(@Param("groupId") int groupId, @Param("userId") int userId);
    
    @Modifying
    @Query(value = "DELETE FROM `groupuser` WHERE group_id= :group_id AND user_id= :user_id", nativeQuery = true)
    int deleteByGroupIdAndUserId(@Param("group_id") int group_id, @Param("user_id") int user_id);
}
