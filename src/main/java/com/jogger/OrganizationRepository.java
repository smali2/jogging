//Bismillah Hirrahman Nirrahim


package com.jogger;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jogger.model.Organization;
import com.jogger.model.Privilege;
import com.jogger.model.Role;

@RepositoryRestResource(/*base URL*/ collectionResourceRel="organizations",path="organizations")
public interface OrganizationRepository extends JpaRepository<Organization, Long>{

	//Page<Organization> findByName(Role org, Pageable p);
	Organization findByName(Role org);
	
	//Page<Organization> findById(Long id, Pageable p);
	Optional<Organization> findById(Long id);
	
	void deleteById(Long id);
	
	
}
