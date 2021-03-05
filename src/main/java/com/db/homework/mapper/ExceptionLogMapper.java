package com.db.homework.mapper;


import com.db.homework.entity.ExceptionLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExceptionLogMapper {
    @Insert("insert into exception_log (name,message,method,uri,ip,time) values(#{name},#{message},#{method},#{uri},#{ip},#{time})")
    void insert(@Param("name") String name, @Param("message") String message,@Param("method") String method,
                @Param("uri") String uri, @Param("ip") String ip, @Param("time") String time);
    //48
    @Select("select * from exception_log order by str_to_date(time,'%Y-%m-%d %H:%i:%s') desc ")
    List<ExceptionLog> getTotalList();
}
