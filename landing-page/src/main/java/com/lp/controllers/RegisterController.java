package com.lp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.lp.models.RegistrationModel;
import com.lp.services.UserRepository;

@RestController
public class RegisterController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = { "application/json" }, consumes = {
			"application/json" })
	public void getMessage(@RequestBody RegistrationModel user) {
		userRepository.saveOrUpdate(user);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = { "application/json" })
	public List<RegistrationModel> getAllRegisteredUsers() {
		return userRepository.getUsers();
	}
}
