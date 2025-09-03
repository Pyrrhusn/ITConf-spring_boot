package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.MyUser;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {

	MyUser findByUsername(String name);
	
//	boolean existsById(Long id);
//	
//	@Query("""
//			SELECT COUNT(e) > 0
//			FROM Users u JOIN u.favorietEvents e
//			WHERE u.id = :userId AND e.id = :eventId
//			""")
//	boolean isEventFavoriet(@Param("userId") Long userId, @Param("eventId") Long eventId);
//	
//	@Query("""
//			SELECT COUNT(e) 
//			FROM Users u JOIN u.favorietEvents e
//			WHERE u.id = :userId
//			""")
//	Integer countFavorietEvents(@Param("userId") Long userId);
	
}
