package com.ssuog.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssuog.model.Quiz;
import com.ssuog.model.Student;
import com.ssuog.po.EServiceResponseCode;
import com.ssuog.po.UserInfo;
import com.ssuog.po.UserRole;
import com.ssuog.service.QuizService;
import com.ssuog.service.StudentService;
import com.ssuog.util.Response;
import com.ssuog.util.ResponseLst;
import com.ssuog.util.ResponseT;

@Controller
public class SSUogServlet {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private QuizService quizService;
	
	/**
	 *  student register
	 * @param request
	 * @return
	 */
	@RequestMapping("/student/register.do")
	public @ResponseBody ResponseT<UserInfo> register(HttpServletRequest request){
		ResponseT<UserInfo> resp = new ResponseT<UserInfo>(EServiceResponseCode.C200);
		try {
			String guid = request.getParameter("guid");
		    String username = request.getParameter("username");
		    String password = request.getParameter("password");
		    String email = request.getParameter("email");
		    
		    if(StringUtils.isEmpty(guid) 
		    		|| StringUtils.isEmpty(username) 
		    		|| StringUtils.isEmpty(password) 
		    		|| StringUtils.isEmpty(email)) {
		    	resp.valueOf(EServiceResponseCode.C401);
	    		resp.setMessage("register fail! guid or username or password or email is null or empty!");
	    		return resp;
		    }
		    
		    if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
		    	Student student = studentService.getStudentByGuid(guid);
		    	if(student != null){
		    		resp.valueOf(EServiceResponseCode.C401);
					resp.setMessage("register fail! guid：" + guid + " is already exist！");
					return resp;
		    	} else {
		    		student = new Student();
		    		student.setGuid(guid);
		    		student.setUsername(username);
		    		student.setPassword(password);
		    		student.setEmail(email);
		    		studentService.initStudent(student);
		    	}
		    } else {
		    	resp.valueOf(EServiceResponseCode.C401);
	    		resp.setMessage("login fail! username or password can not be null！");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
	    return resp;
	}
	
	/**
	 * check guid repeat
	 * @param request
	 * @return
	 */
	@RequestMapping("/student/checkGuid.do")
	public @ResponseBody Response checkGuid(HttpServletRequest request){
		Response resp = new Response(EServiceResponseCode.C200);
		try {
			String guid = request.getParameter("guid");
			if(StringUtils.isEmpty(guid)) {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("check fail! guid is null or empty!");
				return resp;
			}
			
			Student student = studentService.getStudentByGuid(guid);
			if(student == null){
				return resp;
			} else {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("check fail! guid：" + guid + " is already exist！");
				return resp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * admin or student login system
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/login.do")
	public @ResponseBody ResponseT<UserInfo> login(HttpServletRequest request){
		ResponseT<UserInfo> resp = new ResponseT<UserInfo>(EServiceResponseCode.C200);
		try {
			String guid = request.getParameter("guid");
			String password = request.getParameter("password");
			
			//admin login
			if("admin".equals(guid) && "admin123".equals(password)) {
				UserInfo info = new UserInfo();
				info.setRole(UserRole.ADMIN);
				info.setUsername("admin");
				resp.setData(info);
				HttpSession session = request.getSession(true);
				session.setAttribute("USERINFO", info);
				return resp;
			}
			
			if(StringUtils.isNotEmpty(guid) && StringUtils.isNotEmpty(password)) {
				//use ldap auth student info
//				Student student = ldapAuth(username, password);
				Student student = studentService.getStudentByGuid(guid);
				if(student == null){
					resp.valueOf(EServiceResponseCode.C401);
					resp.setMessage("login fail! guid is not exist！");
					return resp;
				}
				if(password.equals(student.getPassword())){
					UserInfo info = new UserInfo();
					info.setRole(UserRole.STUDENT);
					info.setGuid(student.getGuid());
					info.setUsername(student.getUsername());
					resp.setData(info);
					HttpSession session = request.getSession(true);
					session.setAttribute("USERINFO", info);
					return resp;
				} else {
					resp.valueOf(EServiceResponseCode.C401);
					resp.setMessage("login fail! guid or password is error！");
					return resp;
				}
			} else {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("login fail! username or password can not be null！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * admin or student login off
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/loginOff.do")
	public @ResponseBody ResponseT<UserInfo> loginOff(HttpServletRequest request){
		ResponseT<UserInfo> resp = new ResponseT<UserInfo>(EServiceResponseCode.C200);
		try {
			HttpSession session = request.getSession(true);
			session.setAttribute("USERINFO", "");
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * student query quiz for yourself
	 * @param request
	 * @return
	 */
	@RequestMapping("/student/queryQuizInfo.do")
	public @ResponseBody ResponseT<Quiz> stduentQueryQuizInfo(HttpServletRequest request){
		ResponseT<Quiz> resp = new ResponseT<Quiz>(EServiceResponseCode.C200);
		try {
			UserInfo userInfo = (UserInfo)request.getSession(false).getAttribute("USERINFO");
			if(userInfo == null){
				resp.valueOf(EServiceResponseCode.C403);
				return resp;
			}
			if(userInfo.getRole() != UserRole.STUDENT){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("query fail! this funtion only open for student!");
				return resp;
			}
			Quiz quiz = quizService.getQuizByGuid(userInfo.getGuid());
			resp.setData(quiz);
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * admin query quiz info for pages
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/queryQuizInfo.do")
	public @ResponseBody ResponseLst<Quiz> adminQueryQuizInfo(HttpServletRequest request){
		ResponseLst<Quiz> resp = new ResponseLst<Quiz>(EServiceResponseCode.C200);
		try {
			UserInfo userInfo = (UserInfo)request.getSession(false).getAttribute("USERINFO");
			if(userInfo == null){
				resp.valueOf(EServiceResponseCode.C403);
				return resp;
			}
			if(userInfo.getRole() != UserRole.ADMIN){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("query fail! this funtion only open for admin!");
				return resp;
			}
			String limitStr = request.getParameter("limit");
			String offsetStr = request.getParameter("offset");
			if(StringUtils.isEmpty(limitStr)) {
				limitStr = "10";
			}
			if(StringUtils.isEmpty(offsetStr)) {
				offsetStr = "0";
			}
			int limit = Integer.valueOf(limitStr);
			int offset = Integer.valueOf(offsetStr);
			int countQuizRecords = quizService.countQuizRecords();
			if(countQuizRecords == 0){
				resp.setCount(0);
				return resp;
			}
			resp.setTotal(countQuizRecords);
			List<Quiz> listQuizPages = quizService.listQuizPages(offset, limit);
			if(listQuizPages == null) {
				return resp;
			}
			resp.setCount(listQuizPages.size());
			resp.setData(listQuizPages);
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * admin set quiz info for student
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/setQuizInfo.do")
	public @ResponseBody Response adminSetQuizInfo(HttpServletRequest request){
		Response resp = new Response(EServiceResponseCode.C200);
		try {
			UserInfo userInfo = (UserInfo)request.getSession(false).getAttribute("USERINFO");
			if(userInfo == null){
				resp.valueOf(EServiceResponseCode.C403);
				return resp;
			}
			if(userInfo.getRole() != UserRole.ADMIN){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! this funtion only open for admin!");
				return resp;
			}
			String guid = request.getParameter("guid");
			String quizavailableStr = request.getParameter("quizavailable");
			if(StringUtils.isEmpty(guid)) {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! guid can not be null or empty!");
				return resp;
			}
			
			if(StringUtils.isEmpty(quizavailableStr)) {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! quizavailabe must be true or false!");
				return resp;
			}
			boolean quizavailable = true;
			try {
				quizavailable = Boolean.valueOf(quizavailableStr);
			} catch (Exception e) {
				e.printStackTrace();
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! quizavailable must be true or false!");
				return resp;
			}
			int update = quizService.updateQuizavailabeByGuid(guid, quizavailable);
			if(update <= 0){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! the quzi record for guid:" + guid + " is not exist!");
				return resp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * student submit quiz
	 * @param request
	 * @return
	 */
	@RequestMapping("/student/submitQuiz.do")
	public @ResponseBody Response studentSubmitQuiz(HttpServletRequest request){
		Response resp = new Response(EServiceResponseCode.C200);
		try {
			UserInfo userInfo = (UserInfo)request.getSession(false).getAttribute("USERINFO");
			if(userInfo == null){
				resp.valueOf(EServiceResponseCode.C403);
				return resp;
			}
			if(userInfo.getRole() != UserRole.STUDENT){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! this funtion only open for student!");
				return resp;
			}
			String costtimeStr = request.getParameter("costtime");
			String scoresStr = request.getParameter("scores");
			if(StringUtils.isEmpty(costtimeStr)) {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! costtime can not be null or empty!");
				return resp;
			}
			int costtime = -1;
			try {
				costtime = Integer.valueOf(costtimeStr);
			} catch (Exception e) {
				e.printStackTrace();
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! costtime must be a integer!");
				return resp;
			}
			
			if(StringUtils.isEmpty(scoresStr)) {
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! scores can not be null or empty!");
				return resp;
			}
			
			double scores = -1;
			try {
				scores = Double.valueOf(scoresStr);
			} catch (Exception e) {
				e.printStackTrace();
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! costtime must be a integer!");
				return resp;
			}
			
			Quiz quiz = new Quiz();
			quiz.setCosttime(costtime);
			quiz.setQuizavailable(false);//finish quiz, quizavailable set false
			quiz.setScore(scores);
			quiz.setGuid(userInfo.getGuid());
			quiz.setUpdatetime(new Date());
			
			int update = quizService.updateQuizForStudent(quiz);
			if(update <= 0){
				resp.valueOf(EServiceResponseCode.C401);
				resp.setMessage("update fail! because the quzi record for guid:" + userInfo.getGuid() + " is not exist!");
				return resp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.valueOf(EServiceResponseCode.C500);
		}
		return resp;
	}
	
	/**
	 * auth student info by ldap, 
	 * if fail return null; success return student
	 * @param username
	 * @param password
	 * @return 
	 */
	@SuppressWarnings("unused")
	private Student ldapAuth(String username, String password){
		Student student = new Student();
		student.setGuid(username);
		student.setUsername(username);
		student.setPassword(password);
		return student;
	}
}
