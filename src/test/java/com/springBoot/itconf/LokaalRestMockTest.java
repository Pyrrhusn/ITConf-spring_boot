package com.springBoot.itconf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import exception.LokaalNotFoundException;
import service.LokaalService;

@SpringBootTest
public class LokaalRestMockTest {

	@Mock
	private LokaalService mock;
	
	private LokaalRestController controller;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new LokaalRestController();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "lokaalService", mock);
	}
	
	@ParameterizedTest
	@CsvSource({"B312,40", "b312,40", "c567,25"})
	public void testGetLokaalCapaciteit_isOk(String lokaalNaam, String expectedCapaciteit) throws Exception {
		Mockito.when(mock.getCapaciteitLokaalMetNaam(lokaalNaam)).thenReturn(Integer.valueOf(expectedCapaciteit));
		
		mockMvc.perform(get("/rest/lokaal/" + lokaalNaam + "/capaciteit"))
			.andExpect(status().isOk())
			.andExpect(content().string(expectedCapaciteit));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"N567", "TEST"})
	public void testGetOnbestaandLokaalCapaciteit_notFound(String lokaalNaam) throws Exception {
		Mockito.when(mock.getCapaciteitLokaalMetNaam(lokaalNaam)).thenThrow(new LokaalNotFoundException(lokaalNaam));
		
		Exception exception = assertThrows(Exception.class,
				() -> {mockMvc.perform(get("/rest/lokaal/" + lokaalNaam + "/capaciteit")).andReturn();});
		
		assertTrue(exception.getCause() instanceof LokaalNotFoundException);
		Mockito.verify(mock).getCapaciteitLokaalMetNaam(lokaalNaam);
		
	}
	
	@Test
    public void testGetLokaalCapaciteit_nullOfLeegNaam() throws Exception {
        mockMvc.perform(get("/rest/lokaal/capaciteit")) // geen naam in uri
                .andExpect(status().isNotFound());
    }
	
}
