package com.harishkannarao.spring.spring_ai.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDetailsResolver {

	private final Map<String, UserDetails> userDetailsMap = Map.ofEntries(
		Map.entry("basic-user-token", User.builder()
			.username("basic-user-name")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(Collections.emptyList())
			.build()),
		Map.entry("user-token-1", User.builder()
			.username("user-name-1")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
			.build()),
		Map.entry("user-token-2", User.builder()
			.username("user-name-2")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
			.build()),
		Map.entry("manager-token-1", User.builder()
			.username("manager-name-1")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_STORE_MANAGER")))
			.build()),
		Map.entry("manager-token-2", User.builder()
			.username("manager-name-2")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_STORE_MANAGER")))
			.build()),
		Map.entry("region-manager-token-1", User.builder()
			.username("region-manager-name-2")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_REGIONAL_MANAGER")))
			.build())
	);

	public Optional<UserDetails> resolve(String token) {
		return Optional.ofNullable(userDetailsMap.get(token));
	}
}
