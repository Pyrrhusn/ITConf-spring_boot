package com.springBoot.itconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import service.EventService;

@Controller
@RequestMapping("/overzicht")
public class OverzichtController {
	
	@Autowired
	private EventService eventService;
	
	@GetMapping
    private String showOverview(Model model) {
		model.addAttribute("events", eventService.findAllSortDatumThenStartTijdstip());
		return "views/overzicht";
    }
}
