package com.excilys.cdb.service;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.Authorities;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.exceptions.UserNotFoundException;

@Service
@Transactional
public class UserService implements IUserService{

	private UserCrudDao userDao;

	public UserService(UserCrudDao userDao) {
		this.userDao = userDao;
	}
	
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
	
	public User inscription(String username, String password) {
		User user = new User(username, password);
		Set<Authorities> authorities = Collections.singleton(new Authorities(Authorities.Role.ROLE_USER, user));
		user.setAuthorities(authorities);
		return userDao.save(user);
	}
}