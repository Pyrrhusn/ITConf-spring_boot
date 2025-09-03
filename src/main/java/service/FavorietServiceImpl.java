package service;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Event;
import domain.MyUser;
import exception.UserMaxFavorietenException;
import jakarta.transaction.Transactional;

public class FavorietServiceImpl implements FavorietService {
	
	private static final int MAX_FAVORIETEN = 2;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private EventService eventService;
	
	//TODO db or user entiteit??
	
	@Override
	@Transactional
	public List<Event> findAll(String username) {
		List<Event> favorieten = myUserDetailsService.findByUsername(username).getFavorietEvents();
		favorieten.sort(Comparator.comparing(Event::getDatum).thenComparing(Event::getStartTijdstip).thenComparing(Event::getNaam));
		return favorieten;
	}
	
	@Override
	@Transactional
	public void voegFavorietToe(String username, Long eventId) {		
		Event event = eventService.findById(eventId);
		MyUser user = myUserDetailsService.findByUsername(username);

		if (user.getFavorietEvents().size() >= MAX_FAVORIETEN) {
			throw new UserMaxFavorietenException();
		}
		
		user.getFavorietEvents().add(event);
		myUserDetailsService.save(user);
	}
	
	@Override
	@Transactional
	public void verwijderFavoriet(String username, Long eventId) {
		MyUser user = myUserDetailsService.findByUsername(username);
		user.getFavorietEvents().removeIf(e -> e.getId() == eventId);
		myUserDetailsService.save(user);
	}
	
	@Override
	@Transactional
	public boolean isFavoriet(String username, Long eventId) {
		MyUser user = myUserDetailsService.findByUsername(username);
		return user.getFavorietEvents().stream().filter(e -> e.getId() == eventId).findFirst().isPresent();
	}
	
	@Override
	@Transactional
	public boolean kanAanFavorietToevoegen(String username) {
//		return myUserRepository.countFavorietEvents(userId);
		
		return myUserDetailsService.findByUsername(username).getFavorietEvents().size() < MAX_FAVORIETEN;
	}
	
}
