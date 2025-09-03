package com.springBoot.itconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
			.authorizeHttpRequests(requests ->
					requests.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
							.requestMatchers("/login**").permitAll()
							.requestMatchers("/i18n/**").permitAll()
							.requestMatchers("/img/**").permitAll()
							.requestMatchers("/icons/**").permitAll()
							.requestMatchers("/css/**").permitAll()
							.requestMatchers("/").permitAll()
							.requestMatchers("/overzicht").permitAll()
							.requestMatchers("/error").permitAll()
							.requestMatchers("/rest/**").permitAll()
							.requestMatchers("/403**").permitAll()
							.requestMatchers("/favorieten").hasRole("USER")
							.requestMatchers("/event/toevoegen").hasRole("ADMIN")
							.requestMatchers("/event/{id}").hasAnyRole("USER", "ADMIN")
							.requestMatchers("/favorieten/**").hasRole("USER")
							.requestMatchers("/**").hasRole("ADMIN"))
			.formLogin(form -> 
							form.defaultSuccessUrl("/", true)
							.loginPage("/login")
							.usernameParameter("username")
							.passwordParameter("password"))
			.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/403"));
		
		return http.build();
	}

}
