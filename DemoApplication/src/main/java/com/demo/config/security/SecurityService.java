package com.demo.config.security;

public interface SecurityService {

	/***
	 * Http rest endpoints Paths to ignore by spring security
	 * 
	 * @return String array of ant matcher paths to ignore when authenticating.
	 */
	String[] getHttpIgnoring();

	/***
	 * ressoure path to ignore by spring security
	 * 
	 * @return String array of ant matcher paths to ignore when authenticating.
	 */
	String[] getWebIgnoring();
}
