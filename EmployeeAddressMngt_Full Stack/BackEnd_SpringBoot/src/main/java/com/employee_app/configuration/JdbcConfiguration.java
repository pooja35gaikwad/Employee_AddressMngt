package com.employee_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class JdbcConfiguration {
	@Bean
	public JdbcTemplate jdbcTemplate(HikariDataSource hikariDataSource){
	   return new JdbcTemplate(hikariDataSource);
	}
}
