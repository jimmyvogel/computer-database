package com.excilys.cdb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private String description;
	private String image;
	private List<ComputerDTO> computers;

	public CompanyDTO() {
	}
	public CompanyDTO(final long id, final String name) {
		this(id, name, null);
	}
	public CompanyDTO(final long id, final String name, final String description) {
		this(id, name, description, null);
	}
	public CompanyDTO(final long id, final String name, final String description, final String image) {
		this(id, name, description, image, new ArrayList<ComputerDTO>());
	}
	public CompanyDTO(final long id, final String name, final String description, final String image, final List<ComputerDTO> computers) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
		this.computers = computers;
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

	
	public List<ComputerDTO> getComputers() {
		return computers;
	}

	public void setComputers(List<ComputerDTO> computers) {
		this.computers = computers;
	}

	
	public String getDescription() {
		return description;
	}
	public String getImage() {
		return image;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImage(String image) {
		this.image = image;
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
