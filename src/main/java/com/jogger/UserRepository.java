//Bismillah Hirrahman Nirrahim

package com.jogger;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jogger.model.User;

@RepositoryRestResource(/*base URL*/ collectionResourceRel="users",path="users")
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(@Param("username") String username);
	
	Page<User> findByUsername(String username, Pageable p);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User getUserByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.organization.name = 'USER'")
	Page<User> getAllRoleUser(Pageable p);
	
	
}
