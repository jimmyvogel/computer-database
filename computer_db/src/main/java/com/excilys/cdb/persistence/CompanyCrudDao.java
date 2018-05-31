package com.excilys.cdb.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

@Repository
public interface CompanyCrudDao extends PagingAndSortingRepository<Company, Long> {

	@Override
	List<Company> findAll();

	void deleteAllByIdIn(Iterable<Long> ids);

	Page<Company> findByNameContainingOrderByName(String companyName, Pageable p);
}
