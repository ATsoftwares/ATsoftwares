package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.demo.config.security.SecurityService;
import com.demo.config.security.properties.SecurityProperties;

@Service
public class DemoSecurityService implements SecurityService {

	@Autowired
	@Qualifier("security")
	private SecurityProperties securityProperties;

	@Override
	public String[] getHttpIgnoring() {
		List<String> ignoring = securityProperties.getHttpSecurityIgnoring();
		return ignoring.toArray(new String[ignoring.size()]);
	}

	@Override
	public String[] getWebIgnoring() {
		List<String> ignoring = securityProperties.getWebSecurityIgnoring();
		return ignoring.toArray(new String[ignoring.size()]);
	}
}
