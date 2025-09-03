package com.springBoot.itconf;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class NotFoundController implements ErrorController {
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public String handleError(HttpServletRequest request, Model model, Locale locale) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			
			if(statusCode == 404) {
				return "error/notFound";
			} else {
				model.addAttribute("errorCode", statusCode.toString());
				model.addAttribute("errorMessage", messageSource.getMessage("error.system.generic", null, locale));
				return "error/error";
			}
		}
		
		return "redirect:/"; // redirect naar landing pagina
	}
	
}