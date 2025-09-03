package domain;

import static org.assertj.core.api.Assertions.assertThat;

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

public class LokaalTest {

	private Validator validator;
	
	@BeforeEach
	public void beforeEach() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	private Lokaal createLokaal(String naam, Integer capaciteit) {
		return Lokaal.builder().naam(naam).capaciteit(capaciteit).build();
	}
	
	private static Stream<Arguments> validLokaalData() {
		return Stream.of(Arguments.of("L123", 25),
						Arguments.of("L456", 12),
						Arguments.of("F345", 50));
	}
	
	@ParameterizedTest
	@MethodSource("validLokaalData")
	public void testValidLokaal(String naam, Integer capaciteit) {
		Lokaal validLokaal = createLokaal(naam, capaciteit);
		Set<ConstraintViolation<Lokaal>> violations = validator.validate(validLokaal);
		assertThat(violations).isEmpty();
	}
	
	private static Stream<Arguments> invalidLokaalData() {
		return Stream.of(Arguments.of("L12", 25, "naam"),
						Arguments.of("L123", null, "capaciteit"),
						Arguments.of(null, 25, "naam"),
						Arguments.of("5678", 25, "naam"),
						Arguments.of("L567", 100, "capaciteit"),
						Arguments.of("L567", 0, "capaciteit"));
	}
	
	@ParameterizedTest
	@MethodSource("invalidLokaalData")
	public void testInvalidLokaalData(String naam, Integer capaciteit, String expected) {
		Lokaal invalidLokaal = Lokaal.builder().naam(naam).capaciteit(capaciteit).build();
		Set<ConstraintViolation<Lokaal>> violations = validator.validate(invalidLokaal);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
	}
	
}
