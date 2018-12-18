package com.ssuog.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ssuog.mapper.StudentMapper;
import com.ssuog.model.Quiz;
import com.ssuog.model.Student;

@Service
public class StudentService {
	
	@Resource
	StudentMapper studentDao;
	
	@Autowired
	QuizService quizService;
	
	@Transactional(readOnly=true)
	public Student getStudentByGuid(String guid){
		return studentDao.selectByPrimaryKey(guid);
	}
	
	/**
	 * if student not exist,
	 * then insert new student record and insert new quiz record 
	 * @param student
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Student initStudent(Student student){
		//insert new student info
		studentDao.insert(student);
		//insert new quiz info
		Quiz quiz = new Quiz();
		quiz.setScore(-1.0);
		quiz.setGuid(student.getGuid());
		quiz.setQuizavailable(false);
		quiz.setCosttime(0);
		quiz.setUpdatetime(new Date());
		quizService.insert(quiz);
		student.setPassword("");
		return student;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int addNewStudent(Student student){
		return studentDao.insert(student);
	}
}
