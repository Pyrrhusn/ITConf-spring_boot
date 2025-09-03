package validator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EventDatumInPeriodeValidator.class)
public @interface EventDatumInPeriode {
	
	String begin();
	
	String eind();
	
	String message() default "{default.eventDatumInPeriode}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
