/**
 * Bismillah Hirrahman Nirrahim
 */
package com.toptal.model;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;


/**
 * @author Administrator
 *
 */
@Entity
public class Jog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String date;
	private String time;
	private float distance;
	private float minutes;
	private String location;
	private String weather;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return java.sql.Date.valueOf(this.date);
	}
	public void setDate(String date) {
		/*this.date = date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();;
			      */
		this.date = date;
	}
	public void setDate(Date date) {
		this.date = date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().toString();	
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String loc) {
		this.location = loc;
	}
	
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public float getMinutes() {
		return minutes;
	}
	public void setMinutes(float minutes) {
		this.minutes = minutes;
	}
	
	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}
	/**
	 * @param weather the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}
	@Override
	public String toString() {
		return "Jog [id=" + id + ", date=" + date + ", time=" + time + ", distance=" + distance + ", minutes=" + minutes
				+ ", location=" + location + ", weather=" + weather + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date, distance, id, location, minutes, time);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Jog))
			return false;
		Jog other = (Jog) obj;
		return Objects.equals(date, other.date)
				&& Float.floatToIntBits(distance) == Float.floatToIntBits(other.distance) && id == other.id
				&& Objects.equals(location, other.location)
				&& Float.floatToIntBits(minutes) == Float.floatToIntBits(other.minutes)
				&& Objects.equals(time, other.time);
	}
	
	
	
	
}
