package com.ssuog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ssuog.mapper.QuizMapper;
import com.ssuog.model.Quiz;

@Service
public class QuizService {
	
	@Resource
	QuizMapper quizDao;
	
	/**
	 * select quiz by guid for student
	 * @param guid
	 * @return
	 */
	@Transactional(readOnly = true)
	public Quiz getQuizByGuid(String guid){
		return quizDao.selectByPrimaryKey(guid);
	}
	
	/**
	 * select quizs by offset and limit for admin
	 * @param guid
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Quiz> listQuizPages(int offset, int limit){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("offset", offset);
		param.put("limit", limit);
		return quizDao.listQuizPages(param);
	}
	
	/**
	 * count quiz records number
	 * @param guid
	 * @return
	 */
	@Transactional(readOnly = true)
	public int countQuizRecords(){
		return quizDao.countQuizRecords();
	}
	
	/**
	 * update set quizavailabe for quiz by guid
	 * @param guid
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateQuizavailabeByGuid(String guid, boolean quizavailable){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("quizavailable", quizavailable);
		param.put("guid", guid);
		return quizDao.updateQuizavailabeByGuid(param);
	}
	
	/**
	 * update set quiz for student
	 * @param guid
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateQuizForStudent(Quiz quiz){
		return quizDao.updateByPrimaryKey(quiz);
	}
	
	/**
	 * insert quiz record
	 * @param quiz
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int insert(Quiz quiz){
		return quizDao.insert(quiz);
	}
}
