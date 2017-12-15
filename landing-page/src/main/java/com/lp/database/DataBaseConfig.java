package com.lp.database;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "someEntityManagerFactory", transactionManagerRef = "someTransactionManager", basePackages = {
		"com.lp.services" })
@EntityScan(basePackages = "com.lp.models")
@ConfigurationProperties(prefix = "mysql.datasource")
public class DataBaseConfig {

	@Autowired
	private Environment env;

	@Bean
	public DataSource someDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("mysql.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("mysql.datasource.url"));
		dataSource.setUsername(env.getProperty("mysql.datasource.username"));
		dataSource.setPassword(env.getProperty("mysql.datasource.password"));
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean someEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(someDataSource());
		em.setPackagesToScan(new String[] { "com.lp.models" });
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	public PlatformTransactionManager someTransactionManager() {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(someEntityManagerFactory().getObject());
		tm.setDataSource(someDataSource());
		return tm;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.setProperty("spring.jpa.show-sql", env.getProperty("spring.jpa.show-sql"));
		properties.setProperty("spring.jpa.hibernate.naming.physical-strategy",
				env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
		return properties;
	}

}
