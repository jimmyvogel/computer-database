package com.excilys.cdb.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

@Repository
public interface CompanyCrudDao extends PagingAndSortingRepository<Company, Long> {

	public enum PageCompanyOrder implements DaoOrder {
		BY_NAME, BY_NAME_DESC;
	}

	@Override
	List<Company> findAll();

	Long deleteAllByIdIn(Iterable<Long> ids);

	Page<Company> findByNameContainingOrderByName(String companyName, Pageable p);

	Page<Company> findByNameContainingOrderByNameDesc(String companyName, Pageable p);

	Page<Company> findAllByOrderByName(Pageable pageable);

	Page<Company> findAllByOrderByNameDesc(Pageable pageable);

}
