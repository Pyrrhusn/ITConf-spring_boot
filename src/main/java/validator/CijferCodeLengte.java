package validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CijferCodeLengteValidator.class)
public @interface CijferCodeLengte {

	int lengte();
	
	String message() default "{default.cijferCodeLengte}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
