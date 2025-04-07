package com.harishkannarao.spring.spring_ai.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationHelperTest {

	private final AuthenticationHelper authenticationHelper = new AuthenticationHelper();

	@BeforeEach
	@AfterEach
	public void cleanUp() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void getCurrentUsername_returns_username() {
		UserDetails user = User.builder()
			.username("some-user-name")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(Collections.emptyList())
			.build();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String result = authenticationHelper.getCurrentUsername();

		assertThat(result).isEqualTo(user.getUsername());
	}
}