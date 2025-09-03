package com.springBoot.itconf;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import domain.Event;
import domain.Lokaal;
import domain.MyUser;
import domain.Role;
import domain.Spreker;
import repository.EventRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import repository.MyUserRepository;

@Component
public class InitDataConfig implements CommandLineRunner {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private LokaalRepository lokaalRepository;
	
	@Autowired
	private SprekerRepository sprekerRepository;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private MyUserRepository myUserRepository;
	
	@Override
	public void run(String... args) {

		Lokaal l1 = Lokaal.builder().naam("B312").capaciteit(25).build();
		Lokaal l2 = Lokaal.builder().naam("c123").capaciteit(40).build();
		Lokaal l3 = Lokaal.builder().naam("b102").capaciteit(1).build();
		
		lokaalRepository.save(l1);
		lokaalRepository.save(l2);
		lokaalRepository.save(l3);
		
		Spreker s1 = new Spreker("AB");
		Spreker s2 = new Spreker("HK");
		Spreker s3 = new Spreker("PL");
		Spreker s4 = new Spreker("Myself");
		Spreker s5 = new Spreker("Glados");
		Spreker s6 = new Spreker("Skynet");
		
		sprekerRepository.saveAll(List.of(s1, s2, s3, s4, s5, s6));
		
		Event e1 = Event.builder().naam("How to Kubernetes").beschrijving("Kubernetes")
					.sprekers(List.of(s1, s2, s3)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
					.startTijdstip(LocalTime.of(12, 30)).eindTijdstip(LocalTime.of(14, 30)).beamercode("0123").beamercheck("26").prijs(9.99).build();
		Event e2 = Event.builder().naam("Solitude").beschrijving(null).sprekers(List.of(s4))
					.lokaal(l2).datum(LocalDate.of(2025, 4, 17)).startTijdstip(LocalTime.of(8, 30)).eindTijdstip(LocalTime.of(11, 30))
					.beamercode("4567").beamercheck("08").prijs(42.00).build();
		Event e3 = Event.builder().naam("AI: Threat 2 humanity?").beschrijving("Let's welcome our overlords! As artificial intelligence advances at breakneck speed, the question arises: is AI a tool for progress or a potential threat to our very existence?")
					.sprekers(List.of(s5, s6)).lokaal(l3).datum(LocalDate.of(2025, 4, 18))
					.startTijdstip(LocalTime.of(10, 30)).eindTijdstip(LocalTime.of(15, 0)).beamercode("9999").beamercheck("08").prijs(19.84).build();
		
		Event e4 = Event.builder().naam("Same date").beschrijving("Rest Test")
				.sprekers(List.of(s5, s4, s3)).lokaal(l1).datum(LocalDate.of(2025, 4, 14))
				.startTijdstip(LocalTime.of(10, 30)).eindTijdstip(LocalTime.of(12, 0)).beamercode("6379").beamercheck("74").prijs(86.66).build();
		
		eventRepository.save(e1);
		eventRepository.save(e2);
		eventRepository.save(e3);
		eventRepository.save(e4);
		
		var user = MyUser.builder().username("userNFE").role(Role.USER).password(encoder.encode("nfe")).build();
		var admin = MyUser.builder().username("admin").role(Role.ADMIN).password(encoder.encode("admin")).build();
		var userMetFavorieten = MyUser.builder().username("userWFE").role(Role.USER).password(encoder.encode("wfe")).favorietEvents(List.of(e1, e2)).build();
	
		myUserRepository.saveAll(List.of(user, admin, userMetFavorieten));
	
	}

}
