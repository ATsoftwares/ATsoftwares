package com.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import com.demo.config.security.filters.CsrfFilters;
import com.demo.services.AuthenticationService;
import com.demo.services.DemoSecurityService;

@Configuration
@ComponentScan
@EnableWebSecurity
public class DemoWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private DemoSecurityService demoSecurityService;
	
//	@Autowired
//	private DemoOauthService demoOauthService;

//	@Autowired
//	private DemoAuthenticationSuccess demoAuthenticationSuccess;
//
//	@Autowired
//	private DemoAuthenticationFailure demoAuthenticationFailure;
//
//	@Autowired
//	private DemoUnauthorizedEntryPoint demoUnauthorizedEntryPoint;
//
//	@Autowired
//	private DemoBasicAuthenticationEntryPoint demoBasicAuthenticationEntryPoint;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			httpBasic().
		and().
			authorizeRequests().antMatchers(HttpMethod.GET, demoSecurityService.getHttpIgnoring()).permitAll().
				anyRequest().authenticated().
		and().
			formLogin().
				loginPage("/#/login").loginProcessingUrl("/login").
				usernameParameter("user").passwordParameter("password").
		and().
			logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).
		and().
			csrf().
				csrfTokenRepository(csrfTokenRepository()).
				and().addFilterAfter(CsrfFilters.csrfHeaderFilter(), SessionManagementFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.
			ignoring().antMatchers(demoSecurityService.getWebIgnoring());
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
}
