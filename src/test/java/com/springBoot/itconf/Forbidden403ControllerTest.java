package com.springBoot.itconf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class Forbidden403ControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testToon403View() throws Exception {
		mockMvc.perform(get("/403")).andExpect(status().isOk()).andExpect(view().name("error/403"));
	}
}
