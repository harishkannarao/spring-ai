package com.harishkannarao.spring.spring_ai.entity;

import java.util.UUID;

public record ChatWithMemory(
	UUID conversationId,
	String chat
) {
}
