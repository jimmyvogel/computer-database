package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Factory pour la création des daos gérant les requêtes sur la base de donnée.
 * @author vogel
 *
 */
public class DaoFactory {

    // Variables du fichier properties
    private static final String FICHIER_PROPERTIES = "dao.properties";
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_NOM_UTILISATEUR = "username";
    private static final String PROPERTY_MOT_DE_PASSE = "password";

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    // Variables pour récupérer les id max à l'initialisation pour gérer
    // l'auto-incrémentation en manuelle.
    private static boolean initialized = false;

    // DaoFactory unique
    private static DaoFactory dao;

    // Enum pour la généricité de la méthode factory.
    public enum DaoType {
        COMPUTER, COMPANY
    };

    /**
     * Initialisation de la factory pour créer le singleton.
     * @throws DAOConfigurationException erreur de configuration.
     */
    private static void initializeFactory() throws DAOConfigurationException {
        Logger logger = LoggerFactory.getLogger(DaoFactory.class);
        logger.info("Initialisation du singleton dao factory");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DAOConfigurationException("Driver introuvable");
        }
        dao = new DaoFactory();

        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        InputStream fichierProperties = classLoader
                .getResourceAsStream(FICHIER_PROPERTIES);
        if (fichierProperties == null) {
            throw new DAOConfigurationException("Le fichier properties "
                    + FICHIER_PROPERTIES + " est introuvable.");
        }
        try {
            properties.load(fichierProperties);
            config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
            config.setUsername(properties.getProperty(PROPERTY_NOM_UTILISATEUR));
            config.setPassword(properties.getProperty(PROPERTY_MOT_DE_PASSE));
            //config.addDataSourceProperty( "cachePrepStmts" , "true" );
            //config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
            //config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
            ds = new HikariDataSource(config);
        } catch (IOException e) {
            throw new DAOConfigurationException(
                    "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
        }

        initialized = true;
    }

    /**
     * Retourne une instance de DaoFactory.
     * @return un objet DaoFactory
     * @throws DAOConfigurationException
     *             envoie une erreur de config si exception.
     * @throws SQLException
     */
    public static DaoFactory getInstance() throws DAOConfigurationException {
        if (!initialized) {
            initializeFactory();
        }
        return dao;
    }

    /**
     * Retourne une connection à la base de donnée.
     * @return an object of class Connection
     * @throws DAOConfigurationException configuration hikari exception
     */
    public Connection getConnection() throws DAOConfigurationException {
        Connection c = null;
        try {
            c = ds.getConnection();
        } catch (SQLException e) {
            throw new DAOConfigurationException("Connection impossible", e);
        }
        return c;
    }

    /**
     * Factory pour dao company.
     * @param daoType le type de dao demandé.
     * @return une dao
     */
    public Dao<?> getDao(DaoType daoType) {
        Dao<?> res = null;
        switch (daoType) {
        case COMPANY:
            res = CompanyDao.getInstance(this);
            break;
        case COMPUTER:
            res = ComputerDao.getInstance(this);
            break;
        }
        return res;
    }

}
