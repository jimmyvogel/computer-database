package com.excilys.cdb.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;

@Repository
public interface ComputerCrudDao extends PagingAndSortingRepository<Computer, Long> {

	@Override
	List<Computer> findAll();

	void deleteByIdIn(Iterable<Long> ids);

	void deleteByCompanyIdIn(Iterable<Long> ids);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByName(String name, String companyName, Pageable p);

}
