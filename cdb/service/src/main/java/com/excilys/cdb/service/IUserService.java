package com.excilys.cdb.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.User;

@Service
@Transactional
public interface IUserService {

	public Optional<User> inscription(String username, String password);
	
	public Optional<User> findByUsername(String username);
}
