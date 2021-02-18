//Bismillah Hirrahman Nirrahim

package com.jogger.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jogger.report.Report;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	private String password;
	/*
	@ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "users_privileges", 
      joinColumns = 
        @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = 
        @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Set<Privilege> privileges;
	*/
	//@JsonUnwrapped // this allows properties to be "unwrapped". See: https://stackoverflow.com/questions/55175224/jackson-rest-api-add-entity-field-with-its-id-only-in-manytoone
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
    private Set<Jog> jogs = new HashSet<Jog>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
    private Set<Report> reports = new HashSet<Report>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = password;
		String encodedPassword = encoder.encode(rawPassword);
		
		this.password = encodedPassword;
	}
	
	/*
	public Set<Privilege> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(Set<Privilege> type) {
		this.privileges = type;
	}
	*/
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	

}
