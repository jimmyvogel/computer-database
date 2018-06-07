package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.util.Optional;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.DateValidation;

/**
 * Mapper de computer sans validation avec optionnal.
 * @author vogel
 *
 */
public class MapperComputer {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);

	public static Optional<ComputerDTO> map(Computer computer) {
		return Optional.ofNullable(new ComputerDTO(computer));
	}

	
	/**
	 * Mapping to certifiate string format.
	 * @param name le nom du computer
	 * @param introduced la date d'introduction
	 * @param discontinued la date de résiliation
	 * @param idCompany l'id de la compagnie créatrice du computer
	 * @return un optionnal de computer pouvant être null.
	 */
	public static Optional<Computer> map(String name, String introduced, String discontinued, String idCompany) {
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
	 */
	public static Optional<Computer> map(String id, String name, String introduced, String discontinued,
			String idCompany) {
		LocalDate dateIntro = DateValidation.validDateFormat(introduced);
		LocalDate dateDiscon = DateValidation.validDateFormat(discontinued);
		Computer c = null;
		try {
			c = new Computer(Long.valueOf(id), name, dateIntro, dateDiscon, new Company(Long.valueOf(idCompany), ""));
		} catch (NumberFormatException e) {
		}
		return Optional.ofNullable(c);
	}

	/**
	 * Mapping from computerdto to computer.
	 * @param computer computerdto
	 * @return un optionnal computer
	 */
	public static Optional<Computer> map(ComputerDTO computer) {
		String introduced = computer.getIntroduced() == null ? null : computer.getIntroduced().toString();
		String discontinued = computer.getDiscontinued() == null ? null : computer.getDiscontinued().toString();
		return map(String.valueOf(computer.getId()), computer.getName(), introduced, discontinued,
				String.valueOf(computer.getCompanyId()));
	}

}
