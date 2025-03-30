package com.harishkannarao.spring.spring_ai.entity;

import java.util.UUID;

public record DeleteConversationRequest(
	UUID conversationId
) {
}
