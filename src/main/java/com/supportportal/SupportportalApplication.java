package com.supportportal;

import com.supportportal.domain.User;
import com.supportportal.enumeration.Role;
import com.supportportal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.supportportal.constant.FileConstant.USER_FOLDER;
import static com.supportportal.constant.UserImplConstant.FOUND_USER_BY_USERNAME;

@SpringBootApplication
public class SupportportalApplication {
	@Autowired
	private UserRepository userRepository;
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(SupportportalApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			User superAdmin = new User();
			superAdmin.setUserId("1001818818");
			superAdmin.setFirstName("John");
			superAdmin.setLastName("Adams");
			superAdmin.setUsername("john");
			superAdmin.setPassword(bCryptPasswordEncoder().encode("password"));
			superAdmin.setEmail("admin@gmail.com");
			superAdmin.setActive(true);
			superAdmin.setNotLocked(true);
			superAdmin.setRole(Role.ROLE_SUPER_ADMIN.name());
			superAdmin.setAuthorities(Role.ROLE_SUPER_ADMIN.getAuthorities());
			superAdmin.setJoinDate(new Date());

			User user = new User();
			user.setUserId("100181019");
			user.setFirstName("Chisom");
			user.setLastName("Obidke");
			user.setUsername("user");
			user.setPassword(bCryptPasswordEncoder().encode("password"));
			user.setEmail("user@gmail.com");
			user.setActive(true);
			user.setNotLocked(true);
			user.setRole(Role.ROLE_USER.name());
			user.setAuthorities(Role.ROLE_USER.getAuthorities());
			user.setJoinDate(new Date());

			userRepository.save(superAdmin);
			userRepository.save(user);
			LOGGER.info(FOUND_USER_BY_USERNAME + superAdmin.getUsername());
			LOGGER.info(FOUND_USER_BY_USERNAME + user.getUsername());
		};
	}
}
