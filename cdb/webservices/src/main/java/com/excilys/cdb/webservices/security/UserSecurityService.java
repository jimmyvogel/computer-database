package com.excilys.cdb.webservices.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.UserCrudDao;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.UserService;

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
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
	}

}
