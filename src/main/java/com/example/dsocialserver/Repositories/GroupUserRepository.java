/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.dsocialserver.Repositories;

import com.example.dsocialserver.Models.GroupUser;
import com.example.dsocialserver.Models.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface GroupUserRepository extends CrudRepository<GroupUser, Object> {

    @Query(value = "SELECT * FROM groupuser WHERE groupuser.group_id=:groupId GROUP BY groupuser.user_id ORDER BY id DESC", nativeQuery = true)
    Page<GroupUser> findAllUserJoinedByGroupId(Pageable pageable, @Param("groupId") int groupId);

    void deleteAllByIdIn(List ids);

    @Query(value = "SELECT * FROM `groupuser` WHERE groupuser.group_id=:groupId AND groupuser.user_id=:userId", nativeQuery = true)
    GroupUser findByIdAndUserId(@Param("groupId") int groupId, @Param("userId") int userId);

    @Modifying
    @Query(value = "DELETE FROM `groupuser` WHERE group_id= :group_id AND user_id= :user_id", nativeQuery = true)
    int deleteByGroupIdAndUserId(@Param("group_id") int group_id, @Param("user_id") int user_id);
}
