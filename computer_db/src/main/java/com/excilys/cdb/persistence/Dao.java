package main.java.com.excilys.cdb.persistence;

import java.util.List;

public interface Dao<T> {

	long getCount();

	T getById(long id);

	List<T> getAll();
	
	Page<T> getPage(int numeroPage);
	
}