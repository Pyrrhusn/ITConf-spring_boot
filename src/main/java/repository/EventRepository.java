package repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import domain.Event;
import domain.Lokaal;
import java.util.List;



public interface EventRepository extends JpaRepository<Event, Long> {
	
	@Query("""
			SELECT COUNT(e) > 0 FROM Event e
			WHERE (:id IS NULL OR e.id <> :id) 
			AND e.lokaal = :lokaal
			AND e.datum = :datum
			AND (:startTijdstip < e.eindTijdstip AND :eindTijdstip > e.startTijdstip)
			""")
	boolean isEventOverlapExcludingSelf(@Param("id") Long id, @Param("lokaal") Lokaal lokaal, @Param("datum") LocalDate datum, @Param("startTijdstip") LocalTime startTijdstip, @Param("eindTijdstip") LocalTime eindTijdstip);
	
	@Query("""
			SELECT COUNT(e) > 0 FROM Event e
			WHERE (:id IS NULL OR e.id <> :id)
			AND LOWER(e.naam) = LOWER(:naam)
			AND e.datum = :datum
			""")
	boolean bestaatEvenAlExcludingSelf(@Param("id") Long id, @Param("naam") String naam, @Param("datum") LocalDate datum);
	
	List<Event> findByDatum(LocalDate datum);
	
}
