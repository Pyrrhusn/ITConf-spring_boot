package com.springBoot.itconf;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	MessageSource messageSource;

	@GetMapping
	private String login(HttpServletRequest request, String error, Model model, Locale locale) {
		if (request.getParameter("error") != null) {
			model.addAttribute("error", messageSource.getMessage("login.form.error", null, locale));
		}
		if (request.getParameter("logout") != null) {
			model.addAttribute("msg", messageSource.getMessage("login.logout.msg", null, locale));
		}
		return "views/login";
	}
	
}
