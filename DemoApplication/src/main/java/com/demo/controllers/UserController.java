package com.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.models.UserInfo;
import com.demo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<UserInfo> getUsers() {
		return userRepository.getUsers();
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void getMessage(UserInfo user) {
		userRepository.saveOrUpdate(user);
	}

}
