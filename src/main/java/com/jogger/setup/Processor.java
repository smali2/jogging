// Bismillah Hirrahman Nirrahim

package com.jogger.setup;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jogger.JogRepository;
import com.jogger.ReportRepository;
import com.jogger.UserRepository;
import com.jogger.model.Jog;
import com.jogger.model.Role;
import com.jogger.model.User;
import com.jogger.report.Report;
import com.jogger.security.UserPrincipal;

@Service
public class Processor {
	
	@Autowired
	JogRepository jog_repo;
	
	@Autowired
	ReportRepository report_repo;
	
	@Autowired
	UserRepository user_repo;
	
	
	// Adds a new jog to the report table
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public void addJogToReport(@RequestBody Jog jog) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.ADMIN) || loggedInUser.getId()==jog.getUser().getId()) {
    		LocalDate date = LocalDate.parse(jog.getDate().toString());
    		int weekOfYear = date.get(WeekFields.of(new Locale("en")).weekOfYear());
    		
    		// find if current week already exists
    		Optional<Report> finder = report_repo.findOptionalByWeeknumberAndUserId(weekOfYear, jog.getUser().getId());
    		if (finder.isPresent()) {
    			Report entry = finder.get();
    			entry.setTotalJogs(entry.getTotalJogs()+1);
    			entry.setDistanceTravelled(entry.getDistanceTravelled()+jog.getDistance());
    			entry.setTotalMinutes(entry.getTotalMinutes()+jog.getMinutes());
    			entry.setAverageSpeed();
    			report_repo.save(entry);
    			return;
    		}
    		
    		Report entry = new Report(jog.getUser(), weekOfYear, (double) jog.getDistance(), 1, jog.getMinutes());
    		entry.setAverageSpeed();
    		report_repo.save(entry);
    	}
		
	}
	
	// Updates jog against the report table
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	//@PutMapping(path="/report", consumes={"application/json"})
	public void updateJogInReport(@RequestBody Jog jog, Jog prevJog) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.ADMIN) || loggedInUser.getId()==jog.getUser().getId()) {
    		LocalDate date = LocalDate.parse(jog.getDate().toString());
    		int weekOfYear = date.get(WeekFields.of(new Locale("en")).weekOfYear());
    		
    		// find if current week already exists
    		Optional<Report> finder = report_repo.findOptionalByWeeknumberAndUserId(weekOfYear, jog.getUser().getId());
    		if (finder.isPresent()) {
    			Report entry = finder.get();
    			// update distance if it is positive
    			if (jog.getDistance()>=0) entry.setDistanceTravelled(entry.getDistanceTravelled()-prevJog.getDistance()+jog.getDistance());
    			// update minutes if it is positive
    			if (jog.getMinutes()>=0) entry.setTotalMinutes(entry.getTotalMinutes()-prevJog.getMinutes()+jog.getMinutes());
    			entry.setAverageSpeed();
    			report_repo.save(entry);
    			return;
    		}
    		
    		Report entry = new Report(jog.getUser(), weekOfYear, (double) jog.getDistance(), 1, jog.getMinutes());
    		entry.setAverageSpeed();
    		report_repo.save(entry);
    	}
	}
	
	
	// Updates jog against the report table
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public void deleteJogInReport(Jog jog) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User loggedInUser = ((UserPrincipal)auth.getPrincipal()).getUser();
    	Role curOrgName = loggedInUser.getOrganization().getName();
    	
    	if (curOrgName.equals(Role.ADMIN) || loggedInUser.getId()==jog.getUser().getId()) {
    		LocalDate date = LocalDate.parse(jog.getDate().toString());
    		int weekOfYear = date.get(WeekFields.of(new Locale("en")).weekOfYear());
    		
    		// find if current week already exists
    		Optional<Report> finder = report_repo.findOptionalByWeeknumberAndUserId(weekOfYear, jog.getUser().getId());
    		if (finder.isPresent()) {
    			Report entry = finder.get();
    			entry.setDistanceTravelled(entry.getDistanceTravelled()-jog.getDistance());
    			entry.setTotalMinutes(entry.getTotalMinutes()-jog.getMinutes());
    			entry.setTotalJogs(entry.getTotalJogs()-1);
    			entry.setAverageSpeed();
    			report_repo.save(entry);
    			return;
    		}
    	}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
