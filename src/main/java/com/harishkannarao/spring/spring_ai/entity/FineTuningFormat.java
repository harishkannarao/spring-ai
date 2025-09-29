package com.harishkannarao.spring.spring_ai.entity;

import java.util.List;

public record FineTuningFormat(
	List<Content> messages
) {
	public record Content(
		String content,
		Role role
	) {
		public enum Role {
			system,
			user,
			assistant
		}
	}
}
