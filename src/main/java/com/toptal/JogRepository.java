/**
 * Bismillah Hirrahman Nirrahim
 */
package com.toptal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.model.Jog;
import com.toptal.model.User;

/**
 * @author Administrator
 *
 */
@RepositoryRestResource(/*base URL*/ collectionResourceRel="jogs",path="jogs")
public interface JogRepository extends JpaRepository<Jog, Long>{
	
	
	Page<Jog> findByDate(String date, Pageable p);
	Page<Jog> findByDateGreaterThan(String date, Pageable p);
	Page<Jog> findByDateLessThan(String date, Pageable p);
	Page<Jog> findByDateNot(String date, Pageable p);
	
	Page<Jog> findByDistance(float dist, Pageable p);
	Page<Jog> findByDistanceGreaterThan(float dist, Pageable p);
	Page<Jog> findByDistanceLessThan(float dist, Pageable p);
	Page<Jog> findByDistanceNot(float dist, Pageable p);
	
	Page<Jog> findByTime(String time, Pageable p);
	Page<Jog> findByTimeGreaterThan(String time, Pageable p);
	Page<Jog> findByTimeLessThan(String time, Pageable p);
	Page<Jog> findByTimeNot(String time, Pageable p);
	
	Page<Jog> findByMinutes(float minutes, Pageable p);
	Page<Jog> findByMinutesGreaterThan(float minutes, Pageable p);
	Page<Jog> findByMinutesLessThan(float minutes, Pageable p);
	Page<Jog> findByMinutesNot(float minutes, Pageable p);
	
	Page<Jog> findByLocation(String location, Pageable p);
	Page<Jog> findByLocationNot(String location, Pageable p);
	
	@Query("SELECT u FROM Jog u WHERE u.user.id = :id")
	Page<Jog> getJogsByUserId(@Param("id") Long id, Pageable p);
	
	
}
