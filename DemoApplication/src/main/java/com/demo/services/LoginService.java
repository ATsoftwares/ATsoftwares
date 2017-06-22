package com.demo.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.models.UserInfo;
import com.demo.repository.UserRepository;
import com.demo.utils.DemoUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	public UserInfo checkCredentials(String json) throws JsonParseException, JsonMappingException, IOException {
		LoginInfo login = DemoUtils.deserialize(json, LoginInfo.class);
		UserInfo user = userRepository.getUserByCredentials(login.getUser(), login.getPassword());
		if (user != null) {
			return user;
		} else {
			return null;
		}
	}

	public static class LoginInfo {
		private String username;
		private String password;

		public String getUser() {
			return username;
		}

		public void setUser(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
