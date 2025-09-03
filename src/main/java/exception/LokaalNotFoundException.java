package exception;

public class LokaalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public LokaalNotFoundException(String naam) {
		super(String.format("Geen lokaal gevonden met naam '%s'", naam));
	}

}
