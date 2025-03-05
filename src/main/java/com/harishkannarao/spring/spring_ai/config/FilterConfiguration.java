package com.harishkannarao.spring.spring_ai.config;

import com.harishkannarao.spring.spring_ai.filter.RequestTracingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Collections;

@Configuration
public class FilterConfiguration {

	@Bean("requestTracingFilter")
	public FilterRegistrationBean<RequestTracingFilter> registerRequestTracingFilter() {
		FilterRegistrationBean<RequestTracingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestTracingFilter());
		filterRegistrationBean.setName(RequestTracingFilter.NAME);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setUrlPatterns(Collections.singletonList(RequestTracingFilter.PATH));
		return filterRegistrationBean;
	}
}
