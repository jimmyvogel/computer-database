package com.excilys.cdb.mapper;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.ComputerValidator;
import com.excilys.cdb.validator.DateValidation;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Mapper de computer sans validation avec optionnal.
 * @author vogel
 *
 */
public class MapperComputer {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);

	public static ComputerDTO map(Computer computer) {
		return new ComputerDTO(computer);
	}
	
	public static Page<ComputerDTO> mapToPage(Page<Computer> computers) {
		return computers.map(s -> MapperComputer.map(s));
	}

	
	/**
	 * Mapping to certifiate string format.
	 * @param name le nom du computer
	 * @param introduced la date d'introduction
	 * @param discontinued la date de résiliation
	 * @param idCompany l'id de la compagnie créatrice du computer
	 * @return un optionnal de computer pouvant être null.
	 * @throws ValidatorDateException 
	 * @throws ValidatorStringException 
	 */
	public static Computer map(String name, String introduced, String discontinued, String idCompany) throws ValidatorStringException, ValidatorDateException {
		return map("0", name, introduced, discontinued, idCompany);
	}

	/**
	 * Mapping to certifiate string format.
	 * @param id id du computer
	 * @param name le nom du computer
	 * @param introduced la date d'introduction
	 * @param discontinued la date de résiliation
	 * @param idCompany l'id de la compagnie créatrice du computer
	 * @return un optionnal de computer pouvant être null.
	 * @throws ValidatorDateException 
	 * @throws ValidatorStringException 
	 */
	public static Computer map(String id, String name, String introduced, String discontinued,
			String idCompany) throws ValidatorStringException, ValidatorDateException {
		LocalDate dateIntro = DateValidation.validDateFormat(introduced);
		LocalDate dateDiscon = DateValidation.validDateFormat(discontinued);
		Computer c = new Computer(Long.valueOf(id), name, dateIntro, dateDiscon, new Company(Long.valueOf(idCompany), ""));
		ComputerValidator.validComputer(c);
		return c;
	}

	/**
	 * Mapping from computerdto to computer.
	 * @param computer computerdto
	 * @return un optionnal computer
	 * @throws ValidatorDateException 
	 * @throws ValidatorStringException 
	 */
	public static Computer map(ComputerDTO computer) throws ValidatorStringException, ValidatorDateException {
		String introduced = computer.getIntroduced() == null ? null : computer.getIntroduced().toString();
		String discontinued = computer.getDiscontinued() == null ? null : computer.getDiscontinued().toString();
		return map(String.valueOf(computer.getId()), computer.getName(), introduced, discontinued,
				String.valueOf(computer.getCompanyId()));
	}

}
