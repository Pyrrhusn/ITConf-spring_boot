package com.springBoot.itconf;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.Lokaal;
import service.LokaalService;

@SpringBootTest
@AutoConfigureMockMvc
public class LokaalControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LokaalService lokaalService;
    
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testToonLokaalForm() throws Exception {
    	mockMvc.perform(get("/lokaal/toevoegen"))
        .andExpect(status().isOk())
        .andExpect(view().name("views/lokaalForm"))
        .andExpect(model().attributeExists("lokaal"));
    }
    
    @WithMockUser
    @Test
    public void testToonLokaalForm_userNoAccess() throws Exception {
    	mockMvc.perform(get("/lokaal/toevoegen"))
        .andExpect(status().isForbidden());
    }
    
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testLokaalForm_okEnRedirect() throws Exception {
    	Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build();
    	String saveMessage = "Lokaal met 25 capaciteit werd toegevoegd.";
    	
    	mockMvc.perform(post("/lokaal/toevoegen").with(csrf()).flashAttr("lokaal", l1))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(redirectedUrl("/lokaal/toevoegen"))
    		.andExpect(flash().attributeExists("lokaalSaveSuccessMessage"))
    		.andExpect(flash().attribute("lokaalSaveSuccessMessage", saveMessage));
    	
    	verify(lokaalService).save(l1);
    }
    
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testLokaalForm_validationErrors() throws Exception {
    	mockMvc.perform(post("/lokaal/toevoegen").with(csrf())
    		.param("naam", "").param("capaciteit", "0"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("views/lokaalForm"))
    		.andExpect(model().attributeHasFieldErrors("lokaal", "naam", "capaciteit"));
    }
	
}
