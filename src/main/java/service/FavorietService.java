package service;

import java.util.List;

import domain.Event;

public interface FavorietService {

	List<Event> findAll(String username);

	void voegFavorietToe(String username, Long eventId);

	void verwijderFavoriet(String username, Long eventId);

	boolean isFavoriet(String username, Long eventId);

	boolean kanAanFavorietToevoegen(String username);

}