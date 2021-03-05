package com.db.homework.mapper;

import com.db.homework.entity.Role;
import com.db.homework.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    //8
    @Select("select * from user where card_no = #{card_no} and password = #{password} limit 1")
    @Results(id = "user",value = {
            @Result(column = "card_no",property = "cardNo"),
            @Result(column = "last_time",property = "lastTime"),
    })
    public User getOneByCardnoPassword(@Param("card_no")String cardNo, @Param("password")String password);
    //9  //10
    @Select("select * from user where card_no = #{cardNo} limit 1")
    @ResultMap(value = {"user"})
    User getOneByCardno(String cardNo);
    //9
    @Insert("insert into user (card_no,password,salt,enabled,status,last_time) values(#{card_no},#{password},#{salt},#{enabled},#{status},#{lastTime})")
    void addOne(@Param("card_no") String cardNo, @Param("password") String password,
                @Param("salt") String salt, @Param("enabled") Boolean enabled, @Param("status") Boolean status,@Param("lastTime") String lastTime);
    //10
    @Select("select * from user where card_no = #{cardNo} limit 1")
    @ResultMap(value = {"user"})
    User getStatus(String cardNo);

    //8
    @Update("update user set status = #{status},last_time = #{last_time} where card_no = #{card_no}")
    void setStatus(@Param("card_no") String cardNo, @Param("status") Boolean status, @Param("last_time") String lastTime);
    //31
    @Delete("delete from user where card_no = #{cardNo} limit 1")
    void deleteCardNo(String cardNo);
    //33
    @Select("select * from user")
    @ResultMap(value = {"user"})
    List<User> getAllUsers();

}
