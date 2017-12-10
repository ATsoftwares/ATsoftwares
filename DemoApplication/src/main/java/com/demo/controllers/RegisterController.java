package com.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.models.UserInfo;
import com.demo.repository.UserRepository;

@RestController
public class RegisterController {
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
			"application/json" })
	public void getMessage(@RequestBody UserInfo user) {
		userRepository.saveOrUpdate(user);
	}
}
