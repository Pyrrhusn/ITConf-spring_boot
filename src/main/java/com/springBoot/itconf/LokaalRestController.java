package com.springBoot.itconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.LokaalService;

@RestController
@RequestMapping("/rest")
public class LokaalRestController {
	
	@Autowired
	private LokaalService lokaalService;
	
	@GetMapping("/lokaal/{naam}/capaciteit")
	public Integer getCapaciteitLokaalMetNaam(@PathVariable String naam) {
		return lokaalService.getCapaciteitLokaalMetNaam(naam);
	}
	
}
