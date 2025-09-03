package exception;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventOverlapException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final String lokaalNaam;
	private final LocalDate datum;
	private final LocalTime startTijdstip;
	private final LocalTime eindTijdstip;

}
