package com.db.homework.mapper;

import com.db.homework.entity.UserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    @Select("select rid from back_user_role where uid = #{uid}")
    List<Integer> getRidsByUid(int uid);
    //39
    @Select("select * from back_user_role where uid = #{uid} and rid = #{rid} limit 1")
    UserRole containUidRid(@Param("uid") int uid, @Param("rid") int rid);
    //39
    @Insert("insert into back_user_role (uid,rid) values(#{uid},#{rid})")
    void insertUidRid(@Param("uid") int uid, @Param("rid") int rid);
    @Delete("delete from back_user_role where uid = #{uid} and rid = #{rid} limit 1")
    void deleteUidRid(@Param("uid") int uid, @Param("rid") int rid);
}
