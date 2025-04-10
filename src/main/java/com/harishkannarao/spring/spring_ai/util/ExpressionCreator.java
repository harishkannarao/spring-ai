package com.harishkannarao.spring.spring_ai.util;

import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpressionCreator {

	public Filter.Expression create(UserDetails userDetails) {
		FilterExpressionBuilder builder = new FilterExpressionBuilder();
		List<String> roles = userDetails.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.toList();
		if (roles.contains("ROLE_USER")) {
			return builder.and(
				builder.eq("id", userDetails.getUsername()),
				builder.eq("type", "USER")
			).build();
		}
		return null;
	}
}
