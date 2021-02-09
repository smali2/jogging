// Bismillah Hirrahman Nirrahim

package com.toptal.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.OrganizationRepository;
import com.toptal.model.Organization;

@RestController
public class OrganizationController {

	@Autowired
	OrganizationRepository repo;
	
	@GetMapping("/organizations/{id}")
	@ResponseBody
	public Optional<Organization> findOrgById(@PathVariable Long id) {
	    return Optional.of(repo.findById(id).orElse(null));
	}
	
	// The following method is theoretical. If the Org ID is being used by a user, 
	// then there will be a foreign key error.
	/*
	@DeleteMapping("/organizations/{id}")
	@ResponseBody
	public Optional<Organization> deleteById(@PathVariable Long id) {
		Optional<Organization> org = Optional.of(repo.findById(id).orElse(null));
	    repo.deleteById(id);
		return org;
	}
	*/
	
	// The following method is theoretical. It won't work since Organization's
	// Role field is an enum. Any attempt will fail with a 400 Bad Request.
	@PostMapping("/organization")
	@ResponseBody
	public Organization addOrg(@RequestBody Organization org) {
	    repo.save(org);
		return org;
	}
	
	// The following method is mostly theoretical and just here for amusement. 
	// If name field is changed, it won't work since Organization's
	// name field is an enum. Any attempt will fail with a 400 Bad Request.
	// If the Org ID is changed, then that will not work if the ID is being
	// used by other users currently (i.e., foreign key error)
	@PutMapping("/organization")
	@ResponseBody
	public Organization putOrg(@RequestBody Organization org) {
	    repo.save(org);
		return org;
	}
	
}
