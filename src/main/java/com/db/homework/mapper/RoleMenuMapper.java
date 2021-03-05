package com.db.homework.mapper;


import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMenuMapper {
    @Select("select mid from back_role_menu where rid = #{rid}")
    List<Integer> getMidsByRid(Integer rid);
    //42
    @Delete("delete from back_role_menu where rid = #{rid}")
    void deleteRidMidsByRid(int rid);
    //42
    @Insert("insert into back_role_menu (rid,mid) values(#{rid},#{mid})")
    void insertRidMid(@Param("rid") int rid, @Param("mid") int mid);
}
