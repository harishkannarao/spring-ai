package com.harishkannarao.spring.spring_ai.entity;

import java.util.List;

public record InputDocument(
	String content,
	List<InputMetaData> metaData
) {
}
