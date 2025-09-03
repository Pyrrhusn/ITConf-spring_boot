package com.springBoot.itconf;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import service.EventService;
import service.FavorietService;
import service.LokaalService;
import service.SprekerService;

@Controller
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private LokaalService lokaalService;
	
	@Autowired
	private SprekerService sprekerService;
	
	@Autowired
	private FavorietService favorietService;
	
	@ModelAttribute("lokalen")
	private List<Lokaal> populateLokalen() {
		return lokaalService.findAll();
	}
	
	@GetMapping("/{id:[0-9]+}")
	private String showEventDetails(@PathVariable Long id, Model model, Principal principal, Authentication authentication) {
		model.addAttribute("event", eventService.findById(id));
		String username = principal.getName();
		
		if (authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_USER"))) {
			model.addAttribute("isFavoriet", favorietService.isFavoriet(username, id));
			model.addAttribute("kanAanFavorietToevoegen", favorietService.kanAanFavorietToevoegen(username));			
		}
		
		return "views/eventDetails";
	}
	
	@GetMapping("/toevoegen")
	private String showEventForm(Model model) {
		Event event = new Event();
		event.setSprekers(List.of(new Spreker()));
		model.addAttribute("event", event);
        return "views/eventForm";
	}
	
	@PostMapping("/toevoegen")
	private String processEventForm(@Valid Event event, BindingResult result, Model model) {
		if (result.hasErrors()) {
    		return "views/eventForm";
    	}
		
		eventService.validateInDB(event);
		event.getSprekers().forEach(sprekerService::saveIfNotExists);
		eventService.save(event);
		return "redirect:/overzicht";
	}
	
	@PostMapping(value = {"/toevoegen", "/{id:[0-9]+}/bewerken"}, params = {"addSpreker"})
	private String addSpreker(Event event, BindingResult result) {
		event.getSprekers().add(new Spreker());
        return "views/eventForm";
	}
	
	@PostMapping(value = {"/toevoegen", "/{id:[0-9]+}/bewerken"}, params = {"removeSpreker"})
	private String removeSpreker(Event event, BindingResult result, HttpServletRequest req) {
		Long sprekerId = Long.valueOf(req.getParameter("removeSpreker"));
		event.getSprekers().remove(sprekerId.intValue());
        return "views/eventForm";
	}
	
	@GetMapping("/{id:[0-9]+}/bewerken")
	private String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("event", eventService.findById(id));
		return "views/eventForm";
	}
	
	@PostMapping("/{id:[0-9]+}/bewerken")
	private String processEditForm(@PathVariable Long id, @Valid Event event, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "views/eventForm";
		}
		
		event.setId(id);
		eventService.validateInDB(event);
		event.getSprekers().forEach(sprekerService::saveIfNotExists);
		eventService.save(event);
		return "redirect:/event/" + id;
	}
	
}
