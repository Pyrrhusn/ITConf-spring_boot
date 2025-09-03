package com.springBoot.itconf;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import exception.EventBestaatAlException;
import exception.EventNotFoundException;
import exception.EventOverlapException;
import exception.UserMaxFavorietenException;

@ControllerAdvice
public class GlobalControllerAdvcie {
	
	@Autowired
	private MessageSource messageSource;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	@ExceptionHandler(EventOverlapException.class)
	public String handleEventOverlap(EventOverlapException ex, Model model, Locale locale) {
		String errorMessage = messageSource.getMessage("error.eventOverlap", new Object[] {ex.getDatum(), ex.getLokaalNaam(), ex.getStartTijdstip(), ex.getEindTijdstip()}, locale);
		model.addAttribute("errorMessage", errorMessage);
		return "error/error";
	}
	
	@ExceptionHandler(EventBestaatAlException.class)
	public String handleEventBestaatAl(EventBestaatAlException ex, Model model, Locale locale) {
		String errorMessage = messageSource.getMessage("error.eventBestaatAl", new Object[] {ex.getNaam(), ex.getDatum()}, locale);
		model.addAttribute("errorMessage", errorMessage);
		return "error/error";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error";
    }
	
	@ExceptionHandler(EventNotFoundException.class)
	public String handleEventNotFoundException(EventNotFoundException ex, Model model, Locale locale) {
		ex.printStackTrace();
//		String errorMessage = messageSource.getMessage("error.event.notFound.byId", new Object[] {ex.getId()}, locale);
//		model.addAttribute("errorMessage", errorMessage);
		return "error/error";
	}
	
	@ExceptionHandler(UserMaxFavorietenException.class)
	public String handleUserMaxFavorietenException(UserMaxFavorietenException ex, Model model, Locale locale) {
		ex.printStackTrace();
//		String errorMessage = messageSource.getMessage("error.user.favorieten.max", null, locale);
//		model.addAttribute("errorMessage", errorMessage);
		return "error/error";
	}
	
	@ModelAttribute
	public void populateLoggedInUserAttributes(Authentication authentication, Model model) {
		if (authentication != null && authentication.isAuthenticated()) {
			model.addAttribute("username", authentication.getName());
			
			String userRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findAny().orElse("ROLE_USER").substring(5);
			model.addAttribute("userRole", userRole);
		}
	}

}
