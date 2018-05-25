package com.excilys.cdb.mapper;

import java.time.LocalDateTime;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.DateValidation;

/**
 * Mapper de computer sans validation avec optionnal.
 * @author vogel
 *
 */
public class MapperComputer {

	/**
	 * Mapping to certifiate string format.
	 * @param name le nom du computer
	 * @param introduced la date d'introduction
	 * @param discontinued la date de résiliation
	 * @param idCompany l'id de la compagnie créatrice du computer
	 * @return un optionnal de computer pouvant être null.
	 */
	public Optional<Computer> map(String name, String introduced, String discontinued, String idCompany) {
    	LocalDateTime dateIntro = DateValidation.validDateFormat(introduced);
    	LocalDateTime dateDiscon = DateValidation.validDateFormat(discontinued);
    	Computer c = null;
    	try {
    		c = new Computer(name, dateIntro, dateDiscon, new Company(Long.valueOf(idCompany), ""));
    	} catch (NumberFormatException e) { }
    	return Optional.ofNullable(c);
	}

}
