package exception;

public class EventDatumParseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EventDatumParseException(String datum) {
		super(String.format("Fout bij parsen van datum '%s', verwacht formaat is 'yyyy-MM-dd'", datum));
	}
	
}
