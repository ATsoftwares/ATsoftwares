package com.demo.config.security.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component("security")
@ConfigurationProperties(prefix = "demo.security")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@Getter @Setter
public class SecurityProperties {

	/***
	 * List of ant matchers rest http endpoints that spring security should ignore
	 */
	private List<String> httpSecurityIgnoring;

	/***
	 * List of ant matchers resources endpoints that spring security should ignore
	 */
	private List<String> webSecurityIgnoring;
}
