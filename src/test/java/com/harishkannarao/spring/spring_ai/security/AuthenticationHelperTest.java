package com.harishkannarao.spring.spring_ai.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationHelperTest {

	private final AuthenticationHelper authenticationHelper = new AuthenticationHelper();

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
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		HttpServletRequest httpServletRequest = mock();
		when(httpServletRequest.getUserPrincipal())
			.thenReturn(authentication);
		UserDetails result = authenticationHelper.getCurrentUsername(httpServletRequest);

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(user);
	}
}