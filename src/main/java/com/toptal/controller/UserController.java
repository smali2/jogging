//Bismillah Hirrahman Nirrahim


package com.toptal.controller;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.UserRepository;
import com.toptal.model.Jog;
import com.toptal.model.Role;
import com.toptal.model.User;
import com.toptal.security.UserPrincipal;

@RestController
public class UserController {
	
	@Autowired
	UserRepository repo;
	
	@GetMapping(path ="/users", produces= {"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public Page<User> getUsers(Pageable p) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	
    	Page<User> result;
    	if (loggedInUser.getOrganization().getName().equals((Role.ADMIN))) {
    		result = repo.findAll(p);
    	} else if (loggedInUser.getOrganization().getName().equals((Role.MANAGER))) {
    		result = repo.getAllRoleUser(p);
    	} else {
    		result = repo.findByUsername(loggedInUser.getUsername(), p);
    		
    	}
                
        return result;
    }
	
	@GetMapping(path ="/user/{id}", produces= {"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public Optional<User> findById(@PathVariable long id) {
    	Optional<User> res = repo.findById(id);
    	if (res.isPresent()) {
    		res.get().setPassword("<encrypted>");
    	}
        return res;
    }

    @PostMapping(path="/user", consumes={"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public User create(@RequestBody User user) {
    	if (user==null || user.getUsername()==null || user.getPassword()==null || user.getOrganization()==null || user.getOrganization().getId()==null || user.getOrganization().getName()==null) {
    		throw new IllegalArgumentException("You can leave the user ID field as null, but all the other fields cannot be null!");
    	}
    	
    	if ((user.getOrganization().getName().ordinal()+1)!=user.getOrganization().getId().intValue()) {
    		throw new DataIntegrityViolationException("Make sure that the Org ID and Org Name match! 1=User, 2=Manager, 3=Admin");
    	}
    	
    	if (repo.findByUsername(user.getUsername())!=null) {
    		throw new ConstraintViolationException("Sir, this username is already taken. Please choose another one.", null, null);
    	}
    	
    	if (user.getId()==null || repo.findById(user.getId()).isPresent()) {
    		// Let the ID autoincrement
    		user.setId(null);
    	} 
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.MANAGER) && user.getOrganization().getName().ordinal()>0) {
    		throw new DataIntegrityViolationException("You are a Manager and you can only make accounts of id=1, name=USER");
    	}
    	
    	repo.save(user);
        return user;
    }
    
    @PutMapping(path="/user/{id}", consumes={"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public User update(@RequestBody User user, @PathVariable Long id) {
    	if (user==null || user.getUsername()==null || user.getPassword()==null || user.getOrganization()==null || user.getOrganization().getId()==null || user.getOrganization().getName()==null) {
    		throw new IllegalArgumentException("You can leave the user ID field as null, but all the other fields cannot be null!");
    	}
    	
    	if ((user.getOrganization().getName().ordinal()+1)!=user.getOrganization().getId().intValue()) {
    		throw new DataIntegrityViolationException("Make sure that the Org ID and Org Name match! 1=User, 2=Manager, 3=Admin");
    	}
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.MANAGER) && user.getOrganization().getName().ordinal()>0) {
    		throw new DataIntegrityViolationException("You are a Manager and you can only modify accounts of id=1, name=USER");
    	}
    	
    	return repo.findById(id)
    		      .map(curUser -> {
    		        curUser.setUsername(user.getUsername());
    		        curUser.setOrganization(user.getOrganization());
    		        curUser.setPassword(user.getPassword());
    		        return repo.save(curUser);
    		      })
    		      .orElseGet(() -> {
    		    	return create(user);
    		      });
    	
    	
    	
    	//repo.save(user);
        //return user;
    }
    
    @PutMapping(path="/user/{username}", consumes={"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public User update(@RequestBody User user, @PathVariable String username) {
    	if (user==null || user.getUsername()==null || user.getPassword()==null || user.getOrganization()==null || user.getOrganization().getId()==null || user.getOrganization().getName()==null) {
    		throw new IllegalArgumentException("You can leave the user ID field as null, but all the other fields cannot be null!");
    	}
    	
    	if ((user.getOrganization().getName().ordinal()+1)!=user.getOrganization().getId().intValue()) {
    		throw new DataIntegrityViolationException("Make sure that the Org ID and Org Name match! 1=User, 2=Manager, 3=Admin");
    	}
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.MANAGER) && user.getOrganization().getName().ordinal()>0) {
    		throw new DataIntegrityViolationException("You are a Manager and you can only modify accounts of id=1, name=USER");
    	}
    	
    	Optional<User> userToEdit = repo.findByUsername(username);
    	if (userToEdit.isPresent()) {
    		
	    	userToEdit.get().setUsername(user.getUsername());
	    	userToEdit.get().setOrganization(user.getOrganization());
	    	userToEdit.get().setPassword(user.getPassword());
	    	return repo.save(userToEdit.get());
    	}
    		        
    	return create(user);
    }
    
    @DeleteMapping(path="/user/{id}", consumes={"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public void delete(@PathVariable Long id) {
    	User user = repo.getOne(id);
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.MANAGER) && user.getOrganization().getName().ordinal()>0) {
    		throw new DataIntegrityViolationException("You are a Manager and you can only delete accounts of id=1, name=USER");
    	}
    	
    	repo.deleteById(id);
    }
    
    @DeleteMapping(path="/user/{username}", consumes={"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseBody
    public void delete(@PathVariable String username) {
    	User user = repo.getUserByUsername(username);
    	if (user==null) return;
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.MANAGER) && user.getOrganization().getName().ordinal()>0) {
    		throw new DataIntegrityViolationException("You are a Manager and you can only delete of Role/Organization type USER");
    	}
    	repo.deleteById(user.getId());
    }
    
    
    /*
    private String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }
    */
}
