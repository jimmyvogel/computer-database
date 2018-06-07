package com.excilys.cdb.mapper;

import java.util.Optional;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.model.Company;

/**
 * Mapper de computer sans validation avec optionnal.
 * @author vogel
 *
 */
public class MapperCompany {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);

	public static Optional<CompanyDTO> map(Company c) {
		return Optional.ofNullable(new CompanyDTO(c.getId(), c.getName()));
	}

	public static Optional<Company> map(CompanyDTO c) {
		return Optional.ofNullable(new Company(c.getId(), c.getName()));
	}
}
