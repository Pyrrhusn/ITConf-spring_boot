package validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventDatumInPeriodeValidator implements ConstraintValidator<EventDatumInPeriode, LocalDate> {
	
	@Autowired
	private MessageSource messageSource;
	
	private LocalDate begin;
	private LocalDate eind;
	
	@Override
	public void initialize(EventDatumInPeriode constraintAnnotation) {
		begin = LocalDate.parse(constraintAnnotation.begin());
		eind = LocalDate.parse(constraintAnnotation.eind());
	}
	
	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		boolean isValid = !value.isBefore(begin) && !value.isAfter(eind);
		if (!isValid) {
			Locale locale = LocaleContextHolder.getLocale();
			String pattern = messageSource.getMessage("date.format.pattern", null, locale);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			String beginFormatted = begin.format(formatter);
			String eindFormatted = eind.format(formatter);
			String resourceKey = context.getDefaultConstraintMessageTemplate();
			if (resourceKey.startsWith("{") && resourceKey.endsWith("}")) {
				resourceKey = resourceKey.substring(1, resourceKey.length() - 1);
			}
			String message = messageSource.getMessage(resourceKey, new Object[] {beginFormatted, eindFormatted}, locale);
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		}
		
		return isValid;
	}

}
