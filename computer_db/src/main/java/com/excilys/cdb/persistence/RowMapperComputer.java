package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Mapper pour un computer.
 * @author vogel
 *
 */
public class RowMapperComputer implements RowMapper<Computer> {

	@Override
	public Computer mapRow(ResultSet result, int rownumber) throws SQLException {
        Computer computer = new Computer();

        try {
            computer.setId(result.getLong(3));
            computer.setName(result.getString(4));
            if (result.getString(5) != null) {
                computer.setIntroduced(
                        result.getTimestamp(5).toLocalDateTime().toLocalDate());
            }

            if (result.getString(6) != null) {
                computer.setDiscontinued(
                        result.getTimestamp(6).toLocalDateTime().toLocalDate());
            }

            if (result.getString(2) != null) {
                Company c = new Company(result.getLong(1), result.getString(2));
                computer.setCompany(c);
            }
            return computer;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
	}
}
