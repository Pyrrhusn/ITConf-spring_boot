package validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Event;
import domain.Spreker;

public class EventSprekersUniqueValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Event.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Event event = (Event) target;
		List<Spreker> sprekers = event.getSprekers();
		
		if (sprekers == null || sprekers.size() < 2) {
			return;
		}
		
		Set<String> namen = sprekers.stream().map(s -> s.getNaam() != null ? s.getNaam().toLowerCase() : null).collect(Collectors.toSet());
		if (namen.contains(null)) {
			return;
		}
		
		if (namen.size() != sprekers.size()) {
			errors.rejectValue("sprekers", "event.sprekers.uniek", "default.eventSprekers.uniek");			
		}
	}

}
