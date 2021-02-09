// Bismillah Hirrahman Nirrahim

package com.toptal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toptal.model.Privilege;

/** UNUSED AS ORGANIZATION LEVEL AUTHORIZATION  IS BEING USED **/
//@RepositoryRestResource(/*base URL*/ collectionResourceRel="privileges",path="privileges")
public interface PrivilegeRepository {//extends JpaRepository<Privilege, Long> {
	
	//Page<Privilege> findByName(String privilege, Pageable p);
	Privilege findByName(String privilege);
	
	
	//@PostAuthorize("hasPermission(returnObject, 'read_privilege')")
    @GetMapping("/privileges/{id}")
    @ResponseBody
    public Privilege findById(@PathVariable long id);
	
	
	
}
