package com.db.homework.mapper;

import com.db.homework.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    //9
    @Select("select * from student where sno = #{sno} and password = #{password} limit 1")
    Student getOneBySnoAndPassword(@Param("sno")String sno, @Param("password")String password);
    //9
    @Update("update student set registered = #{registered} where id = #{id}")
    void updateRegistered(@Param("id") int id, @Param("registered") Boolean registered);
    //9
    @Update("update student set card_no = #{cardNo} where id = #{id} limit 1")
    void updateCardNo(@Param("id") int id, @Param("cardNo") String cardNo);
    //22
    @Select("select * from student where card_no = #{cardNo} limit 1")
    @Results(id = "student",value = {
            @Result(column = "card_no",property = "cardNo")
    })
    Student getByCardNo(String cardNo);
    //29
    @Select("select s.* from student s")
    @ResultMap(value = {"student"})
    List<Student> getTotalList();
    //32

    @Insert("insert into student (sno,password,salt,name,department,phone,registered) values(#{sno},#{password},#{salt},#{name},#{department},#{phone},#{registered})")
    void addStudent(@Param("sno") String sno, @Param("password") String password, @Param("salt") String salt,
                    @Param("name") String name, @Param("department") String department, @Param("phone") String phone, @Param("registered")boolean registered);

    //30
    @Select("select * from student where id = #{id} limit 1")
    @ResultMap(value = {"student"})
    Student getById(int id);
    //36
    @Update("update student set registered = 0 where card_no=#{cardNo} limit 1")
    void updateAllCardNo(String cardNo);
}
