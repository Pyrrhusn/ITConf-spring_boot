package com.springBoot.itconf;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
import exception.UserMaxFavorietenException;
import service.FavorietService;

@SpringBootTest
@AutoConfigureMockMvc
public class FavorietControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private FavorietService favorietService;
	
	@Test
	@WithMockUser
	public void testToonFavorieten() throws Exception {
		Spreker s1 = new Spreker("S1");
		Spreker s2 = new Spreker("S2");
		Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build();
		
		Event e1 = Event.builder()
				.id(1L).naam("Test1").beschrijving("Beschrijving1")
				.sprekers(List.of(s1, s2)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
				.startTijdstip(LocalTime.of(10, 0)).eindTijdstip(LocalTime.of(12, 0))
				.beamercode("0123").beamercheck("26").prijs(12.34).build();
		Event e2 = Event.builder()
				.id(2L).naam("Test2").beschrijving("Beschrijving2")
				.sprekers(List.of(s1, s2)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
				.startTijdstip(LocalTime.of(12, 30)).eindTijdstip(LocalTime.of(14, 30))
				.beamercode("0123").beamercheck("26").prijs(12.34).build();
		
		when(favorietService.findAll("user")).thenReturn(List.of(e1, e2));
		
		mockMvc.perform(get("/favorieten")).andExpect(status().isOk())
			.andExpect(view().name("views/favorieten"))
			.andExpect(model().attributeExists("favorieten"))
			.andExpect(model().attribute("favorieten", List.of(e1, e2)));
	}
	
	@Test
	@WithMockUser
    public void testToevoegenFavorietEvent() throws Exception {
        mockMvc.perform(post("/favorieten").with(csrf())
                .param("eventId", "1")
                .param("action", "add")
                .param("location", "eventDetails"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(favorietService).voegFavorietToe("user", 1L);
    }
	
	@Test
	@WithMockUser
    public void testVerwijderenFavorietEvent() throws Exception {
        mockMvc.perform(post("/favorieten").with(csrf())
                .param("eventId", "2")
                .param("action", "remove")
                .param("location", "favorieten"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/favorieten"));

        verify(favorietService).verwijderFavoriet("user", 2L);
    }
	
	@Test
	@WithMockUser
    public void testTeovoegenFavorietThrowsUserMaxFavorietenException() throws Exception {
		doThrow(new UserMaxFavorietenException()).when(favorietService).voegFavorietToe("user", 3L);
		
		 mockMvc.perform(post("/favorieten").with(csrf())
	                .param("eventId", "3")
	                .param("action", "add")
	                .param("location", "eventDetails"))
	                .andExpect(view().name("error/error"));

        verify(favorietService).voegFavorietToe("user", 3L);
    }
	
	@Test
	@WithMockUser
    void testPostFavorietInvalidLocation() throws Exception {
        mockMvc.perform(post("/favorieten").with(csrf())
                .param("eventId", "4")
                .param("action", "add")
                .param("location", "INVALID"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    void testPostFavorietInvalidAction() throws Exception {
        mockMvc.perform(post("/favorieten").with(csrf())
                .param("eventId", "5")
                .param("action", "invalidAction")
                .param("location", "eventDetails"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
	
}
