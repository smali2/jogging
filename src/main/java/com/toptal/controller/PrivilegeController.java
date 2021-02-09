//Bismillah Hirrahman Nirrahim

package com.toptal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.PrivilegeRepository;
import com.toptal.model.Privilege;

/** UNUSED AS ORGANIZATION LEVEL AUTHORIZATION  IS BEING USED **/
//@RestController
public class PrivilegeController {

	//@Autowired
	PrivilegeRepository repo;
	/*
    @PostMapping(path="/privilege", consumes={"application/json"})
    @ResponseBody
	public Privilege addPrivilege(@RequestBody Privilege priv) {
		repo.save(priv);
		return priv;
	}
	*/
}
