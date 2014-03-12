package com.nqeron.eventplugin;

import java.util.Date;

import org.bukkit.Location;

public class Event {
	private String name;
	private Location loc;
	private Date date;
	
	public Event(String name) {
		this.name = name;
	}

	public void setLocation(Location l) {
		this.loc = l;
		
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public Location getLocation(){
		return loc;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate(){
		return date;
	}

}
