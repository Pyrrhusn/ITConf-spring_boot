package service;

import domain.MyUser;

public interface MyUserDetailsService {
	
	MyUser findByUsername(String name);
	
	void save(MyUser user);

}