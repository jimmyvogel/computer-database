package com.excilys.cdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {
	@Id
	@Column(name = "username")
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@ManyToMany
	private Set<Authorities> authorities = new HashSet<>();

	/**
	 * Constructor par défaut.
	 */
	public User() {
		this(null, null);
	}
	
	public User(String username, String password, Set<Authorities> authorities, boolean enabled) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.enabled = enabled;
	}
	
	public User(String username, String password) {
		this(username, password, new HashSet<Authorities>(), true);
	}
	
	public void addAuthority(Authorities authority) {
		this.authorities.add(authority);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}

}