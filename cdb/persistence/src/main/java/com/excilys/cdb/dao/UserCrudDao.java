package com.excilys.cdb.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.User;

@Repository
public interface UserCrudDao extends PagingAndSortingRepository<User, Long> {
	User findByUsername(String username);
}
