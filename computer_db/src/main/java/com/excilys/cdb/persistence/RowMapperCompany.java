package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Company;

/**
 * Mapper pour une compagnie.
 * @author vogel
 *
 */
public class RowMapperCompany implements RowMapper<Company> {

	@Override
	public Company mapRow(ResultSet result, int rownumber) throws SQLException {
        try {
            // id and name
            if (result.getString(1) != null && result.getString(2) != null) {
                return new Company(result.getLong(1), result.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}
}
