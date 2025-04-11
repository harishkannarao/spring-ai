package com.harishkannarao.spring.spring_ai.util;

import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExpressionCreator {

	private final Map<String, String> storeManagerMap = Map.ofEntries(
		Map.entry("manager-name-1", "store-01"),
		Map.entry("manager-name-2", "store-02")
	);

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
		} else if (roles.contains("ROLE_STORE_MANAGER")) {
			return builder.and(
				builder.eq("id", storeManagerMap.getOrDefault(userDetails.getUsername(), "")),
				builder.eq("type", "STORE")
			).build();
		} else if (roles.contains("ROLE_REGIONAL_MANAGER")) {
			return builder.eq("type", "STORE").build();
		}
		return null;
	}
}
