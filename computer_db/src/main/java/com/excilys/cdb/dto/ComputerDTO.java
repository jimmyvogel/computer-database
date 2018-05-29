package com.excilys.cdb.dto;

import java.time.LocalDateTime;

import com.excilys.cdb.model.Computer;

/**
 * DTO de la class Computer.
 * @author vogel
 *
 */
public class ComputerDTO {

    private long id;
    private String name;
    private LocalDateTime introduced;
    private LocalDateTime discontinued;
    private long companyId;
    private String company;

    /**
     * Constructor empty for framework.
     */
    public ComputerDTO() { }

    /**
     * Constructor.
     * @param computer
     *            l'instance a transformer en dto.
     */
    public ComputerDTO(Computer computer) {
        this.id = computer.getId();
        this.name = computer.getName();
        this.introduced = computer.getIntroduced();
        this.discontinued = computer.getDiscontinued();
        if (computer.getCompany() != null) {
            this.companyId = computer.getCompany().getId();
        }
        if (computer.getCompany() != null) {
            this.company = computer.getCompany().getName();
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getIntroduced() {
        return introduced;
    }

    public LocalDateTime getDiscontinued() {
        return discontinued;
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((company == null) ? 0 : company.hashCode());
        result = prime * result + (int) (companyId ^ (companyId >>> 32));
        result = prime * result
                + ((discontinued == null) ? 0 : discontinued.hashCode());
        result = prime * result
                + ((introduced == null) ? 0 : introduced.hashCode());
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
        ComputerDTO other = (ComputerDTO) obj;
        if (company == null) {
            if (other.company != null) {
                return false;
            }
        } else if (!company.equals(other.company)) {
            return false;
        }
        if (companyId != other.companyId) {
            return false;
        }
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}

	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ComputerDTO [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", companyId=" + companyId + ", company=" + company + "]";
	}
}
