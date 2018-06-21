package com.excilys.cdb.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.User;
import com.excilys.cdb.service.IUserService;
import com.excilys.cdb.service.exceptions.UserNotFoundException;

@Service
public class UserSecurityService implements UserDetailsService {

	private IUserService userService;
	
	public UserSecurityService(IUserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt = userService.findByUsername(username);
		UserBuilder builder = null;
		if (opt.isPresent()) {
			User user = opt.get();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.disabled(!user.isEnabled());
			builder.password(user.getPassword());
			String[] authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new);

			builder.authorities(authorities);
		} else {
			throw new UserNotFoundException();
		}
		return builder.build();
	}

}
