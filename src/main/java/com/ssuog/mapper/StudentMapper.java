package com.ssuog.mapper;

import com.ssuog.model.Student;

public interface StudentMapper {
    int deleteByPrimaryKey(String guid);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(String guid);
    
    Student selectByUsername(String username);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
}