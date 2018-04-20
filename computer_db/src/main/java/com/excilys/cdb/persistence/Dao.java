package com.excilys.cdb.persistence;

import java.util.List;

public interface Dao<T> {

	public long getCount();

	public T getById(long id);

	public List<T> getAll();
	
	public Page<T> getPage(int numeroPage);
	
}