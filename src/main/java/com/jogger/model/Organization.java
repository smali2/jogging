//Bismillah Hirrahman Nirrahim

package com.jogger.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Organization {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "ENUM('ADMIN', 'MANAGER', 'USER')")
    @Enumerated(EnumType.STRING)
    private Role name;

    /* Enable the following block of code if you want to enable
     * privilege level control of each organization. For example,
     * if you want to remove PUT operation on User class for Manager level,
     * you can do that by removing Privilege name UPDATE_USER.
     * 
     * PrivilegeController and PrivilegeRepository will also need to be activated by 
     * activating class level annotations on each. Further, the @Entity
     * annotation also needs to be activated for Privilege class and UserPrincipal
     * class' getAuthorities() method will need modification.
     * 
     * Ensure that any commented out code is uncommented to active if 
     * necessary. For example, org field in Privilege class.
     * 
     * The use of Privilege class is only necessary if Custom Expression
     * Security at method level is required. For our purposes, it is not,
     * hence it is unused. 
     * 
     * Spring will need to resolve to CustomMethodSecurityExpressionRoot
     * to enable the isMember() method. The isMember() method will be used
     * with @Pre/PostAuthorization annotations for each method. Refer to 
     * guides for more details iA.
     */
    /*
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
    @JoinTable(name = "org_privileges", 
      joinColumns = 
        @JoinColumn(name = "org_id", referencedColumnName = "id"),
      inverseJoinColumns = 
        @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Set<Privilege> privileges = new HashSet<Privilege>();
    
    public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}
    *
    *
    */
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    private Set<User> users = new HashSet<User>();
    
	public Organization(Role name) {
		super();
		this.name = name;
	}

	public Organization() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getName() {
		return name;
	}

	public void setName(Role name) {
		this.name = name;
	}



    
}
