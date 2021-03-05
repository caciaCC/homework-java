package com.db.homework.mapper;


import com.db.homework.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("select * from back_permission where id = #{pid}")
    Permission getPermsByPid(Integer pid);
    //37
    @Select("select * from back_permission")
    List<Permission> getAllPerms();
}
