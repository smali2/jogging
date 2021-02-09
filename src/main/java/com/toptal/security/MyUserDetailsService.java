// Bismillah Hirrahman Nirrahim

package com.toptal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toptal.UserRepository;
import com.toptal.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		
		User user = repo.findByUsername(username).get();
		if (user==null) {
			throw new UsernameNotFoundException("User Not Found 404");
		}
		
		return new UserPrincipal(user);
		
		
	}
	
}
