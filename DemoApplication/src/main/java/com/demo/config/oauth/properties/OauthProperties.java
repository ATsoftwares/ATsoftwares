package com.demo.config.oauth.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "demo.oauth")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound =  true)
@Getter @Setter
public class OauthProperties {

	/***
	 * List of ant matchers rest http endpoints that spring security should ignore
	 */
	private List<String> httpIgnoring;
	
	/***
	 * List of ant matchers resources endpoints that spring security should ignore
	 */
	private List<String> webIgnoring;
}
