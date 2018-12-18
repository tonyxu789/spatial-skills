package com.ssuog.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssuog.po.UserInfo;

public class LoginFilter implements Filter {
    public LoginFilter() {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURL().toString();

        // Get session parameters
        UserInfo userInfo = null;
        HttpSession session = req.getSession(false);
		if (session != null) {
			Object object = session.getAttribute("USERINFO");
			if(object != null && !"".equals(object)) {
				userInfo = (UserInfo) session.getAttribute("USERINFO");
			}
		}
        
        try {
        	if(userInfo != null){
        		chain.doFilter(request, response);
        		return;
        	}
        	
        	if(userInfo == null && 
        			(url.indexOf("login.do") >= 0 
        			|| url.indexOf("register.do") >= 0 
        			|| url.indexOf("checkGuid.do") >= 0)) {
        		// Perform operations that do not require identity
        		if (session != null) {
        			session.setAttribute("USERINFO", null);
        		}
        		chain.doFilter(request, response);
        	} else {
        		// No login, jump to login page
        		String redirectUrl = "/login.html";
        		resp.sendRedirect(redirectUrl);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}


}
