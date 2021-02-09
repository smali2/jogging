// Bismillah Hirrahman Nirrahim

package com.toptal;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.toptal.report.Report;

@RepositoryRestResource(/*base URL*/ collectionResourceRel="report",path="report")
public interface ReportRepository extends JpaRepository<Report, Long> {

	Optional<Report> findByWeeknumber(Integer week);
	
	@Query(value = "SELECT * FROM Report WHERE user_id = ?2 and week_number = ?1", nativeQuery = true)
	Optional<Report> findOptionalByWeeknumberAndUserId(Integer week, Long Id);
	
	//void addJogToReport(@RequestBody Jog jog);
	//void updateJogInReport(@RequestBody Jog jog, Jog prevJog);
	//void deleteJogInReport(@RequestBody Jog jog);
	
	Page<Report> findAll(Pageable p);
	
	@Query(value = "SELECT * FROM Report WHERE user_id = :id", nativeQuery = true)
	Page<Report> findAllByUser(Long id, Pageable p);
}
