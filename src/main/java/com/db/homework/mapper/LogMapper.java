package com.db.homework.mapper;


import com.db.homework.entity.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    //44
    @Insert("insert into log (modul,type,descp,method,uri,ip,time) values(#{modul},#{type},#{descp},#{method},#{uri},#{ip},#{time})")
    void insert(@Param("modul") String modul,@Param("type") String type, @Param("descp")String descp,
                @Param("method")String method, @Param("uri")String uri, @Param("ip")String ip, @Param("time")String time);

    //47
//    ORDER BY STR_TO_DATE(concat(date,'-30'),'%Y-%m-%d')
    @Select("select * from log order by str_to_date(time,'%Y-%m-%d %H:%i:%s') desc ")
    List<Log> getTotalList();
}
