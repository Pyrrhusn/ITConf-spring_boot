package exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventBestaatAlException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String naam;
	private final LocalDate datum;

}
