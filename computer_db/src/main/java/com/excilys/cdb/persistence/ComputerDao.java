package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DaoException;

/**
 * Classe contenant les requêtes possibles sur la table des computer de la base
 * de donnée.
 * @author vogel
 *
 */
public class ComputerDao implements Dao<Computer> {

    private DaoFactory factory;
    private static ComputerDao dao;

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
    private static final String SQL_DELETE_COMPUTER = "DELETE FROM `computer` WHERE `id`=?";
    private static final String SQL_COUNT_COMPUTER = "SELECT COUNT(`id`) AS `total` FROM `computer`";
    private static final String SQL_PAGE_COMPUTER = SQL_ALL_COMPUTERS
            + " LIMIT ? OFFSET ?";

    /**
     * Méthode de fabrique de computer dao.
     * @param daoFactory la factory de fabrication de composants daos.
     * @return une référence sur le singleton computer dao
     */
    public static ComputerDao getInstance(DaoFactory daoFactory) {
        if (dao == null) {

            Logger logger = LoggerFactory.getLogger(ComputerDao.class);
            logger.info("Initialisation du singleton de type ComputerDao");

            dao = new ComputerDao();
            dao.factory = daoFactory;
        }
        return dao;
    }

    /**
     * Retourne tous les computers de la bdd.
     * @return une liste
     * @throws DaoException exception de requête
     */
    public List<Computer> getAll() throws DaoException {
        List<Computer> computers = new ArrayList<Computer>();
        try {
            Connection c = factory.getConnection();
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(SQL_ALL_COMPUTERS);

            while (result.next()) {
                computers.add(MapperDao.mapComputer(result));
            }

            stmt.close();
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return computers;
    }

    /** Récupérer un Computer de la bdd.
     * @param id l'id du computer
     * @return un objet Computer
     * @throws DaoException erreur de reqûete.
     */
    public Computer getById(long id) throws DaoException {
        Computer computer = null;
        try {
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPUTER);
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                computer = MapperDao.mapComputer(result);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return computer;
    }

    /**
     * Créer un objet de type computer.
     * @param computer Un objet complet en argument
     * @return l'id de l'objet crée ou -1 si la création a échoué
     * @throws DaoException erreur sur la reqûete
     */
    public long create(final Computer computer) throws DaoException {
        long id = -1;
        try {
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_AJOUT_COMPUTER,
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, computer.getName());
            if (computer.getIntroduced() != null) {
                stmt.setTimestamp(2,
                        Timestamp.valueOf(computer.getIntroduced()));
            } else {
                stmt.setTimestamp(2, null);
            }

            if (computer.getDiscontinued() != null) {
                stmt.setTimestamp(3,
                        Timestamp.valueOf(computer.getDiscontinued()));
            } else {
                stmt.setTimestamp(3, null);
            }

            if (computer.getCompany() != null) {
                stmt.setLong(4, computer.getCompany().getId());
            } else {
                stmt.setString(4, null);
            }

            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.first()) {
                id = rs.getLong(1);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return id;
    }

    /** Méthode pour modifier un computer en bdd.
     * @param computer le computer a modifié.
     * @return le résultat en booléan.
     * @throws DaoException exception sur la requête
     */
    public boolean update(final Computer computer) throws DaoException {
        int result = 0;
        try {
            Computer before = this.getById(computer.getId());

            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_UPDATE_COMPUTER);
            stmt.setString(1, computer.getName());

            if (computer.getIntroduced() != null) {
                stmt.setTimestamp(2,
                        Timestamp.valueOf(computer.getIntroduced()));
            } else if (before.getIntroduced() != null) {
                stmt.setTimestamp(2,
                        Timestamp.valueOf(before.getIntroduced()));
            } else {
                stmt.setString(2, null);
            }

            if (computer.getDiscontinued() != null) {
                stmt.setTimestamp(3,
                        Timestamp.valueOf(computer.getDiscontinued()));
            } else if (before.getDiscontinued() != null) {
                    stmt.setTimestamp(3,
                            Timestamp.valueOf(before.getDiscontinued()));
            } else {
                stmt.setString(3, null);
            }


            //Si on veut garder le même il faut le garder en paramètre.
            if (computer.getCompany() == null) {
                stmt.setString(4, null);
            } else {
                stmt.setLong(4, computer.getCompany().getId());
            }

            stmt.setLong(5, computer.getId());

            result = stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return (result > 0) ? true : false;
    }

    /** Méthode pour supprimer un computer.
     * @param id l'id de l'objet à supprimer
     * @return un boolean pour connaitre le résultat
     * @throws DaoException exception sur la requête
     */
    public boolean deleteComputer(final long id) throws DaoException {

        int result = 0;
        try {
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_DELETE_COMPUTER);
            stmt.setLong(1, id);

            result = stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return (result > 0) ? true : false;
    }

    /**
     * Méthode pour récupérer le nombre d'objets de type computer en bdd.
     * @return le nombre d'instances
     * @throws DaoException
     *             exception sur la requête
     */
    public long getCount() throws DaoException {
        long count = 0;
        try {
            Connection c = factory.getConnection();
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(SQL_COUNT_COMPUTER);
            if (result.next()) {
                count = result.getLong("total");
            }
            stmt.close();

        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }

        return count;
    }

    /** Méthode pour récupérer les objets d'une certaine page de computer.
     * @param numeroPage le numéro de la page a récupérer
     * @return une liste de computers dans une page
     * @throws DaoException exception sur la requête
     */
    public Page<Computer> getPage(final int numeroPage) throws DaoException {
        return getPage(numeroPage, LIMIT_DEFAULT);
    }

    /** Méthode pour récupérer un nombre spécifié d'objets d'une certaine page de computer.
     * @param numeroPage le numéro de la page a récupérer
     * @param limit le nombre d'objets a instancier
     * @return une liste de computers dans une page
     * @throws DaoException exception sur la requête
     */
    public Page<Computer> getPage(final int numeroPage, final int limit) throws DaoException {
        Page<Computer> page = new Page<Computer>(limit, (int) this.getCount());
        try {
            List<Computer> computers = new ArrayList<Computer>();
            Connection c = factory.getConnection();
            PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPUTER);
            stmt.setInt(1, page.getLimit());
            stmt.setInt(2, page.offset(numeroPage));

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                computers.add(MapperDao.mapComputer(result));
            }
            page.charge(computers, numeroPage);

            stmt.close();
        } catch (SQLException e) {
            throw new DaoException("Requête exception", e);
        }
        return page;
    }

}
