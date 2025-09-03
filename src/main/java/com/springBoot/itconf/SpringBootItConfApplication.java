package com.springBoot.itconf;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import perform.PerformRest;
import service.EventService;
import service.EventServiceImpl;
import service.FavorietService;
import service.FavorietServiceImpl;
import service.LokaalService;
import service.LokaalServiceImpl;
import service.MyUserDetailsService;
import service.MyUserDetailsServiceImpl;
import service.SprekerService;
import service.SprekerServiceImpl;
import validator.EventSprekersUniqueValidator;
import validator.EventTijdValidator;
import validator.LokaalNaamUniqueValidator;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class SpringBootItConfApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootItConfApplication.class, args);
		
		try {
			new PerformRest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/overzicht");
	}

	@Bean
	LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale.Builder().setLanguage("nl").setRegion("BE").build());
		return slr;
	}
	
	@Bean
	EventService eventService() {
		return new EventServiceImpl();
	}
	
	@Bean
	LokaalService lokaalService() {
		return new LokaalServiceImpl();
	}
	
	@Bean
	SprekerService sprekerService() {
		return new SprekerServiceImpl();
	}
	
	@Bean
	UserDetailsService userDetailsService() { //voor security
		return new MyUserDetailsServiceImpl();
	}
	
	@Bean
	MyUserDetailsService myUserDetailsService() { //voor doa
		return new MyUserDetailsServiceImpl();
	}
	
	@Bean
	FavorietService favorietService() {
		return new FavorietServiceImpl();
	}
	
	@Bean
	Validator eventSprekersUniqueValidator() {
		return new EventSprekersUniqueValidator();
	}
	
	@Bean
	Validator eventTijdValidator() {
		return new EventTijdValidator();
	}
	
	@Bean
	Validator lokaalNaamUniqueValidator() {
		return new LokaalNaamUniqueValidator();
	}
	
}
