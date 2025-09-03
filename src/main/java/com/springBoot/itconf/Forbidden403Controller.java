package com.springBoot.itconf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Forbidden403Controller { //nodig om gebruikers naam en rol uit de global model attribuut te kunnen tonen op 403 pagina
	
	@GetMapping("/403")
	public String show403Page() {
		return "error/403";
	}
	
}
