package com.harishkannarao.spring.spring_ai.entity;

import java.util.Map;

public record InputSecureDocument(
	String content,
	Map<String, Object> metaData
) {
}
