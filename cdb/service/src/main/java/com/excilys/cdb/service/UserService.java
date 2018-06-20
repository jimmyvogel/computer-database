package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.Authorities;
import com.excilys.cdb.model.User;

@Service
@Transactional
public class UserService implements IUserService{

	protected UserCrudDao userDao;

	public UserService(UserCrudDao userDao) {
		this.userDao = userDao;
	}
	
	public User inscription(String username, String password) {
		User user = new User(username, password);
		user.setAuthorities(Collections.singleton(new Authorities(Authorities.Role.ROLE_USER)));
		user.setEnabled(true);
		user.setLastPasswordResetDate(LocalDateTime.now());
		return userDao.save(user);
	}
}