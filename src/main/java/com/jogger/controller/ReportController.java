// Bismillah Hirrahman Nirrahim

package com.jogger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jogger.ReportRepository;
import com.jogger.model.Role;
import com.jogger.model.User;
import com.jogger.report.Report;
import com.jogger.security.UserPrincipal;

@RestController
public class ReportController {

	@Autowired
	ReportRepository repo;
	

	@GetMapping(path ="/reports", produces= {"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseBody
	public Page<Report> generateReport(Pageable p) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Page<Report> result;
    	if (loggedInUser.getOrganization().getName().equals(Role.ADMIN)) {
    		// generate for all users
    		result = repo.findAll(p);
    		return result;
    	} else {
    		result = repo.findAllByUser(loggedInUser.getId(), p);
    	}
    	
    	return result;
	}
	
	@GetMapping(path ="/report/{userid}", produces= {"application/json"})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseBody
	public Page<Report> generateReport(@PathVariable Long userid, Pageable p) {
    	Page<Report> result;
    	result = repo.findAllByUser(userid, p);
    	return result;
	}
	
}
