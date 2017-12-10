package com.demo.config.rmi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.demo.services.GreetingService;

/*@Configuration
public class RmiClientConfig {

	@Bean
	public RmiProxyFactoryBean rmiProxyFactoryBean() {
		RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
		rmiProxyFactoryBean.setServiceUrl("rmi://localhost:5000/greetingService");
		rmiProxyFactoryBean.setServiceInterface(GreetingService.class);
		rmiProxyFactoryBean.afterPropertiesSet();
		return rmiProxyFactoryBean;
	}
}*/
