package service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import domain.Event;
import exception.EventBestaatAlException;
import exception.EventDatumParseException;
import exception.EventNotFoundException;
import exception.EventOverlapException;
import jakarta.transaction.Transactional;
import repository.EventRepository;

public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	@Transactional
	public List<Event> findAllSortDatumThenStartTijdstip() {
		return eventRepository.findAll(Sort.by("datum", "startTijdstip"));
	}
	
	@Override
	@Transactional
	public Event findById(Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));	
	}
	
	@Override
	@Transactional
	public void isEventOverlap(Event event) {
		if (eventRepository.isEventOverlapExcludingSelf(event.getId(), event.getLokaal(), event.getDatum(), event.getStartTijdstip(), event.getEindTijdstip()))
				throw new EventOverlapException(event.getLokaal().getNaam(), event.getDatum(), event.getStartTijdstip(), event.getEindTijdstip());
	}
	
	@Override
	@Transactional
	public void isEventBestaatAl(Event event) {
		if (eventRepository.bestaatEvenAlExcludingSelf(event.getId(), event.getNaam(), event.getDatum()))
			throw new EventBestaatAlException(event.getNaam(), event.getDatum());
	}
	
	@Override
	@Transactional
	public void save(Event event) {
		eventRepository.save(event);
	}
	
	@Override
	@Transactional
	public void validateInDB(Event event) {
		isEventOverlap(event);
		isEventBestaatAl(event);
	}
	
	@Override
	@Transactional
	public List<Event> getEventsOpDatum(String datum) {
		if (datum == null || datum.isBlank()) {
			throw new EventDatumParseException(datum);
		}
		
		try {
			LocalDate parsedDatum = LocalDate.parse(datum);
			List<Event> events = eventRepository.findByDatum(parsedDatum);
			events.sort(Comparator.comparing(Event::getStartTijdstip));
			return events;
		} catch (DateTimeParseException ex) {
			throw new EventDatumParseException(datum);
		}
	}
	
}
