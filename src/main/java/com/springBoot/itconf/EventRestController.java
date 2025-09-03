package com.springBoot.itconf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Event;
import service.EventService;

@RestController
@RequestMapping("/rest")
public class EventRestController {
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/events/{datum}")
	public List<Event> getEventsOpDatum(@PathVariable String datum) { //String ipv LocalDate om te parsen in service en custom exception te gooien
		return eventService.getEventsOpDatum(datum);
	}
	
}