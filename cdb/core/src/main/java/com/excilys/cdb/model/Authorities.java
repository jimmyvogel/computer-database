package com.excilys.cdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Authorities")
public class Authorities {
	
	public enum Role {
		ROLE_USER,
		ROLE_ADMIN;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "authority")
    //@Enumerated(EnumType.STRING)
	private String authority;


	/**
	 * Constructor par défaut.
	 */
	public Authorities() {
	}
	
	public Authorities(Role roleUser) {
		this.authority = roleUser.toString();
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
