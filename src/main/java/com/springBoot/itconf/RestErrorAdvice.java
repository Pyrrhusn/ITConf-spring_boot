package com.springBoot.itconf;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exception.EventDatumParseException;
import exception.LokaalNotFoundException;

@RestControllerAdvice
public class RestErrorAdvice {
	
	@ResponseBody
	@ExceptionHandler(EventDatumParseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String eventDatumParseHandler(EventDatumParseException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(LokaalNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String lokaalNotFoundHandler(LokaalNotFoundException ex) {
		return ex.getMessage();
	}
	
}
