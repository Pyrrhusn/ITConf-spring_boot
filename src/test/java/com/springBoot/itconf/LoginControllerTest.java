package com.springBoot.itconf;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Test
    void testWrongPassword() throws Exception {
        mockMvc.perform(formLogin("/login")
            .user("username", "userNFE")
            .password("password", "wrongPassword"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login?error"));
    }
	
	@Test
    void testCorrectPassword() throws Exception {
        mockMvc.perform(formLogin("/login")
            .user("username", "userNFE")
            .password("password", "nfe"))
            .andExpect(status().isFound()) 
            .andExpect(redirectedUrl("/"));
    }
	
	@Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("views/login"));
    }
	
	@Test
    public void testLoginFormMetErrorParam() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("views/login"));
    }

    @Test
    public void testLoginFormMetLogoutParam() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("views/login"));
    }
	
}
