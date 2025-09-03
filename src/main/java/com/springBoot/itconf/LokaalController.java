package com.springBoot.itconf;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Lokaal;
import jakarta.validation.Valid;
import service.LokaalService;

@Controller
@RequestMapping("/lokaal")
public class LokaalController {
	
	@Autowired
	private LokaalService lokaalService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/toevoegen")
	private String showLokaalForm(Model model) {
		model.addAttribute("lokaal", new Lokaal());
		return "views/lokaalForm";
	}
	
	@PostMapping("/toevoegen")
	private String processLokaalForm(@Valid Lokaal lokaal, BindingResult result, Model model, Locale locale, RedirectAttributes ra) {
		if (result.hasErrors()) {
			return "views/lokaalForm";
		}
		
		lokaalService.save(lokaal);
		ra.addFlashAttribute("lokaalSaveSuccessMessage", messageSource.getMessage("lokaal.save.success.message", new Object[] {lokaal.getCapaciteit()} , locale));
		return "redirect:/lokaal/toevoegen";
	}
	
}
