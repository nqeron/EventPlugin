package com.nqeron.eventplugin;

import java.text.DateFormat;
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
	
	@Override
	public String toString() {
		DateFormat dFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		
		String dateString = "no date set";
		if (date != null){
			dateString = dFormat.format(date);
		}
		
		String locString = "no location set";
		if(loc != null){
			locString = loc.toString();
		}
		
	    return String.format("%s:  date: %s, location: %s", name, dateString, locString);
	}

}
