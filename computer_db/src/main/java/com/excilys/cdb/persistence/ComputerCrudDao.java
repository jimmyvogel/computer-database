package com.excilys.cdb.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;

@Repository
public interface ComputerCrudDao extends PagingAndSortingRepository<Computer, Long> {
}
