package com.sou.rest.basicauth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@NamedQueries({ @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name ") })
@Table(name = "USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Column(name = "ID")
    private Long id;
	
	@Id
	@NotNull
	@Size(min = 5, max = 45)
	@Column(name = "NAME", length = 45)
	private String name;

	@NotNull
	@Size(min = 5, max = 16)
	@Column(name = "PASSWORD")
	private String password;
	
	@NotNull
	@Column(name = "ROLES", length = 45)
	@ElementCollection()
	@CollectionTable(name="USER_ROLES", joinColumns=@JoinColumn(name="USER_NAME"))
	private Set<String> roles;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name, Set<String> roles) {
		this.name = name;
		this.roles = roles;
	}

	public User(String name, String password, Set<String> roles) {
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return (int) (Math.random() * 100);
	}

	public String getPassword() {
		return password;
	}
	
	public Set<String> getRoles() {
		return roles;
	}
}
