package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CijferCodeLengteValidator implements ConstraintValidator<CijferCodeLengte, String> {

	private int lengte;
	
	@Override
	public void initialize(CijferCodeLengte constraintAnnotation) {
		lengte = constraintAnnotation.lengte();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		boolean isValid = value.matches("^\\d{" + lengte + "}$");
		
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
		}
		return isValid;
	}

}
