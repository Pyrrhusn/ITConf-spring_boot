package service;

import java.util.List;

import domain.Lokaal;

public interface LokaalService {

	List<Lokaal> findAll();

	void save(Lokaal lokaal);

	boolean existsByIgnoreCaseNaam(String naam);

	Integer getCapaciteitLokaalMetNaam(String naam);

}