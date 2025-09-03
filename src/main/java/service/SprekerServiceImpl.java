package service;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Spreker;
import jakarta.transaction.Transactional;
import repository.SprekerRepository;

public class SprekerServiceImpl implements SprekerService {
	
	@Autowired
	private SprekerRepository sprekerRepository;
	
	@Override
	@Transactional
	public void save(Spreker spreker) {
		sprekerRepository.save(spreker);
	}
	
	@Override
	@Transactional
	public boolean existsByNaamIgnoreCase(String naam) {
		return sprekerRepository.existsByNaamIgnoreCase(naam);
	}
	
	@Override
	@Transactional
	public boolean existsById(String naam) {
		return sprekerRepository.existsById(naam);
	}
	
	@Override
	@Transactional
	public void saveAll(Iterable<Spreker> sprekers) {
		sprekerRepository.saveAll(sprekers);
	}
	
	@Override
	@Transactional
	public void saveIfNotExists(Spreker s) {
		if (!existsById(s.getNaam())) {
			save(s);
		}
	}
	
}
