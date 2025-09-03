package service;

import java.util.List;

import domain.Event;

public interface EventService {

	List<Event> findAllSortDatumThenStartTijdstip();

	Event findById(Long id);

	void isEventOverlap(Event event);

	void isEventBestaatAl(Event event);

	void save(Event event);

	void validateInDB(Event event);

	List<Event> getEventsOpDatum(String datum);

}