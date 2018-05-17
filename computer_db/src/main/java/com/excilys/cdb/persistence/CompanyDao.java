package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Classe contenant les requêtes possibles sur la table des company de la base
 * de donnée.
 * @author vogel
 *
 */
@Repository
public class CompanyDao implements Dao<Company> {

	@Autowired
	private HikariDataSource ds;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDao.class);

	private static final String SQL_SEARCH_ALL_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `name` LIKE ? ORDER by `name`";
	private static final String SQL_AJOUT_COMPANY = "INSERT into `company` (name) VALUES (?)";
	private static final String SQL_ALL_COMPANIES = "SELECT `id`,`name` FROM `company`";
	private static final String SQL_ONE_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `id`=?";
	private static final String SQL_DELETE_LINKED_COMPUTER = "DELETE FROM `computer` WHERE `company_id`=?";
	private static final String SQL_DELETE_COMPANY = "DELETE FROM `company` WHERE `id`=?";
	private static final String SQL_COUNT_COMPANY = "SELECT COUNT(`id`) AS `total` FROM `company`";
	private static final String SQL_PAGE_COMPANY = SQL_ALL_COMPANIES + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_PAGE_COMPANY = SQL_SEARCH_ALL_COMPANY + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_COUNT = "SELECT COUNT(`id`) AS `total` FROM `company` WHERE `name` LIKE ?";

	public static final String MESS_REQUEST_EXCEPTION = "Reqûete exception";
	public static final String MESS_SQL_EXCEPTION = "Erreur sql";

	/**
	 * Méthode pour récupérer toutes les entreprises de la bdd.
	 * @return une référence sur le singleton company dao
	 * @throws DaoException exception sur la requête
	 */
	public List<Company> getAll() throws DaoException {
		List<Company> companies = new ArrayList<Company>();
		try (Connection c = ds.getConnection(); Statement stmt = c.createStatement()) {
			ResultSet result = stmt.executeQuery(SQL_ALL_COMPANIES);
			while (result.next()) {
				companies.add(MapperDao.mapCompany(result));
			}

		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return companies;
	}

	/**
	 * Méthode pour récupérer une entreprise.
	 * @param id l'élément à récupérer
	 * @return un optional objet company
	 * @throws DaoException exception sur la requête
	 */
	public Optional<Company> getById(final long id) throws DaoException {
		Company company = null;
		try (Connection c = ds.getConnection(); PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPANY)) {
			stmt.setLong(1, id);

			ResultSet result = stmt.executeQuery();

			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet retourné
			 */
			if (result.next()) {
				company = MapperDao.mapCompany(result);
			}

		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return Optional.ofNullable(company);
	}

	/**
	 * Méthode pour récupérer les entreprises par name.
	 * @param name nom des éléments à récupérer
	 * @return une liste de Company
	 * @throws DaoException exception sur la requête
	 */
	public List<Company> getByName(final String name) throws DaoException {
		List<Company> companies = new ArrayList<Company>();
		try (Connection c = ds.getConnection();
				PreparedStatement stmt = c.prepareStatement(SQL_SEARCH_ALL_COMPANY)) {
			stmt.setString(1, "%" + name + "%");
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				companies.add(MapperDao.mapCompany(result));
			}

		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return companies;
	}

	/**
	 * Méthode pour récupérer le nombre d'objets de type entreprise en bdd.
	 * @return le nombre d'instances
	 * @throws DaoException exception sur la requête
	 */
	public long getCount() throws DaoException {
		long count = 0;
		try (Connection c = ds.getConnection(); Statement stmt = c.createStatement()) {
			String query = SQL_COUNT_COMPANY;
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				count = result.getLong("total");
			}
		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return count;
	}

	/**
	 * Méthode pour récupérer le nombre d'objets résultats d'une recherche par nom.
	 * @param search le nom de la recherche
	 * @return le nombre d'instances
	 * @throws DaoException exception sur la requête
	 */
	public long getSearchCount(final String search) throws DaoException {
		long count = 0;
		try (Connection c = ds.getConnection(); PreparedStatement stmt = c.prepareStatement(SQL_SEARCH_COUNT)) {
			stmt.setString(1, "%" + search + "%");

			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				count = result.getLong("total");
			}
		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return count;
	}

	/**
	 * Méthode pour récupérer les objets d'une certaine page.
	 * @param numeroPage le numéro de la page a récupérer
	 * @return une liste d'objets dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Company> getPage(final int numeroPage) throws DaoException {
		return getPage(numeroPage, LIMIT_DEFAULT);
	}

	/**
	 * Méthode pour récupérer un nombre spécifié d'objets d'une certaine page.
	 * @param numeroPage le numéro de la page a récupérer
	 * @param limit nombre d'objets à récupérer
	 * @return une liste d'objets dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Company> getPage(final int numeroPage, final int limit) throws DaoException {
		Page<Company> page = new Page<Company>(limit, (int) this.getCount());
		List<Company> companies = new ArrayList<Company>();
		try (Connection c = ds.getConnection(); PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPANY)) {
			stmt.setInt(1, page.getLimit());
			stmt.setInt(2, page.offset(numeroPage));
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				companies.add(MapperDao.mapCompany(result));
			}
			page.charge(companies, numeroPage);
		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}
		return page;
	}

	/**
	 * Méthode pour récupérer les résultats d'une recherche sur le nom de compagnie.
	 * @param search le nom de recherche
	 * @param numeroPage le numéro de la page a récupérer
	 * @return une liste de computers dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Company> getPageSearch(final String search, final int numeroPage) throws DaoException {
		return getPageSearch(search, numeroPage, LIMIT_DEFAULT);
	}

	/**
	 * Méthode pour récupérer des compagnies de nom le paramètre donné.
	 * @param search le nom à chercher
	 * @param numeroPage le numéro de la page a récupérer
	 * @param limit nombre d'objets à récupérer
	 * @return une liste d'objets dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Company> getPageSearch(final String search, final int numeroPage, final int limit) throws DaoException {
		Page<Company> page = new Page<Company>(limit, (int) this.getSearchCount(search));
		List<Company> companies = new ArrayList<Company>();
		try (Connection c = ds.getConnection();
				PreparedStatement stmt = c.prepareStatement(SQL_SEARCH_PAGE_COMPANY)) {
			stmt.setString(1, "%" + search + "%");
			stmt.setInt(2, page.getLimit());
			stmt.setInt(3, page.offset(numeroPage));
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				companies.add(MapperDao.mapCompany(result));
			}
			page.charge(companies, numeroPage);
		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}
		return page;
	}

	/**
	 * Delete a company.
	 * @param id id of the company to delete.
	 * @return résultat
	 * @throws DaoException exception sur la requête.
	 */
	public boolean delete(long id) throws DaoException {
		return delete(Collections.singleton(id));
	}

	/**
	 * Supprimer une liste de company.
	 * @param companies les ids des company à supprimer.
	 * @return résultat.
	 * @throws DaoException exception sur la requête.
	 */
	public boolean delete(Set<Long> companies) throws DaoException {
		int resultCompany = 0;
		boolean deleteOk = true;
		Connection c;
		try {
			c = ds.getConnection();
			try (PreparedStatement stmtCompany = c.prepareStatement(SQL_DELETE_COMPANY);
					PreparedStatement stmtComputer = c.prepareStatement(SQL_DELETE_LINKED_COMPUTER)) {

				Transaction.beginTransaction(c);
				for (Long id : companies) {
					stmtCompany.setLong(1, id);
					stmtComputer.setLong(1, id);
					stmtComputer.executeUpdate();
					resultCompany = stmtCompany.executeUpdate();
					if (resultCompany <= 0) {
						deleteOk = false;
						break;
					}
				}
			} catch (SQLException e) {
				try {
					c.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Rollback fail.");
					throw new DaoException(MESS_REQUEST_EXCEPTION, e1);
				}
				throw new DaoException(MESS_REQUEST_EXCEPTION, e);
			}
		} catch (SQLException e2) {
			LOGGER.error("Error SQL");
			throw new DaoException(MESS_SQL_EXCEPTION, e2);
		}

		Transaction.endTransaction(c, deleteOk);

		return deleteOk;
	}

	/**
	 * Créer un objet de type company.
	 * @param company Un objet complet en argument
	 * @return l'id de l'objet crée ou -1 si la création a échoué
	 * @throws DaoException erreur sur la reqûete
	 */
	public long create(final Company company) throws DaoException {
		long id = -1;

		if (company.getName() == null) {
			throw new DaoException("Argument (name) requête création company null");
		}
		try (Connection c = ds.getConnection();
				PreparedStatement stmt = c.prepareStatement(SQL_AJOUT_COMPANY, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, company.getName());
			stmt.execute();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs != null && rs.first()) {
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new DaoException(MESS_REQUEST_EXCEPTION, e);
		}

		return id;
	}

}
