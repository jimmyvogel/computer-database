package com.excilys.cdb.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.User;

@Repository
public interface UserCrudDao extends PagingAndSortingRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
