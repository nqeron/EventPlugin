package com.nqeron.eventplugin;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;

public class Event {
	private String name;
	private String message;
	private Location loc;
	private Date date;
	
	private LinkedList<Date> notification;
	private ArrayList<Integer> ids;
	
	public Event(String name) {
		this.name = name;
		notification = new LinkedList<Date>();
		ids = new ArrayList<Integer>();
	}

	public void setLocation(Location l) {
		this.loc = l;
		
	}
	
	public void setMessage(String msg){
		message = msg;
	}
	
	public void addNotification(Date date){
		notification.add(date);
	}
	
	public List<Date> getNotifications(){
		return notification;
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
	
	public String getMessage() {
		return message;
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

	public boolean hasMessage() {
		return message != null || !message.isEmpty();
	}

	public void removeNotification(Date date) {
		notification.remove(date);
	}

	public List<Integer> getNotificationIDs() {
		return ids;
	}

	public void addNotificationID(Integer t) {
		ids.add(t);
	}

}
