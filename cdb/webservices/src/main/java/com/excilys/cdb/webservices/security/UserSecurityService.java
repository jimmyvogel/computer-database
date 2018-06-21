package com.excilys.cdb.webservices.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.User;
import com.excilys.cdb.service.IUserService;

@Service
public class UserSecurityService implements UserDetailsService {

	private IUserService userService;
	
	public UserSecurityService(IUserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt = userService.findByUsername(username);
		User user = opt.orElseThrow(()-> new UsernameNotFoundException(username + "not found."));
		return JwtUserFactory.create(user);
	}

}
