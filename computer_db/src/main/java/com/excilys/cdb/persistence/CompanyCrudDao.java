package com.excilys.cdb.persistence;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

@Repository
public interface CompanyCrudDao extends PagingAndSortingRepository<Company, Long> {

	/**
	 * .
	 * @param ids .
	 * @return .
	 */
	boolean deleteAllById(Set<Long> ids);
}
