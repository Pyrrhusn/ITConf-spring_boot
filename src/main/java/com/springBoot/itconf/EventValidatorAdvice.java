package com.springBoot.itconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(assignableTypes = EventController.class)
public class EventValidatorAdvice {
	
	@Autowired
	private Validator eventTijdValidator;
	
	@Autowired
	private Validator eventSprekersUniqueValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(eventTijdValidator, eventSprekersUniqueValidator);
	}
	
}
