package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 *
 */
public class ComputerServiceImpl implements IComputerService {

    private ComputerDao computerDao;
    private CompanyDao companyDao;

    private static ComputerServiceImpl service;

    /**
     * Récupérer une instance de type computerServiceImpl.
     * @return une référence sur le singleton ComputerServiceImpl.
     * @throws DAOConfigurationException erreur de configuration
     */
    public static ComputerServiceImpl getInstance()
            throws DAOConfigurationException {
        if (service == null) {
            Logger logger = LoggerFactory.getLogger(ComputerServiceImpl.class);
            logger.info("Initialisation du singleton computer service");

            service = new ComputerServiceImpl();
            DaoFactory factory = DaoFactory.getInstance();
            service.computerDao = (ComputerDao) factory
                    .getDao(DaoFactory.DaoType.COMPUTER);
            service.companyDao = (CompanyDao) factory
                    .getDao(DaoFactory.DaoType.COMPANY);
        }
        return service;
    }

    /**
     * Retourne tous les computers de la bdd.
     * @return une liste
     * @throws ServiceException exception de service
     */
    public List<Computer> getAllComputer() throws ServiceException {
        List<Computer> list = new ArrayList<Computer>();
        try {
            list = computerDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return list;
    }

    /**
     * Retourne toutes les company de la bdd.
     * @return une liste
     * @throws ServiceException exception de service.
     */
    public List<Company> getAllCompany() throws ServiceException {
        List<Company> list = new ArrayList<Company>();
        try {
            list = companyDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return list;
    }

    /**
     * Optenir une page de company.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    public Page<Company> getPageCompany(int page)
            throws ServiceException {
        Page<Company> pageCompany = null;
        try {
            pageCompany = companyDao.getPage(page);
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageCompany;
    }

    /**
     * Optenir une page de computer.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    public Page<Computer> getPageComputer(int page) throws ServiceException {
        Page<Computer> pageComputer = null;
        try {
            pageComputer = computerDao.getPage(page);
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageComputer;
    }

    /**
     * Retourne une company de la bdd.
     * @param id l'id de l'élément à récupérer
     * @return la Company résultant
     * @throws ServiceException exception de service
     */
    public Company getCompany(long id) throws ServiceException {
        Company company = null;
        try {
            company = companyDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return company;
    }

    /**
     * Retourne un computer de la bdd.
     * @param id l'id de l'élément à récupérer
     * @return le Computer résultant
     * @throws ServiceException exception de service
     */
    public Computer getComputer(long id) throws ServiceException {
        Computer computer = null;
        try {
            computer = computerDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return computer;
    }

    /** Créer un computer.
     * @param name le nom de la compagnie
     * @return boolean le résultat
     * @throws ServiceException erreur de service.
     */
    public long createComputer(String name) throws ServiceException {
        long result = -1;

        if (name != null && Computer.validName(name)) {
            String s = validTextProcess(name);
            Computer c = new Computer(s);
            try {
                result = computerDao.create(c);
            } catch (DaoException e) {
                throw new ServiceException("Méthode dao fail", e);
            }
        }
        return result;
    }

    /** Créer un computer.
     * @param name le nom de la compagnie ou null
     * @param introduced la date ou null
     * @param discontinued la date d'arret ou null
     * @param companyId l'id de la company ou -1.
     * @return boolean le résultat
     * @throws ServiceException erreur de service.
     */
    public long createComputer(String name, LocalDateTime introduced,
            LocalDateTime discontinued, long companyId)
            throws ServiceException {
        if (name == null) {
            return -1;
        }

        long result = -1;

        if (Computer.validName(name)) {

            String s = validTextProcess(name);
            Computer c = new Computer(s);

            // Verification validité date, null est accepté.
            if (introduced == null || Computer.validDate(introduced)) {
                c.setIntroduced(introduced);
            }

            if (discontinued == null || Computer.validDate(discontinued)) {
                c.setDiscontinued(discontinued);
            }

            // Soit aucun des deux, soit les deux avec verif sur l'écart, soit
            // juste introduced.
            if (discontinued == null
                    || (introduced == null && discontinued == null)
                    || (introduced != null && discontinued != null
                            && introduced.isBefore(discontinued))) {

                if (companyId > 0) {
                    try {
                        Company inter = companyDao.getById(companyId);
                        if (inter != null) {
                            c.setCompany(inter);
                        }
                    } catch (DaoException e) {
                        throw new ServiceException("Méthode dao fail on getById", e);
                    }
                }

                try {
                    result = computerDao.create(c);
                } catch (DaoException e) {
                    throw new ServiceException("Méthode dao fail", e);
                }
            }

        }
        return result;
    }

    /** Détruire un computer.
     * @param id l'id du computer à supprimer
     * @return un boolean représentant le résultat
     * @throws ServiceException erreur de service
     */
    public boolean deleteComputer(long id) throws ServiceException {
        if (id < 1) {
            return false;
        }

        boolean result = false;
        try {
            result = computerDao.deleteComputer(id);
        } catch (DaoException e) {
            throw new ServiceException("Méthode de dao fail", e);
        }
        return result;
    }

    /** Modifié un computer.
     * @param id l'id du computer a modifié
     * @param name le nouveau nom du computer
     * @return boolean le résultat
     * @throws ServiceException erreur de service.
     */
    public boolean updateComputer(long id, String name)
            throws ServiceException {

        boolean result = false;
        if (name != null && Computer.validName(name)) {
            String s = validTextProcess(name);
            Computer c;
            try {
                c = computerDao.getById(id);
            } catch (DaoException e) {
                throw new ServiceException("Méthode de la dao getbyid fail", e);
            }
            if (c != null) {
                c.setName(s);
                try {
                    result = computerDao.update(c);
                } catch (DaoException e) {
                    throw new ServiceException("Méthode de la dao update fail",
                            e);
                }
            }
        }

        return result;
    }

    /** Modifié un computer.
     * @param id l'id du computer à modifié.
     * @param name le nouveau nom de la compagnie ou null
     * @param introduced la nouvelle date ou null
     * @param discontinued la nouvelle date d'arret ou null
     * @param companyId l'id de la nouvelle company, 0 pour supprimer, ou -1 pour null.
     * @return un boolean pour le résultat
     * @throws ServiceException erreur de service.
     */
    public boolean updateComputer(long id, String name,
            LocalDateTime introduced, LocalDateTime discontinued,
            long companyId) throws ServiceException {
        if (name == null && introduced == null && discontinued == null
                && companyId < 0) {
            return false;
        }

        if (name != null && !Computer.validName(name)) {
            return false;
        }

        // Verification validité date.
        if (introduced != null && !Computer.validDate(introduced)) {
            return false;
        }
        if (discontinued != null && !Computer.validDate(discontinued)) {
            return false;
        }

        boolean result = false;
        try {
            // Recuperation initial.
            Computer initial = computerDao.getById(id);

            if (initial == null) {
                throw new ServiceException("Le computer spécifié n'existe pas.");
            }

            // Initialisation du nouveau computer.
            Computer nouveau = new Computer(initial.getId(), initial.getName(),
                    initial.getIntroduced(), initial.getDiscontinued(),
                    initial.getCompany());

            if (name != null) {
                String s = validTextProcess(name);
                nouveau.setName(s);
            }

            // Gestion des cas ou introduced ou discontinued existe
            if (introduced != null || discontinued != null) {

                // Les deux existent
                if (introduced != null && discontinued != null) {
                    if (introduced.isAfter(discontinued)) {
                        return false;
                    }
                    nouveau.setIntroduced(introduced);
                    nouveau.setDiscontinued(discontinued);
                } else {
                    // Seulement introduced est present
                    if (introduced != null) {

                        if (initial.getDiscontinued() != null && introduced
                                .isAfter(initial.getDiscontinued())) {
                            return false;
                        }

                        // Pas de problème temporel si le discontinued
                        // n'existait pas.
                        nouveau.setIntroduced(introduced);
                    } else {
                        // Discontinued ne peut exister si il n'y en avait pas
                        // avant.
                        if (initial.getIntroduced() == null) {
                            return false;
                        }
                        if (discontinued.isBefore(initial.getIntroduced())) {
                            return false;
                        }
                    }
                }
            }

            // Gestion différente selon si on a déjà une company lié ou non.
            if (companyId > 0) {
                Company comp = companyDao.getById(companyId);
                if (comp == null) {
                    return false;
                }
                nouveau.setCompany(comp);

            //Suppression si on choisit 0;
            } else if (companyId == 0) {
                nouveau.setCompany(null);
            }

            result = computerDao.update(nouveau);

        } catch (DaoException e) {
            throw new ServiceException("Méthode de la dao fail", e);
        }

        return result;
    }

    /** Récupérer le nombre de computers.
     * @return un type long
     * @throws ServiceException erreur de service.
     */
    public long countComputers() throws ServiceException {
        long count = 0;
        try {
            count = computerDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Méthode de la dao fail", e);
        }
        return count;
    }

    /** Récupérer le nombre de compagnies existantes.
     * @return un type long
     * @throws ServiceException erreru de service.
     */
    public long countCompanies() throws ServiceException {
        long count = 0;
        try {
            count = companyDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Méthode de la dao fail", e);
        }
        return count;
    }

    /**
     * Méthode pour modifier en valide le format d'un string.
     * @param s
     *            le string a valider
     * @return un objet de type String.
     */
    public String validTextProcess(String s) {
        return s.replaceAll("<[^>]*>", "");
    }
}
