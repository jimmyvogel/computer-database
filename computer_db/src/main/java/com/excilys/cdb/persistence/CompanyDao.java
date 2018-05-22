package com.excilys.cdb.persistence;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.mysql.jdbc.Statement;

/**
 * Classe contenant les requêtes possibles sur la table des company de la base
 * de donnée.
 * @author vogel
 *
 */
@Repository
public class CompanyDao implements Dao<Company> {

	@Autowired
	private JdbcTemplate jt;

	private static final String SQL_SEARCH_ALL_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `name` LIKE ? ORDER by `name`";
	private static final String SQL_AJOUT_COMPANY = "INSERT into `company` (name) VALUES (?)";
	private static final String SQL_ALL_COMPANIES = "SELECT `id`,`name` FROM `company`";
	private static final String SQL_ONE_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `id`=?";
	private static final String SQL_DELETE_LINKED_COMPUTER = "DELETE FROM `computer` WHERE `company_id` IN %s";
	private static final String SQL_DELETE_COMPANY = "DELETE FROM `company` WHERE `id` IN %s";
	private static final String SQL_COUNT_COMPANY = "SELECT COUNT(`id`) FROM `company`";
	private static final String SQL_PAGE_COMPANY = SQL_ALL_COMPANIES + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_PAGE_COMPANY = SQL_SEARCH_ALL_COMPANY + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_COUNT = "SELECT COUNT(`id`) FROM `company` WHERE `name` LIKE ?";

	public static final String MESS_REQUEST_EXCEPTION = "Reqûete exception";
	public static final String MESS_SQL_EXCEPTION = "Erreur sql";

	/**
	 * Méthode pour récupérer toutes les entreprises de la bdd.
	 * @return une référence sur le singleton company dao
	 */
	public List<Company> getAll() {
		return jt.query(SQL_ALL_COMPANIES, new MapperCompany());
	}

	/**
	 * Méthode pour récupérer une entreprise.
	 * @param id l'élément à récupérer
	 * @return un optional objet company
	 */
	public Optional<Company> getById(final long id) {
		Company company = null;
		try {
			company = jt.queryForObject(SQL_ONE_COMPANY, new MapperCompany(), id);
		} catch (EmptyResultDataAccessException e) {
		}

		return Optional.ofNullable(company);
	}

	/**
	 * Méthode pour récupérer les entreprises par name.
	 * @param name nom des éléments à récupérer
	 * @return une liste de Company
	 * @throws DaoException erreur d'arguments
	 */
	public List<Company> getByName(final String name) throws DaoException {
		if (name == null) {
			throw new DaoException("Illegal argument exceptions");
		}
		return jt.query(SQL_SEARCH_ALL_COMPANY, new MapperCompany(), "%" + name + "%");
	}

	/**
	 * Méthode pour récupérer le nombre d'objets de type entreprise en bdd.
	 * @return le nombre d'instances
	 * @throws DaoException exception sur la requête
	 */
	public long getCount() throws DaoException {
		Integer count = 0;
		try {
			count = jt.queryForObject(SQL_COUNT_COMPANY, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			throw new DaoException("Count query fail");
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
		if (search == null) {
			throw new DaoException("Illegal argument exceptions");
		}
		Integer count = 0;
		try {
			count = jt.queryForObject(SQL_SEARCH_COUNT, Integer.class, "%" + search + "%");
		} catch (EmptyResultDataAccessException e) {
			throw new DaoException("Count search query fail");
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
		if (numeroPage < 1 || limit < 1) {
			throw new DaoException("Illegal argument exceptions");
		}
		Page<Company> page = new Page<Company>(limit, (int) this.getCount());
		List<Company> companies = jt.query(SQL_PAGE_COMPANY, new MapperCompany(), page.getLimit(),
				page.offset(numeroPage));
		page.charge(companies, numeroPage);
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
	 * @throws DaoException exception sur la requite
	 */
	public Page<Company> getPageSearch(final String search, final int numeroPage, final int limit) throws DaoException {
		if (search == null || numeroPage < 1 || limit < 1) {
			throw new DaoException("Illegal argument exceptions");
		}
		Page<Company> page = new Page<Company>(limit, (int) this.getSearchCount(search));
		List<Company> companies = jt.query(SQL_SEARCH_PAGE_COMPANY, new MapperCompany(), "%" + search + "%",
				page.getLimit(), page.offset(numeroPage));
		page.charge(companies, numeroPage);
		return page;
	}

	/**
	 * Delete a company.
	 * @param id id of the company to delete.
	 * @return résultat
	 * @throws DaoException exception sur la requête.
	 */
	public boolean delete(long id) throws DaoException {
		return deleteAll(Collections.singleton(id));
	}

	/**
	 * Supprimer une liste de company.
	 * @param companies les ids des company à supprimer.
	 * @return résultat.
	 * @throws DaoException illegal arguments.
	 */
	public boolean deleteAll(Set<Long> companies) throws DaoException {
		if (companies == null) {
			throw new DaoException("Illegal argument exceptions");
		}
		boolean deleteCompany = false;
		if (companies.size() != 0) {
			String sqlIn = companies.toString().replace("[", "(").replace("]", ")");
			jt.update(String.format(SQL_DELETE_LINKED_COMPUTER, sqlIn));
			deleteCompany = jt.update(String.format(SQL_DELETE_COMPANY, sqlIn)) > 0;
		}
		return deleteCompany;
	}

	/**
	 * Créer un objet de type company.
	 * @param company Un objet complet en argument
	 * @return l'id de l'objet crée ou -1 si la création a échoué
	 * @throws DaoException illegal arguments
	 */
	public long create(final Company company) throws DaoException {
		if (company == null || company.getName() == null) {
			throw new DaoException("Illegal argument exceptions");
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jt.update((c) -> {
			PreparedStatement ps = c.prepareStatement(SQL_AJOUT_COMPANY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, company.getName());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}

}
