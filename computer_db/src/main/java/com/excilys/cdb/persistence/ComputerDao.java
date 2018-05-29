package com.excilys.cdb.persistence;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DaoCharacterSpeciauxException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.persistence.exceptions.DaoIllegalArgumentException;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.mysql.jdbc.Statement;

/**
 * Classe contenant les requêtes possibles sur la table des computer de la base
 * de donnée, pas de gestion de validité, mais regestion de la sécurité.
 * @author vogel
 *
 */
@Repository
public class ComputerDao implements Dao<Computer> {

	@Autowired
	private DataSource ds;
	private JdbcTemplate jt;

	private static final String SQL_SEARCH_ALL_COMPUTER = "SELECT company.id,company.name, computer.id, "
			+ "computer.name, computer.introduced, computer.discontinued FROM company "
			+ "RIGHT JOIN computer ON company.id = computer.company_id WHERE computer.name LIKE ? OR company.name LIKE ? ORDER by computer.name";
	private static final String SQL_ALL_COMPUTERS = "SELECT company.id,company.name, computer.id, "
			+ "computer.name, computer.introduced, computer.discontinued FROM company "
			+ "RIGHT JOIN computer ON company.id = computer.company_id";
	private static final String SQL_ONE_COMPUTER = "SELECT company.id,company.name, computer.id, "
			+ "computer.name, computer.introduced, computer.discontinued FROM company "
			+ "RIGHT JOIN computer ON company.id = computer.company_id WHERE computer.id=?";
	private static final String SQL_AJOUT_COMPUTER = "INSERT into `computer` (name,introduced,discontinued,company_id)"
			+ " VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_COMPUTER = "UPDATE computer SET name=?, introduced=?, discontinued=?, "
			+ "company_id=? WHERE id=? ";
	private static final String SQL_DELETE_COMPUTER = "DELETE FROM `computer` WHERE `id` IN %s";
	private static final String SQL_COUNT_COMPUTER = "SELECT COUNT(`id`) AS `total` FROM `computer`";
	private static final String SQL_PAGE_COMPUTER = SQL_ALL_COMPUTERS + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_PAGE_COMPUTER = SQL_SEARCH_ALL_COMPUTER + " LIMIT ? OFFSET ?";
	private static final String SQL_SEARCH_COUNT = "SELECT COUNT(computer.id) AS `total` FROM company "
			+ "RIGHT JOIN computer ON company.id = computer.company_id WHERE computer.name LIKE ? OR company.name LIKE ?";

	public static final String MESS_REQUEST_EXCEPTION = "Requête exception";
	public static final String MESS_SQL_EXCEPTION = "Erreur sql";

	@PostConstruct
	public void init() {
		this.jt = new JdbcTemplate(ds);
	}

	/**
	 * Retourne tous les computers de la bdd.
	 * @return une liste
	 */
	public List<Computer> getAll() {
		return jt.query(SQL_ALL_COMPUTERS, new RowMapperComputer());
	}

	/**
	 * Récupérer un Computer de la bdd.
	 * @param id l'id du computer
	 * @return un objet Computer
	 */
	public Optional<Computer> getById(long id) {
		Computer computer = null;
		try {
			computer = jt.queryForObject(SQL_ONE_COMPUTER, new RowMapperComputer(), id);
		} catch (EmptyResultDataAccessException e) {
		}

		return Optional.ofNullable(computer);
	}

	/**
	 * Récupérer des computers par nom.
	 * @param name le nom des computers cherchés
	 * @return une liste de computer
	 * @throws DaoException erreur de reqûete.
	 */
	public List<Computer> getByName(String name) throws DaoException {
		return jt.query(SQL_SEARCH_ALL_COMPUTER, new RowMapperComputer(), "%" + name + "%", "%" + name + "%");
	}

	/**
	 * Créer un objet de type computer.
	 * @param comp Un objet complet en argument
	 * @return l'id de l'objet crée ou -1 si la création a échoué
	 * @throws DaoException erreur dao.
	 */
	public long create(final Computer comp) throws DaoException {
		if (comp == null) {
			throw new DaoIllegalArgumentException();
		}
		Timestamp introduced = comp.getIntroduced() != null ? Timestamp.valueOf(comp.getIntroduced()) : null;
		Timestamp discontinued = comp.getDiscontinued() != null ? Timestamp.valueOf(comp.getDiscontinued()) : null;
		Long idCompany = comp.getCompany() != null ? comp.getCompany().getId() : null;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jt.update((c) -> {
			PreparedStatement ps = c.prepareStatement(SQL_AJOUT_COMPUTER, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, comp.getName());
			ps.setTimestamp(2, introduced);
			ps.setTimestamp(3, discontinued);
			if (idCompany == null) {
				ps.setString(4, null);
			} else {
				ps.setLong(4, idCompany);
			}
			return ps;
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * Méthode pour modifier un computer en bdd.
	 * @param comp le computer a modifié.
	 * @return le résultat en booléan.
	 */
	public boolean update(Computer comp) {
		if (comp == null) {
			return false;
		}
		Timestamp introduced = comp.getIntroduced() != null ? Timestamp.valueOf(comp.getIntroduced()) : null;
		Timestamp discontinued = comp.getDiscontinued() != null ? Timestamp.valueOf(comp.getDiscontinued()) : null;
		Long idCompany = comp.getCompany() != null ? comp.getCompany().getId() : null;
		return jt.update(SQL_UPDATE_COMPUTER, comp.getName(), introduced, discontinued, idCompany, comp.getId()) > 0;
	}

	/**
	 * Méthode pour supprimer un computer.
	 * @param id l'id de l'objet à supprimer
	 * @return un boolean pour connaitre le résultat
	 * @throws DaoException exception sur la requête
	 */
	public boolean delete(final long id) throws DaoException {
		return delete(Collections.singleton(id));
	}

	/**
	 * Supprimer une liste de computers.
	 * @param computers les ids des computers à supprimer.
	 * @return résultat.
	 * @throws DaoIllegalArgumentException dao exception
	 */
	public boolean delete(Set<Long> computers) throws DaoIllegalArgumentException {
		if (computers == null) {
			throw new DaoIllegalArgumentException();
		}
		boolean deleteComputer = false;
		if (computers.size() > 0) {
			String sqlIn = computers.toString().replace("[", "(").replace("]", ")");
			deleteComputer = jt.update(String.format(SQL_DELETE_COMPUTER, sqlIn)) > 0;
		}
		return deleteComputer;
	}

	/**
	 * Méthode pour récupérer le nombre d'objets de type computer en bdd.
	 * @return le nombre d'instances
	 * @throws DaoException exception sur la requête
	 */
	public long getCount() throws DaoException {
		Integer count = 0;
		try {
			count = jt.queryForObject(SQL_COUNT_COMPUTER, Integer.class);
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
			throw new DaoIllegalArgumentException();
		}
		Integer count = 0;
		try {
			count = jt.queryForObject(SQL_SEARCH_COUNT, Integer.class, "%" + search + "%", "%" + search + "%");
		} catch (EmptyResultDataAccessException e) {
			throw new DaoException("Count search query fail");
		}
		return count;
	}

	/**
	 * Méthode pour récupérer les objets d'une certaine page de computer.
	 * @param numeroPage le numéro de la page a récupérer
	 * @return une liste de computers dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Computer> getPage(final int numeroPage) throws DaoException {
		return getPage(numeroPage, LIMIT_DEFAULT);
	}

	/**
	 * Méthode pour récupérer un nombre spécifié d'objets d'une certaine page de
	 * computer.
	 * @param numeroPage le numéro de la page a récupérer
	 * @param limit le nombre d'objets a instancier
	 * @return une liste de computers dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Computer> getPage(final int numeroPage, final int limit) throws DaoException {
		if (numeroPage < 1 || limit < 1) {
			throw new DaoIllegalArgumentException();
		}
		Page<Computer> page = new Page<Computer>(limit, (int) this.getCount());
		List<Computer> computers = jt.query(SQL_PAGE_COMPUTER, new RowMapperComputer(), page.getLimit(),
				page.offset(numeroPage));
		page.charge(computers, numeroPage);
		return page;
	}

	/**
	 * Méthode pour récupérer les résultats d'une recherche sur le nom de computer
	 * et de compagnie.
	 * @param search le nom de recherche
	 * @param numeroPage le numéro de la page a récupérer
	 * @return une liste de computers dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Computer> getPageSearch(final String search, final int numeroPage) throws DaoException {
		return getPageSearch(search, numeroPage, LIMIT_DEFAULT);
	}

	/**
	 * Méthode pour récupérer les résultats d'une recherche sur le nom de computer
	 * et de compagnie.
	 * @param search le nom de recherche
	 * @param numeroPage le numéro de la page a récupérer
	 * @param limit le nombre d'objets a instancier
	 * @return une liste de computers dans une page
	 * @throws DaoException exception sur la requête
	 */
	public Page<Computer> getPageSearch(final String search, final int numeroPage, final int limit)
			throws DaoException {
		if (search == null || numeroPage < 1 || limit < 1) {
			throw new DaoIllegalArgumentException();
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new DaoCharacterSpeciauxException();
		}
		Page<Computer> page = new Page<Computer>(limit, (int) this.getSearchCount(search));
		List<Computer> companies = jt.query(SQL_SEARCH_PAGE_COMPUTER, new RowMapperComputer(), "%" + search + "%",
				"%" + search + "%", page.getLimit(), page.offset(numeroPage));
		page.charge(companies, numeroPage);
		return page;
	}

}
