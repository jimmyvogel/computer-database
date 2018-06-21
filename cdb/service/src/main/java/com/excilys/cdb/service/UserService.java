package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.Authorities;
import com.excilys.cdb.model.User;

@Service
public class UserService implements IUserService {

	protected UserCrudDao userDao;

	public UserService(UserCrudDao userDao) {
		this.userDao = userDao;
	}
	
	public Optional<User> inscription(String username, String password) {
		if(this.findByUsername(username).isPresent())
		{
			return Optional.empty();
		}
		User user = new User(username, password);
		user.setAuthorities(Collections.singleton(new Authorities(Authorities.Role.ROLE_USER)));
		user.setEnabled(true);
		user.setLastPasswordResetDate(LocalDateTime.now());
		return Optional.of(userDao.save(user));
	}
	
	public Optional<User> findByUsername(String username) {
		return userDao.findByUsername(username);
	}
}