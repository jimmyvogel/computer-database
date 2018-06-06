package com.excilys.cdb.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.excilys.cdb.model.User;

public interface IUserService extends UserDetailsService{

	public User inscription(String username, String password);
}
