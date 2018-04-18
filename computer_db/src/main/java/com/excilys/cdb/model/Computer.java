package main.java.com.excilys.cdb.model;

import java.time.LocalDateTime;
import java.time.Month;

public class Computer {
	
	public static final int TAILLE_MAX_NAME = 100;
	
	public static final LocalDateTime BEGIN_DATE_VALID = 
			LocalDateTime.of(1949, Month.DECEMBER, 31, 23, 59);
	
	public static final LocalDateTime END_DATE_VALID = 
			LocalDateTime.of(2100, Month.JANUARY, 1, 0, 0);
	
	private long id;
	private String name;
	private LocalDateTime introduced;
	private LocalDateTime discontinued;
	private Company company;
	
	public Computer() {}
	public Computer(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Computer(String name) {
		this.name = name;
		this.id = 0;
	}
	
	public Computer(long id, String name, 
			LocalDateTime introduced, LocalDateTime discontinued, Company company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	public Computer(String name, 
			LocalDateTime introduced, LocalDateTime discontinued, Company company) {
		this(0, name, introduced, discontinued, company);
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getIntroduced() {
		return introduced;
	}
	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}
	public LocalDateTime getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ "]";
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static boolean validDate(LocalDateTime ldt) {
		if(ldt.isAfter(Computer.BEGIN_DATE_VALID)
				&& ldt.isBefore(Computer.END_DATE_VALID))
			return true;
		return false;
	}
	
	public static boolean validName(String name) {
		if(name.length() > TAILLE_MAX_NAME)
			return false;
		
		return true;
	}
	
}
