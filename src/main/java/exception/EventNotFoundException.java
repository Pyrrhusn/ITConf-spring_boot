package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final Long id;
}
