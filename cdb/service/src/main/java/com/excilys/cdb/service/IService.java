package com.excilys.cdb.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.service.exceptions.ServiceException;

@Service
@Transactional
public interface IService<T> {


	/**
	 * Détruire plusieurs T.
	 * @param ids id(s) des T à supprimer
	 * @return 
	 * @throws ServiceException set en paramètre vide
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	Long deleteAll(Set<Long> ids) throws ServiceException;

	/**
	 * Détruire un T.
	 * @param id l'id du T à supprimer
	 * @return un boolean représentant le résultat
	 * @throws ServiceException erreur de service
	 */
	Long delete(long id) throws ServiceException;

	/**
	 * Récupérer le nombre de T.
	 * @return un type long
	 * @throws ServiceException erreur de service.
	 */
	@Transactional(readOnly = true)
	long count() throws ServiceException;
}
