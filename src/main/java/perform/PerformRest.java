package perform;

import java.util.stream.Collectors;

import org.springframework.web.reactive.function.client.WebClient;

import domain.Event;
import domain.Spreker;
import reactor.core.publisher.Mono;

public class PerformRest {
	
	private final String SERVER_URI = "http://localhost:8080/rest";
	private WebClient webClient = WebClient.create();
	
	public PerformRest() throws Exception {
		System.out.println("\n-----------Events op 2025-04-14-----------");
		getEventsOpDatum("2025-04-14");
		System.out.println("\n-----------Events op 2025-04-17-----------");
		getEventsOpDatum("2025-04-17");
		System.out.println("\n-----------Events op 2025-09-18 (lege response)-----------");
		getEventsOpDatum("2025-09-18");
		System.out.println("\n-----------Events op TEST (foute parsed datum)-----------");
		try {
			getEventsOpDatum("TEST");	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("\n-----------Lokaal B312 Capaciteit-----------");
		getLokaalCapaciteitMetNaam("B312");
		System.out.println("\n-----------Lokaal c123 Capaciteit-----------");
		getLokaalCapaciteitMetNaam("c123");
		System.out.println("\n-----------Lokaal C123 Capaciteit (case insensitive)-----------");
		getLokaalCapaciteitMetNaam("C123");
		System.out.println("\n-----------Lokaal T567 Capaciteit (lokaal bestaat niet)-----------");
		try {
			getLokaalCapaciteitMetNaam("T567");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("\n-----------Lokaal TEST Capaciteit (geen lokaal naam/nummer)-----------");
		try {
			getLokaalCapaciteitMetNaam("TEST");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void printEventData(Event e) {
		String sprekers = e.getSprekers().stream().map(Spreker::getNaam).collect(Collectors.joining(", ", "[", "]"));
		System.out.printf(
				"Event %s: naam = %s, beschrijving = %s, sprekers = %s, lokaal = %s, datum = %s, startTijdstip = %s, eindTijdstip = %s, beamercode = %s, beamercheck = %s, prijs = %s%n",
				e.getId(),
				e.getNaam(),
				e.getBeschrijving(),
				sprekers,
				e.getLokaal().getNaam(),
				e.getDatum(),
				e.getStartTijdstip(),
				e.getEindTijdstip(),
				e.getBeamercode(),
				e.getBeamercheck(),
				e.getPrijs()
           );
	}
	
	private void printLokaalCapaciteit(Integer capaciteit) {
		System.out.printf("Lokaal heeft een capaciteit van %d.", capaciteit);
	}
	
	private void getEventsOpDatum(String datum) {
		webClient.get().uri(SERVER_URI + "/events/" + datum)
		.retrieve()
		.bodyToFlux(Event.class)
		.flatMap(e -> {
			printEventData(e);
			return Mono.empty();
		})
		.blockLast();
	}
	
	private void getLokaalCapaciteitMetNaam(String naam) {
		webClient.get().uri(SERVER_URI + "/lokaal/" + naam + "/capaciteit")
        .retrieve()
        .bodyToMono(Integer.class)
        .doOnSuccess(this::printLokaalCapaciteit)
        .block();
	}
	
}
