package com.harishkannarao.spring.spring_ai.entity;

import java.util.List;

public record QuestionAnswerWrapper(
	List<QuestionAnswer> qaPairs
) {
}
