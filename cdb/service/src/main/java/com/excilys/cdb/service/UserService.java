package com.excilys.cdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.exceptions.UserNotFoundException;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserCrudDao userDao;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDao.findByUsername(username);
		UserBuilder builder = null;
		if (user != null) {

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