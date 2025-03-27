package com.harishkannarao.spring.spring_ai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
	private final ObjectMapper objectMapper;

	@Autowired
	public JsonUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String toJson(Object input) {
		try {
			return objectMapper.writeValueAsString(input);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public  <T> T fromJson(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public JsonNode toJsonNode(String content) {
		try {
			return objectMapper.readTree(content);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
