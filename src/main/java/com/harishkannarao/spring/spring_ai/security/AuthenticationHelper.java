package com.harishkannarao.spring.spring_ai.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

	public UserDetails getCurrentUsername(HttpServletRequest request) {
		Authentication authentication =
			(UsernamePasswordAuthenticationToken) request.getUserPrincipal();
		return (UserDetails) authentication.getPrincipal();
	}
}
