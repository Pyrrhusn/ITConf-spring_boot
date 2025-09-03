package com.springBoot.itconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(assignableTypes = LokaalController.class)
public class LokaalValidatorAdvice {
	
	@Autowired
	private Validator lokaalNaamUniqueValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(lokaalNaamUniqueValidator);
	}

}
