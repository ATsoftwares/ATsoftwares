package com.demo.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.models.UserInfo;
import com.demo.repository.UserRepository;
import com.demo.services.LoginService;
import com.demo.utils.DemoUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RequestMapping("/login")
@RestController
public class LoginController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody UserInfo login(String json) throws JsonParseException, JsonMappingException, IOException {
		json = DemoUtils.decode(json, "{");
		UserInfo userInfo = DemoUtils.deserialize(json, UserInfo.class);
		return userRepository.getUserByCredentials(userInfo.getUserName(), userInfo.getPassword());
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Greeting login2() throws JsonParseException, JsonMappingException, IOException {
		return new Greeting();
	}

	public class Greeting {
		String id = UUID.randomUUID().toString();
		String msg = "hello aharon";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
};
