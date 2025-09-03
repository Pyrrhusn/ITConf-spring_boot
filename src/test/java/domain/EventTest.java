package domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EventTest {

	private Validator validator;
	
	@BeforeEach
	public void beforeEach() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	private Event createEvent(String naam, List<Spreker> sprekers, Lokaal lokaal,
			LocalDate datum, LocalTime startTijdstip, LocalTime eindTijdstip,
			String beamercode, String beamercheck, Double prijs) {
		return Event.builder().naam(naam).beschrijving("TEST").sprekers(sprekers).lokaal(lokaal).datum(datum)
				.startTijdstip(startTijdstip).eindTijdstip(eindTijdstip).beamercheck(beamercheck)
				.beamercode(beamercode).prijs(prijs).build();
	}
	
	private static Stream<Arguments> validEventData() {
		Spreker s1 = new Spreker("S1");
		Spreker s2 = new Spreker("S2");
		Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build();
		
		return Stream.of(Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
				LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", 12.34));
	}
	
	@ParameterizedTest
	@MethodSource("validEventData")
	public void testValidEvent(String naam, List<Spreker> sprekers, Lokaal lokaal,
			LocalDate datum, LocalTime startTijdstip, LocalTime eindTijdstip,
			String beamercode, String beamercheck, Double prijs) {
		Event validEvent = createEvent(naam, sprekers, lokaal, datum, startTijdstip, eindTijdstip,
				beamercode, beamercheck, prijs);
		Set<ConstraintViolation<Event>> violations = validator.validate(validEvent);
		assertThat(violations).isEmpty();
	}
	
	private static Stream<Arguments> invalidEventData() {
		Spreker s1 = new Spreker("S1");
		Spreker s2 = new Spreker("S2");
		Lokaal l1 = Lokaal.builder().id(1L).naam("L123").capaciteit(25).build();
		
		return Stream.of(
				Arguments.of(null, List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
				LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", 12.34, "naam"),
				Arguments.of("Test1", null, l1, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", 12.34, "sprekers"),
				Arguments.of("Test1", List.of(s1, s2), null, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", 12.34, "lokaal"),
				Arguments.of("Test1", List.of(s1, s2), l1, null,
						LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", 12.34, "datum"),
				Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
						null, LocalTime.of(12, 0), "0123", "26", 12.34, "startTijdstip"),
				Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), null, "0123", "26", 12.34, "eindTijdstip"),
				Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), LocalTime.of(12, 0), null, "26", 12.34, "beamercode"),
				Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", null, 12.34, "beamercheck"),
				Arguments.of("Test1", List.of(s1, s2), l1, LocalDate.of(2025, 4, 14),
						LocalTime.of(10, 0), LocalTime.of(12, 0), "0123", "26", null, "prijs")
				);
	}
	
	@ParameterizedTest
	@MethodSource("invalidEventData")
	public void testInvalidEvent(String naam, List<Spreker> sprekers, Lokaal lokaal,
			LocalDate datum, LocalTime startTijdstip, LocalTime eindTijdstip,
			String beamercode, String beamercheck, Double prijs, String expected) {
		Event validEvent = createEvent(naam, sprekers, lokaal, datum, startTijdstip, eindTijdstip,
				beamercode, beamercheck, prijs);
		Set<ConstraintViolation<Event>> violations = validator.validate(validEvent);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
	}
	
}
