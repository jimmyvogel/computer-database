package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.exceptions.DaoException;

/**
 * Classe contenant les requêtes possibles sur la table des company de la base
 * de donnée.
 * @author vogel
 *
 */
public class CompanyDao implements Dao<Company> {

    private DaoFactory factory;
    private static CompanyDao dao;

    private static final String SQL_ALL_COMPANIES = "SELECT `id`,`name` FROM `company`";
    private static final String SQL_ONE_COMPANY = "SELECT `id`,`name` FROM `company` WHERE `id`=?";
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

            Logger logger = LoggerFactory.getLogger(CompanyDao.class);
            logger.info("Initialisation du singleton de type CompanyDao");

            dao = new CompanyDao();
            dao.factory = factory;
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
        try {
            Connection c = factory.getConnection();
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(SQL_ALL_COMPANIES);

            /*
             * Parcours de la ligne de données de l'éventuel ResulSet retourné
             */
            while (result.next()) {
                companies.add(MapperDao.mapCompany(result));
            }

            stmt.close();
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
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
        try {
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPANY);
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            /*
             * Parcours de la ligne de données de l'éventuel ResulSet retourné
             */
            if (result.next()) {
                company = MapperDao.mapCompany(result);
            }

            stmt.close();

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
        try {
            Connection c = factory.getConnection();
            Statement stmt = c.createStatement();
            String query = SQL_COUNT_COMPANY;
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                count = result.getLong("total");
            }
            stmt.close();

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
        try {
            List<Company> companies = new ArrayList<Company>();
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPANY);
            stmt.setInt(1, page.getLimit());
            stmt.setInt(2, page.offset(numeroPage));

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                companies.add(MapperDao.mapCompany(result));
            }
            page.charge(companies, numeroPage);
            stmt.close();

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }
        return page;
    }

}
