package com.springBoot.itconf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
import exception.EventDatumParseException;
import service.EventService;
import utils.InitFormatter;

@SpringBootTest
public class EventRestMockTest {

	@Mock
	private EventService mock;
	
	private EventRestController controller;
	private MockMvc mockMvc;
	
	private static final Spreker s1 = new Spreker("S1");
	private static final Spreker s2 = new Spreker("S2");
	private static final Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build(); 
	
	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new EventRestController();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "eventService", mock);
	}
	
	@Test
	public void testGetAllEventsOp2025_04_14_noEmptyList() throws Exception {
		Event e1 = Event.builder()
				.id(1L).naam("Test1").beschrijving("Beschrijving1")
				.sprekers(List.of(s1, s2)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
				.startTijdstip(LocalTime.of(10, 0)).eindTijdstip(LocalTime.of(12, 0))
				.beamercode("0123").beamercheck("26").prijs(12.34).build();
		String expectedFormattedDatum = e1.getDatum().format(InitFormatter.LOCAL_DATE_FORMATTER);
		String expectedFormattedStartTijdstip = e1.getStartTijdstip().format(InitFormatter.LOCAL_TIME_FORMATTER);
		String expectedFormattedEindTijdstip = e1.getEindTijdstip().format(InitFormatter.LOCAL_TIME_FORMATTER);
		
		Event e2 = Event.builder()
				.id(2L).naam("Test2").beschrijving("Beschrijving2")
				.sprekers(List.of(s1, s2)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
				.startTijdstip(LocalTime.of(12, 30)).eindTijdstip(LocalTime.of(14, 30))
				.beamercode("0123").beamercheck("26").prijs(12.34).build();
		String expectedFormattedDatum2 = e2.getDatum().format(InitFormatter.LOCAL_DATE_FORMATTER);
		String expectedFormattedStartTijdstip2 = e2.getStartTijdstip().format(InitFormatter.LOCAL_TIME_FORMATTER);
		String expectedFormattedEindTijdstip2 = e2.getEindTijdstip().format(InitFormatter.LOCAL_TIME_FORMATTER);

		Mockito.when(mock.getEventsOpDatum("2025-04-14")).thenReturn(List.of(e1, e2));
		
		mockMvc.perform(get("/rest/events/2025-04-14")).andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isNotEmpty())
			// e1
			.andExpect(jsonPath("$[0].event_id").value(1L))
			.andExpect(jsonPath("$[0].naam").value("Test1"))
			.andExpect(jsonPath("$[0].beschrijving").value("Beschrijving1"))
			.andExpect(jsonPath("$[0].sprekers").isArray())
			.andExpect(jsonPath("$[0].sprekers").isNotEmpty())
			.andExpect(jsonPath("$[0].sprekers[0].spreker_naam").value("S1"))
			.andExpect(jsonPath("$[0].sprekers[1].spreker_naam").value("S2"))
			.andExpect(jsonPath("$[0].lokaal").isNotEmpty())
			.andExpect(jsonPath("$[0].lokaal.lokaal_id").value(1L))
			.andExpect(jsonPath("$[0].lokaal.lokaal_naam").value("L123"))
			.andExpect(jsonPath("$[0].lokaal.capaciteit").value(25))
			.andExpect(jsonPath("$[0].datum").value(expectedFormattedDatum))
			.andExpect(jsonPath("$[0].startTijdstip").value(expectedFormattedStartTijdstip))
			.andExpect(jsonPath("$[0].eindTijdstip").value(expectedFormattedEindTijdstip))
			.andExpect(jsonPath("$[0].beamercode").value("0123"))
			.andExpect(jsonPath("$[0].beamercheck").value("26"))
			.andExpect(jsonPath("$[0].prijs").value(12.34))
			// e2
			.andExpect(jsonPath("$[1].event_id").value(2L))
			.andExpect(jsonPath("$[1].naam").value("Test2"))
			.andExpect(jsonPath("$[1].beschrijving").value("Beschrijving2"))
			.andExpect(jsonPath("$[1].sprekers").isArray())
			.andExpect(jsonPath("$[1].sprekers").isNotEmpty())
			.andExpect(jsonPath("$[1].sprekers[0].spreker_naam").value("S1"))
			.andExpect(jsonPath("$[1].sprekers[1].spreker_naam").value("S2"))
			.andExpect(jsonPath("$[1].lokaal").isNotEmpty())
			.andExpect(jsonPath("$[1].lokaal.lokaal_id").value(1L))
			.andExpect(jsonPath("$[1].lokaal.lokaal_naam").value("L123"))
			.andExpect(jsonPath("$[1].lokaal.capaciteit").value(25))
			.andExpect(jsonPath("$[1].datum").value(expectedFormattedDatum2))
			.andExpect(jsonPath("$[1].startTijdstip").value(expectedFormattedStartTijdstip2))
			.andExpect(jsonPath("$[1].eindTijdstip").value(expectedFormattedEindTijdstip2))
			.andExpect(jsonPath("$[1].beamercode").value("0123"))
			.andExpect(jsonPath("$[1].beamercheck").value("26"))
			.andExpect(jsonPath("$[1].prijs").value(12.34));
		
		Mockito.verify(mock).getEventsOpDatum("2025-04-14");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"2025-06-13", "2025-05-01"})
	public void testGetAllEventsOpDatumsZonderEvents_emptyList(String datum) throws Exception {
		Mockito.when(mock.getEventsOpDatum(datum)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/rest/events/" + datum))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty());
		
		Mockito.verify(mock).getEventsOpDatum(datum);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"2025", "2025-04", "14-04-2025", "TEST", "aa-bb-cc"})
	public void testGetAllEventsOpDatums_foutDatumFormaat_throwsParsingException(String datum) throws Exception {
		Mockito.when(mock.getEventsOpDatum(datum)).thenThrow(new EventDatumParseException(datum));
		
		Exception exception = assertThrows(Exception.class,
				() -> {mockMvc.perform(get("/rest/events/" + datum)).andReturn();});
		
		assertTrue(exception.getCause() instanceof EventDatumParseException);
		Mockito.verify(mock).getEventsOpDatum(datum);
	}
	
	@Test
    public void testGetAllEventsOpDatum_nullOfLeegDatum() throws Exception {
        mockMvc.perform(get("/rest/events/")) // geen datum in uri
                .andExpect(status().isNotFound());
    }
}
