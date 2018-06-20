package com.excilys.cdb.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;

@Repository
public interface ComputerCrudDao extends PagingAndSortingRepository<Computer, Long> {

	public enum PageComputerOrder implements DaoOrder {
		BY_NAME, BY_NAME_DESC, BY_INTRODUCED_DATE, BY_DISCONTINUED_DATE, BY_INTRODUCED_DATE_DESC, BY_DISCONTINUED_DATE_DESC;
	}

	@Override
	List<Computer> findAll();

	Long deleteByIdIn(Iterable<Long> ids);

	Long deleteByCompanyIdIn(Iterable<Long> ids);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByName(String name, String companyName, Pageable p);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByNameDesc(String name, String companyName,
			Pageable p);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByIntroducedAsc(String name, String companyName,
			Pageable p);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByIntroducedDesc(String name, String companyName,
			Pageable p);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByDiscontinuedAsc(String name, String companyName,
			Pageable p);

	Page<Computer> findByNameContainingOrCompanyNameContainingOrderByDiscontinuedDesc(String name, String companyName,
			Pageable p);

	Page<Computer> findAllByOrderByName(Pageable pageable);

	Page<Computer> findAllByOrderByNameDesc(Pageable pageable);

	Page<Computer> findAllByOrderByIntroducedAsc(Pageable pageable);

	Page<Computer> findAllByOrderByIntroducedDesc(Pageable pageable);

	Page<Computer> findAllByOrderByDiscontinuedAsc(Pageable pageable);

	Page<Computer> findAllByOrderByDiscontinuedDesc(Pageable pageable);
}
