package com.excilys.cdb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;

/**
 * Mapper de computer sans validation avec optionnal.
 * @author vogel
 *
 */
public class MapperCompany {

	public static CompanyDTO map(Company c) {
		List<ComputerDTO> list = c.getComputers().stream().map(s->MapperComputer.map(s)).collect(Collectors.toList());
		return new CompanyDTO(c.getId(), c.getName(), list);
	}
	
	/**
	 * Map to dto without taking the list of computer in memory.
	 * @param c a company
	 * @return a dto
	 */
	public static CompanyDTO mapLazy(Company c) {
		return new CompanyDTO(c.getId(), c.getName());
	}
	
	/**
	 * We ignore the computers when going from a companyDTO to a company entity.
	 * @param c
	 * @return
	 */
	public static Company map(CompanyDTO c) {
		return new Company(c.getId(), c.getName());
	}
	
	public static Page<CompanyDTO> mapToPage(Page<Company> page) {
		return page.map(s -> MapperCompany.map(s));
	}
	
	public static Page<CompanyDTO> mapToPageLazy(Page<Company> page) {
		return page.map(s -> MapperCompany.mapLazy(s));
	}
}
