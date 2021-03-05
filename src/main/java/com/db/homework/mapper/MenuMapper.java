package com.db.homework.mapper;


import com.db.homework.entity.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("select * from back_menu where id = #{mid}")
    @Results(id = "back_menu",value = {
            @Result(column = "router_path",property = "path"),
            @Result(column = "name_cn",property = "nameCn"),
            @Result(column = "component_path",property = "component"),
    })
    Menu getMenuByMid(Integer mid);
    @Select("select * from back_menu where pid = #{pid}")
    @ResultMap(value = {"back_menu"})
    List<Menu> getMenusByPid(int pid);
    //38
    @Select("select * from back_menu")
    @ResultMap(value = {"back_menu"})
    List<Menu> getAllMenus();
}
