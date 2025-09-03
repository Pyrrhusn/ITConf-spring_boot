package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Spreker;

public interface SprekerRepository extends JpaRepository<Spreker, String> {
	
	boolean existsByNaamIgnoreCase(String naam);
	
}
