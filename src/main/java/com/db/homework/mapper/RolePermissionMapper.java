package com.db.homework.mapper;

import com.db.homework.entity.RolePermission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    @Select("select pid from back_role_permission where rid = #{rid}")
    List<Integer> getPidsByRid(int rid);

    //41
    @Select("select * from back_role_permission where rid = #{rid} and pid = #{pid} limit 1")
    RolePermission containRidPid(@Param("rid") int rid, @Param("pid") int pid);
    //41
    @Insert("insert into back_role_permission (rid,pid) values(#{rid},#{pid})")
    void insertRidPid(@Param("rid") int rid, @Param("pid") int pid);
    //41
    @Delete("delete from back_role_permission where rid = #{rid} and pid = #{pid} limit 1")
    void deleteRidPid(@Param("rid") int rid, @Param("pid") int pid);
}
