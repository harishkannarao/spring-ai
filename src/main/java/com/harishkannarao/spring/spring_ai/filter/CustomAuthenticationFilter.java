package com.harishkannarao.spring.spring_ai.filter;

import com.harishkannarao.spring.spring_ai.security.CustomAuthenticationResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final CustomAuthenticationResolver customAuthenticationResolver;

	@Autowired
	public CustomAuthenticationFilter(CustomAuthenticationResolver customAuthenticationResolver) {
		this.customAuthenticationResolver = customAuthenticationResolver;
	}

	@Override
	protected void doFilterInternal(
		@NotNull HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain) throws ServletException, IOException {
		try {
			Optional<Authentication> resolvedAuthentication = customAuthenticationResolver.resolve(request);
			resolvedAuthentication.ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
		} catch (Exception e) {
			log.error("Error resolving authentication: " + e.getMessage(), e);
		} finally {
			filterChain.doFilter(request, response);
		}
	}
}
