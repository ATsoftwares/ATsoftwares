package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.config.oauth.OauthSecurityService;
import com.demo.config.oauth.properties.OauthProperties;

@Service
public class DemoOauthService implements OauthSecurityService {

	@Autowired
	private OauthProperties oauthProperties;

	@Override
	public String[] getHttpIgnoring() {
		List<String> ignoring = oauthProperties.getHttpIgnoring();
		return ignoring.toArray(new String[ignoring.size()]);
	}

	@Override
	public String[] getWebIgnoring() {
		List<String> ignoring = oauthProperties.getWebIgnoring();
		return ignoring.toArray(new String[ignoring.size()]);
	}
}
