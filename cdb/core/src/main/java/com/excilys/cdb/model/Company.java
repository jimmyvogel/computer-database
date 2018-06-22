package com.excilys.cdb.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Classe représentant une compagnie de manière simple, sans détails.
 * @author vogel
 *
 */
@Entity
@Table(name = "Company")
public class Company {

	// VALIDATION DATAS
	public static final int TAILLE_MIN_NAME = 3;
	public static final int TAILLE_MAX_NAME = 60;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// Nom de la compagnie
	private String name;

	@OneToMany(mappedBy="company")
	private List<Computer> computers;
	
	private String image;
	
	private String description;
	/**
	 * Constructor. no arguments
	 */
	public Company() {
	}
	public Company(final String name) {
		this(0, name);
	}
	public Company(final long id, final String name) {
		this(id, name, null);
	}
	public Company(final long id, final String name, final String description) {
		this(id, name, null, null);
	}
	public Company(final long id, final String name, final String description, final String image) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
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
	
	

	public List<Computer> getComputers() {
		return computers;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	
	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setDescription(String description) {
		this.description = description;
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
		Company other = (Company) obj;
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
