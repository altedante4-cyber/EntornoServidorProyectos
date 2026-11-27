package org.goya.daw2.dwes.d3ud3.d3_ud3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Map;
@Configuration
@EnableWebSecurity
class WebSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		// @formatter:off
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login2")
				.permitAll()
			)
			.logout(LogoutConfigurer::permitAll);
		// @formatter:on

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder encoder) {
		Map <String , String > usuarios = new LinkedHashMap<>();
		
	usuarios.put("user","$2a$10$DQY8k9JTW7o7NTSRHmWhQOXwHvBZCGaWj06RNBrzleWO/XiUg.nWe");
		usuarios.put("user2","$2a$10$P3CZmGxydvHBcsG5BGq86uwDPyLH24pCNnA2XPNlrYsYfeNV7/pNe");


		List<UserDetails>listausuario = new ArrayList<>();

		for(Map.Entry<String,String>entry : usuarios.entrySet()){
			UserDetails user = User.withUsername(entry.getKey()).password(entry.getValue()).roles("USER").build();
			listausuario.add(user);
		}
			return new InMemoryUserDetailsManager(listausuario);		
	}	

}
