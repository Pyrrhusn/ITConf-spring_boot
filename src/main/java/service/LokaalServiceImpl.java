package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Lokaal;
import exception.LokaalNotFoundException;
import jakarta.transaction.Transactional;
import repository.LokaalRepository;

public class LokaalServiceImpl implements LokaalService {

	@Autowired
	private LokaalRepository lokaalRepository;
	
	@Override
	@Transactional
	public List<Lokaal> findAll() {
		return lokaalRepository.findAll();
	}
	
	@Override
	@Transactional
	public void save(Lokaal lokaal) {
		lokaalRepository.save(lokaal);
	}
	
	@Override
	@Transactional
	public boolean existsByIgnoreCaseNaam(String naam) {
		return lokaalRepository.existsByIgnoreCaseNaam(naam);
	}

	@Override
	@Transactional
	public Integer getCapaciteitLokaalMetNaam(String naam) {
		if (naam == null || naam.isBlank()) {
			throw new LokaalNotFoundException(naam);
		}
		
		return lokaalRepository.findByNaam(naam).orElseThrow(() -> new LokaalNotFoundException(naam)).getCapaciteit();
	}
	
}
