package com.excilys.cdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authorities {
	@Id
	private String authority;

	@ManyToOne
	@JoinColumn(name = "username")
	private User user;


	/**
	 * Constructor par défaut.
	 */
	public Authorities() {
	}
	
	public String getAuthority() {
		return authority;
	}

	public User getUser() {
		return user;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
