package service;

import domain.Spreker;

public interface SprekerService {

	void save(Spreker spreker);

	boolean existsByNaamIgnoreCase(String naam);

	boolean existsById(String naam);

	void saveAll(Iterable<Spreker> sprekers);

	void saveIfNotExists(Spreker s);

}