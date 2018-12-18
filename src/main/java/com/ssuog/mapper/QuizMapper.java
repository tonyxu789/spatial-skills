package com.ssuog.mapper;

import java.util.List;
import java.util.Map;

import com.ssuog.model.Quiz;

public interface QuizMapper {
    int deleteByPrimaryKey(String guid);

    int insert(Quiz record);

    int insertSelective(Quiz record);

    Quiz selectByPrimaryKey(String guid);
    
    List<Quiz> listQuizPages(Map<String, Object> param);
    
    int countQuizRecords();
    
    int updateQuizavailabeByGuid(Map<String, Object> param);

    int updateByPrimaryKeySelective(Quiz record);

    int updateByPrimaryKey(Quiz record);
}