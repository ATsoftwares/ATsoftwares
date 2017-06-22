package com.demo.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class DemoWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	public DemoWebApplicationInitializer() {
		super(DemoWebSecurityConfig.class);
	}

	// @Override
	// protected Class<?>[] getRootConfigClasses() {
	// return new Class[] { DemoWebSecurityConfig.class };
	// }
	//
	// @Override
	// protected Class<?>[] getServletConfigClasses() {
	// return null;
	// }
	//
	// @Override
	// protected String[] getServletMappings() {
	// return new String[] { "/" };
	// }
}
