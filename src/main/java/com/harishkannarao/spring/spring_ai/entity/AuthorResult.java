package com.harishkannarao.spring.spring_ai.entity;

import java.util.List;

public record AuthorResult(
	String author,
	List<String> books
) {
}
