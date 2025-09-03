package com.springBoot.itconf;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.FavorietService;

@Controller
@RequestMapping("/favorieten")
public class FavorietController {
	
	@Autowired
	private FavorietService favorietService;
	
	@GetMapping
	public String showFavorieten(Model model, Principal principal) {
		model.addAttribute("favorieten", favorietService.findAll(principal.getName()));
		return "views/favorieten";
	}
	
	@PostMapping
	public String addFavorietEvent(@RequestParam Long eventId, @RequestParam String action, @RequestParam String location, Principal principal) {
		String username = principal.getName();
		
		if ("add".equalsIgnoreCase(action)) {
			favorietService.voegFavorietToe(username, eventId);		
		} else if ("remove".equalsIgnoreCase(action)) {
			favorietService.verwijderFavoriet(username, eventId);
		}
		
		if (location != null && "favorieten".equalsIgnoreCase(location)) {
			return "redirect:/favorieten";
		}
		
		return "redirect:/";
	}
	
}
