package com.db.homework.mapper;

import com.db.homework.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {
    @Select("select * from category where id= #{id}")
    public Category getCategoryById(int id);
}
