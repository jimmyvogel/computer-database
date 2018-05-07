package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;

/**
 * Classe contenant les requêtes possibles sur la table des company de la base
 * de donnée.
 * @author vogel
 *
 */
public class CompanyDao implements Dao<Company> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDao.class);
    private DaoFactory daoFactory;
    private static CompanyDao dao;

    private static final String SQL_AJOUT_COMPANY = "INSERT into `company` (name) VALUES (?)";
    private static final String SQL_ALL_COMPANIES = "SELECT `id`,`name` FROM `company`";
    private static final String SQL_ONE_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `id`=?";
    private static final String SQL_DELETE_LINKED_COMPUTER = "DELETE FROM `computer` WHERE `company_id`=?";
    private static final String SQL_DELETE_COMPANY = "DELETE FROM `company` WHERE `id`=?";
    private static final String SQL_COUNT_COMPANY = "SELECT COUNT(`id`) AS `total` FROM `company`";
    private static final String SQL_PAGE_COMPANY = SQL_ALL_COMPANIES
            + " LIMIT ? OFFSET ?";

    /**
     * Méthode de fabrique de company dao.
     * @param factory
     *            la factory de fabrication de composants daos.
     * @return une référence sur le singleton company dao
     */
    public static CompanyDao getInstance(final DaoFactory factory) {
        if (dao == null) {
            LOGGER.info("Initialisation du singleton de type CompanyDao");

            dao = new CompanyDao();
            dao.daoFactory = factory;
            LOGGER.info("Singleton companyDao intialisé");
        }
        return dao;
    }

    /**
     * Méthode pour récupérer toutes les entreprises de la bdd.
     * @return une référence sur le singleton company dao
     * @throws DaoException
     *             exception sur la requête
     */
    public List<Company> getAll() throws DaoException {
        List<Company> companies = new ArrayList<Company>();
        try (Connection c = daoFactory.getConnection(); Statement stmt = c.createStatement()) {
            ResultSet result = stmt.executeQuery(SQL_ALL_COMPANIES);
            while (result.next()) {
                companies.add(MapperDao.mapCompany(result));
            }

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        } catch (DAOConfigurationException e1) {
            throw new DaoException("Config exception", e1);
        }

        return companies;
    }

    /**
     * Méthode pour récupérer une entreprise.
     * @param id l'élément à récupérer
     * @return une référence sur le singleton company dao
     * @throws DaoException
     *             exception sur la requête
     */
    public Optional<Company> getById(final long id) throws DaoException {
        Company company = null;
        try (Connection c = daoFactory.getConnection(); PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPANY)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            /*
             * Parcours de la ligne de données de l'éventuel ResulSet retourné
             */
            if (result.next()) {
                company = MapperDao.mapCompany(result);
            }

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return Optional.ofNullable(company);
    }

    /**
     * Méthode pour récupérer le nombre d'objets de type entreprise en bdd.
     * @return le nombre d'instances
     * @throws DaoException
     *             exception sur la requête
     */
    public long getCount() throws DaoException {
        long count = 0;
        try (Connection c = daoFactory.getConnection(); Statement stmt = c.createStatement()) {
            String query = SQL_COUNT_COMPANY;
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                count = result.getLong("total");
            }
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return count;
    }

    /** Méthode pour récupérer les objets d'une certaine page.
     * @param numeroPage le numéro de la page a récupérer
     * @return une liste d'objets dans une page
     * @throws DaoException
     *             exception sur la requête
     */
    public Page<Company> getPage(final int numeroPage) throws DaoException {
        return getPage(numeroPage, LIMIT_DEFAULT);
    }

    /** Méthode pour récupérer un nombre spécifié d'objets d'une certaine page.
     * @param numeroPage le numéro de la page a récupérer
     * @param limit nombre d'objets à récupérer
     * @return une liste d'objets dans une page
     * @throws DaoException
     *             exception sur la requête
     */
    public Page<Company> getPage(final int numeroPage, final int limit) throws DaoException {
        Page<Company> page = new Page<Company>(limit, (int) this.getCount());
        List<Company> companies = new ArrayList<Company>();
        try (Connection c = daoFactory.getConnection(); PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPANY)) {
            stmt.setInt(1, page.getLimit());
            stmt.setInt(2, page.offset(numeroPage));
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                companies.add(MapperDao.mapCompany(result));
            }
            page.charge(companies, numeroPage);
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
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
        int resultCompany = 0;
        boolean deleteOk = true;
        Connection c = daoFactory.getConnection();
        try (PreparedStatement stmtCompany = c.prepareStatement(SQL_DELETE_COMPANY);
                PreparedStatement stmtComputer = c.prepareStatement(SQL_DELETE_LINKED_COMPUTER)) {

            Transaction.beginTransaction(c);
            stmtCompany.setLong(1, id);
            stmtComputer.setLong(1, id);
            resultCompany = stmtCompany.executeUpdate();
            stmtComputer.executeUpdate();
            if (resultCompany <= 0) {
                deleteOk = false;
            }
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                LOGGER.error("Rollback fail.");
                throw new DaoException("Requête fail.", e);
            }
            throw new DaoException("Requête exception", e);
        }

        Transaction.endTransaction(c, deleteOk);

        return deleteOk;
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
        Connection c = daoFactory.getConnection();
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
                throw new DaoException("Requête fail.", e);
            }
            throw new DaoException("Requête exception", e);
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
        try (Connection c = daoFactory.getConnection();
                PreparedStatement stmt = c.prepareStatement(SQL_AJOUT_COMPANY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, company.getName());
            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.first()) {
                id = rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return id;
    }

}
