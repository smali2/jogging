/**
 * Bismillah Hirrahman Nirrahim
 */

package com.jogger.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jogger.model.User;

@Entity
public class Report {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	User user;
	
	@Column(name = "week_number")
	Integer weeknumber;
	Integer total_jogs;
	Float total_minutes;
	Double distance_travelled;
	
	@Column(name = "average_speed")
	Double average_speed;

	
	public Report() {
		super();
	}

	public Report(User user, Integer weekNumber, Double distanceTravelled, Integer totalJogs, Float totalMinutes) {
		super();
		this.user = user;
		this.weeknumber = weekNumber;
		this.distance_travelled = distanceTravelled;
		this.total_jogs = totalJogs;
		this.total_minutes = totalMinutes;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getTotalMinutes() {
		return total_minutes;
	}

	public void setTotalMinutes(Float totalMinutes) {
		this.total_minutes = totalMinutes;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getWeekNumber() {
		return weeknumber;
	}
	public void setWeekNumber(Integer weekNumber) {
		this.weeknumber = weekNumber;
	}
	public Double getDistanceTravelled() {
		return distance_travelled;
	}
	public void setDistanceTravelled(Double distanceTravelled) {
		this.distance_travelled = distanceTravelled;
	}
	
	public Integer getTotalJogs() {
		return total_jogs;
	}
	public void setTotalJogs(Integer totalJogs) {
		this.total_jogs = totalJogs;
	}
	
	public Double getAverageSpeed() {
		return average_speed;
	}

	public void setAverageSpeed() {
		this.average_speed = this.distance_travelled/this.total_minutes;
	}
	
	
}
