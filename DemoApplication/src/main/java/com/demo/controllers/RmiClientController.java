package com.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.services.GreetingService;

@RestController
public class RmiClientController {

/*	@Autowired
	private RmiProxyFactoryBean rmiProxyFactoryBean;

	@RequestMapping("/rmi/greeting")
	public String getGreeting() {
		return ((GreetingService) rmiProxyFactoryBean.getObject()).getGreeting("aharon");
	}*/
}
