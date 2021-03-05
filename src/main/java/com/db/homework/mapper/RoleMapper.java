package com.db.homework.mapper;

import com.db.homework.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    //34
    @Select("select * from back_role")
    @Results(id = "role",value = {
            @Result(column = "name_cn",property = "nameCn"),
    })
    List<Role> getAllRoles();
    @Select("select * from back_role where id = #{id} limit 1")
    @ResultMap(value = {"role"})
    Role getRoleByRid(Integer id);
    //40
    @Insert("insert into back_role (name,name_cn,enabled,des) values(#{name},#{nameCn},#{status},#{des})")
    void add(@Param("name") String name,@Param("nameCn")  String nameCn, @Param("status") boolean status, @Param("des") String des);

    @Update("update back_role set name=#{name},name_cn=#{nameCn},enabled=#{status},des=#{des} where id = #{id} limit 1")
    void update(@Param("id") int id, @Param("name") String name,@Param("nameCn")  String nameCn, @Param("status") boolean status, @Param("des") String des);
    //43
    @Delete("delete from back_role where id = #{id} limit 1")
    void deleteRole(int id);
}
