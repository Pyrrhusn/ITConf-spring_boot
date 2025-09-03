package com.springBoot.itconf;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.MyUser;
import domain.Role;
import repository.MyUserRepository;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringBootItConfApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserDetailsService userService;
	
	@MockitoBean
	private MyUserRepository myUserRepository;
	
	@BeforeEach
	void setup() {
		//Mocking MyUser
		MyUser normalUser = MyUser.builder().id(1L).username("user").password("password")
				.role(Role.USER).build();
		
		//Mocking User (spring builtin)
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		User user = new User(normalUser.getUsername(), normalUser.getPassword(), Collections.singletonList(authority));
		
		when(userService.loadUserByUsername("user")).thenReturn(user);
		when(myUserRepository.findByUsername("user")).thenReturn(normalUser);
	}
	
	@ParameterizedTest
	@CsvSource({"/login, views/login", "/403, error/403", "/overzicht, views/overzicht"})
	void testGetViews(String url, String expectedViewName) throws Exception {
		mockMvc.perform(get(url)).andExpect(status().isOk())
			.andExpect(view().name(expectedViewName));
	}
	
	@ParameterizedTest
	@WithMockUser
	@CsvSource({"/favorieten, views/favorieten", "/event/2, views/eventDetails"})
	void testAccessWithUserRole(String url, String expectedViewName) throws Exception {
		mockMvc.perform(get(url)).andExpect(status().isOk())
			.andExpect(view().name(expectedViewName))
			.andExpect(model().attributeExists("username"))
			.andExpect(model().attribute("username", "user"))
			.andExpect(model().attribute("userRole", "USER"));
	}
	
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testFavorietenNoAccessWithAdmin() throws Exception {
		mockMvc.perform(get("/favorieten")).andExpect(status().isForbidden());
	}
	
	@ParameterizedTest
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@CsvSource({"/event/toevoegen, views/eventForm", "/event/2/bewerken, views/eventForm", "/lokaal/toevoegen, views/lokaalForm"})
	void testAccessWithAdminRole(String url, String expectedViewName) throws Exception {
		mockMvc.perform(get(url)).andExpect(status().isOk())
			.andExpect(view().name(expectedViewName))
			.andExpect(model().attributeExists("username"))
			.andExpect(model().attribute("username", "admin"))
			.andExpect(model().attribute("userRole", "ADMIN"));
	}
	
	@ParameterizedTest
	@WithMockUser
	@ValueSource(strings = {"/event/toevoegen", "/event/2/bewerken", "/lokaal/toevoegen"})
	void testNoAccessToAdminViewsWithUserRole(String url) throws Exception {
		mockMvc.perform(get(url)).andExpect(status().isForbidden());
	}
	
	@WithAnonymousUser
	@ParameterizedTest
	@ValueSource(strings = {"/event/**", "/favorieten**", "/lokaal/**"})
	void testNoAccessAnonymous(String uri) throws Exception {
		mockMvc.perform(get(uri)).andExpect(redirectedUrlPattern("**/login"));
	}
	
	@WithMockUser(username = "user", roles = {"NOT_USER"})
	@ParameterizedTest
	@ValueSource(strings = {"/event/**", "/favorieten**", "/lokaal/**"})
	void testNoAccessWithWrongUserRole(String uri) throws Exception {
		mockMvc.perform(get(uri)).andExpect(status().isForbidden());
	}
	
}
