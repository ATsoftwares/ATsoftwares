package com.adm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class managementController {

	@RequestMapping("manage")
	public @ResponseBody String getContacts() {
		return "Contacts";
	}
}
