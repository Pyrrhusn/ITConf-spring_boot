package validator;

import java.time.LocalTime;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Event;

public class EventTijdValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Event.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Event event = (Event) target;
		LocalTime startTijdstip = event.getStartTijdstip();
		LocalTime eindTijdstip = event.getEindTijdstip();
		
		if (startTijdstip == null || eindTijdstip == null) {
			return;
		}
		
		if (startTijdstip.isAfter(eindTijdstip)) {
			errors.rejectValue("startTijdstip", "event.startTijstip.naEind", "default.eventTijd");
		}
		
		if (eindTijdstip.isBefore(startTijdstip)) {
			errors.rejectValue("eindTijdstip", "event.eindTijdstip.voorStart", "default.eventTijd");
		}
	
	}

}
