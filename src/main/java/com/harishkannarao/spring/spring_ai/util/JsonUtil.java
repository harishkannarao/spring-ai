package com.harishkannarao.spring.spring_ai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class JsonUtil {
	private final ObjectMapper objectMapper;

	@Autowired
	public JsonUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String toJson(Object input) {
		return objectMapper.writeValueAsString(input);
	}

	public  <T> T fromJson(String content, Class<T> valueType) {
		return objectMapper.readValue(content, valueType);
	}

	public JsonNode toJsonNode(String content) {
		return objectMapper.readTree(content);
	}
}
