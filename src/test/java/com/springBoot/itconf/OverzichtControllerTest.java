package com.springBoot.itconf;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
import service.EventService;

@SpringBootTest
@AutoConfigureMockMvc
public class OverzichtControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private EventService eventService;
	
	@Test
	public void testRootRedirectToOverzicht() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/overzicht"));
	}
	
	@Test
	public void testOverzichtEvents() throws Exception {
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
		
		when(eventService.findAllSortDatumThenStartTijdstip()).thenReturn(List.of(e1, e2));
		
		mockMvc.perform(get("/overzicht")).andExpect(status().isOk())
			.andExpect(view().name("views/overzicht"))
			.andExpect(model().attributeExists("events"))
			.andExpect(model().attribute("events", List.of(e1, e2)));
	}
	
	@Test
	public void testIllegalArgumentException() throws Exception {
		doThrow(new IllegalArgumentException()).when(eventService).findAllSortDatumThenStartTijdstip();
		mockMvc.perform(get("/overzicht")).andExpect(view().name("error/error"));
	}

}
