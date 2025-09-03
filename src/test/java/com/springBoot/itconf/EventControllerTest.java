package com.springBoot.itconf;

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
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import service.EventService;
import service.FavorietService;
import service.LokaalService;
import service.SprekerService;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private EventService eventService;
	
	@MockitoBean
	private LokaalService lokaalService;
	
	@MockitoBean
	private SprekerService sprekerService;
	
	@MockitoBean
	private FavorietService favorietService;
	
	private static final Spreker s1 = new Spreker("S1");
	private static final Spreker s2 = new Spreker("S2");
	private static final Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build();
	private static final Event e1 = Event.builder()
			.id(1L).naam("Test1").beschrijving("Beschrijving1")
			.sprekers(List.of(s1, s2)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
			.startTijdstip(LocalTime.of(10, 0)).eindTijdstip(LocalTime.of(12, 0))
			.beamercode("0123").beamercheck("26").prijs(12.34).build();
	
	@BeforeEach
	void before() {
		when(lokaalService.findAll()).thenReturn(Collections.singletonList(l1));
	}
	
	@Test
	@WithMockUser
	public void testToonEventDetails_user_alFavoriet() throws Exception {
		when(eventService.findById(1L)).thenReturn(e1);
		when(favorietService.isFavoriet("user", 1L)).thenReturn(true);
		when(favorietService.kanAanFavorietToevoegen("user")).thenReturn(false);
		
		mockMvc.perform(get("/event/1")).andExpect(status().isOk())
			.andExpect(view().name("views/eventDetails"))
			.andExpect(model().attributeExists("event", "isFavoriet", "kanAanFavorietToevoegen"))
			.andExpect(model().attribute("event", e1))
			.andExpect(model().attribute("isFavoriet", true))
			.andExpect(model().attribute("kanAanFavorietToevoegen", false));
		
		verify(eventService).findById(1L);
		verify(favorietService).isFavoriet("user", 1L);
		verify(favorietService).kanAanFavorietToevoegen("user");
	}
	
	@Test
	@WithMockUser
	public void testToonEventDetails_user_notFavoriet() throws Exception {
		when(eventService.findById(1L)).thenReturn(e1);
		when(favorietService.isFavoriet("user", 1L)).thenReturn(false);
		when(favorietService.kanAanFavorietToevoegen("user")).thenReturn(true);
		
		mockMvc.perform(get("/event/1")).andExpect(status().isOk())
		.andExpect(view().name("views/eventDetails"))
		.andExpect(model().attributeExists("event", "isFavoriet", "kanAanFavorietToevoegen"))
		.andExpect(model().attribute("event", e1))
		.andExpect(model().attribute("isFavoriet", false))
		.andExpect(model().attribute("kanAanFavorietToevoegen", true));
		
		verify(eventService).findById(1L);
		verify(favorietService).isFavoriet("user", 1L);
		verify(favorietService).kanAanFavorietToevoegen("user");
	}
	
	@Test
	@WithMockUser
	public void testToonEventDetails_user_notFavorietEnKanFavorietNietToevoegen() throws Exception {
		when(eventService.findById(1L)).thenReturn(e1);
		when(favorietService.isFavoriet("user", 1L)).thenReturn(false);
		when(favorietService.kanAanFavorietToevoegen("user")).thenReturn(false);
		
		mockMvc.perform(get("/event/1")).andExpect(status().isOk())
		.andExpect(view().name("views/eventDetails"))
		.andExpect(model().attributeExists("event", "isFavoriet", "kanAanFavorietToevoegen"))
		.andExpect(model().attribute("event", e1))
		.andExpect(model().attribute("isFavoriet", false))
		.andExpect(model().attribute("kanAanFavorietToevoegen", false));
		
		verify(eventService).findById(1L);
		verify(favorietService).isFavoriet("user", 1L);
		verify(favorietService).kanAanFavorietToevoegen("user");
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testToonEventDetails_admin() throws Exception {
		when(eventService.findById(1L)).thenReturn(e1);
		
		mockMvc.perform(get("/event/1")).andExpect(status().isOk())
		.andExpect(view().name("views/eventDetails"))
		.andExpect(model().attributeExists("event"))
		.andExpect(model().attributeDoesNotExist("isFavoriet", "kanAanFavorietToevoegen"));
		
		verify(eventService).findById(1L);
	}
	
	@Test
	@WithMockUser
	public void testEventForm_userNoAccess() throws Exception {
		mockMvc.perform(get("/event/toevoegen")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetEventForm_admin() throws Exception {
		mockMvc.perform(get("/event/toevoegen")).andExpect(status().isOk())
			.andExpect(view().name("views/eventForm"))
			.andExpect(model().attributeExists("event", "lokalen"))
			.andExpect(model().attribute("lokalen", List.of(l1)));
	}
	
	@Test
	@WithMockUser
	public void testEditEventForm_userNoAccess() throws Exception {
		mockMvc.perform(get("/event/1/bewerken")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetEventForm_bewerken_admin() throws Exception {
		when(eventService.findById(1L)).thenReturn(e1);
		
		mockMvc.perform(get("/event/1/bewerken")).andExpect(status().isOk())
			.andExpect(view().name("views/eventForm"))
			.andExpect(model().attributeExists("event", "lokalen"))
			.andExpect(model().attribute("event", e1))
			.andExpect(model().attribute("lokalen", List.of(l1)));
		
		verify(eventService).findById(1L);
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testEvent_toevoegen_success() throws Exception {
		mockMvc.perform(post("/event/toevoegen").with(csrf()).flashAttr("event", e1))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/overzicht"));
		
		verify(eventService).validateInDB(e1);
		verify(sprekerService).saveIfNotExists(s1);
		verify(eventService).save(e1);
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testEvent_bewerken_success() throws Exception {
		mockMvc.perform(post("/event/1/bewerken").with(csrf()).flashAttr("event", e1))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/event/1"));
		
		verify(eventService).validateInDB(e1);
		verify(sprekerService).saveIfNotExists(s1);
		verify(eventService).save(e1);
	}
	
}
