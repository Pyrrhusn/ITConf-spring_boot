package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Lokaal;


public interface LokaalRepository extends JpaRepository<Lokaal, Long> {

	Optional<Lokaal> findByNaam(String naam);
	
	boolean existsByIgnoreCaseNaam(String naam);
	
}
