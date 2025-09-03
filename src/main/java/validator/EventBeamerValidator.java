package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventBeamerValidator implements ConstraintValidator<ValidEventBeamer, Event> {

	@Override
	public void initialize(ValidEventBeamer constraintAnnotation) {
	}

	@Override
	public boolean isValid(Event event, ConstraintValidatorContext context) {
		if (event.getBeamercode() == null || event.getBeamercheck() == null) {
			return true;
		}
		
		Integer beamercode = Integer.parseInt(event.getBeamercode());
		Integer beamercheck = Integer.parseInt(event.getBeamercheck());
		
		if (beamercode % 97 != beamercheck) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{event.beamercheck.validation}")
				.addPropertyNode("beamercheck").addConstraintViolation();
			
			return false;
		}
		
		return true;
	}

}
