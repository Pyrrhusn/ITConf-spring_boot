package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Lokaal;
import repository.LokaalRepository;

public class LokaalNaamUniqueValidator implements Validator {
	
	@Autowired
	private LokaalRepository lokaalRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return Lokaal.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Lokaal lokaal = (Lokaal) target;
		
		if (lokaal.getNaam() == null || lokaal.getCapaciteit() == null) { //onnodig request naar db vermijden
			return;
		}
		
		if (lokaalRepository.existsByIgnoreCaseNaam(lokaal.getNaam())) {
			errors.rejectValue("naam", "lokaal.naam.uniek", "Geef een andere naam in");
		}
	}
	
}
