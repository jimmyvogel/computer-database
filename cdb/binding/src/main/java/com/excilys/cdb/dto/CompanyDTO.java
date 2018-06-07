package com.excilys.cdb.dto;

import java.io.Serializable;

/**
 * Classe représentant une compagnie de manière simple, sans détails.
 * @author vogel
 *
 */
public class CompanyDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3118136719983988230L;
	
	private long id;
	private String name;

	/**
	 * Constructor. no arguments
	 */
	public CompanyDTO() {
	}

	/**
	 * Constructor.
	 * @param id l'id de la compagnie
	 * @param name le nom de la compagnie
	 */
	public CompanyDTO(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Constructor company.
	 * @param name le nom de la company
	 */
	public CompanyDTO(final String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CompanyDTO other = (CompanyDTO) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

}
