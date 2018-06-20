package com.excilys.cdb.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.UserService;
import com.excilys.cdb.service.exceptions.UserNotFoundException;

@Service
@Transactional
public class UserSecurityService extends UserService implements UserDetailsService {

	public UserSecurityService(UserCrudDao userDao) {
		super(userDao);
	}

	@Transactional(readOnly = true)
	@Override
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
