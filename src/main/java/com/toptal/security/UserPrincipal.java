// Bismillah Hirrahman Nirrahim

package com.toptal.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.toptal.model.Privilege;
import com.toptal.model.User;


public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -891359123811734879L;

	private User user;
	
	public UserPrincipal(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// This is a key method! It describes whether authorities are managed
	// at the privilege level or the organization level.
	// We have left it at the privilege level, because it is possible
	// that we want to modify certain CRUD privileges at the organization level
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		// Code for granting authorities at organization level
		authorities.add(new SimpleGrantedAuthority(user.getOrganization().getName().toString()));
        
		
		/* Code for granting authorities at privilege level
		for (Privilege privilege : user.getOrganization().getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        */
        return authorities;
		
		//return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
