package com.coding404.demo.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomRememberMe implements AuthenticationSuccessHandler{
	
	
	private String redirectPage;
	
	//생성자
	public CustomRememberMe(String redirectPage) {
		this.redirectPage = redirectPage;
	}
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("나중에 사용자가 다시 브라우저에 들어올 때, rememberMe가 성공하면, 그 이후에 실행된다.");
		//authentication 를 사용해서 권한별로 처리한 후에 
		
		response.sendRedirect(redirectPage);
	}
	
	
	
	
	
}
