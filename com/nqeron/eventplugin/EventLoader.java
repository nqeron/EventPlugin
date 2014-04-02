package com.nqeron.eventplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.sound.sampled.Line;

/* num_events
 * event_name date-time x y z world_id
 * 	notifications
 * 	message
 * 	
 * 
 * 
 */

public class EventLoader {
	
	File file;
	Plugin plugin;
	
	public EventLoader(File file, Plugin plugin){
		this.file = file;
		this.plugin = plugin;
	}
	
	public EventLoader(Plugin plugin){
		this(new File("events.yml"), plugin);
	}
	
	public ArrayList<Event> loadEvents() throws IOException{
		BufferedReader read = new BufferedReader(new FileReader(file));
		ArrayList<Event> events = new ArrayList<Event>();
		
		String header = read.readLine();
		if (header == null || header.trim().equals("") || header.trim().equals("0")){ read.close(); return null;}
		
		int numEvents = Integer.parseInt(header);
		for(int i = 1; i <= numEvents; i++){
			String[] eventDef = new String[3];
			
			for(int j = 0; j < eventDef.length; j++){
				eventDef[j] = read.readLine();
			}
			
			//TODO -- validate lines?
			
			Event e = parseEvent(eventDef);
			if(e != null){
				events.add(e);
			}
		}
		
		read.close();
		return events;
	}
	
	public void saveEvents(Collection<Event> collection) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write(collection.size() + "\n"); //header
		StringBuilder str = new StringBuilder();
		for(Event e : collection){
			
			
			str.append(e.getName() +" ,");
			
			Date date = e.getDate();
			if (date != null){
				str.append(date.getTime() + ",");
			}else{
				str.append(" ,");
			}
			
			Location l = e.getLocation();
			if(l !=null){
				str.append(l.getX() + " ,");
				str.append(l.getY() + " ,");
				str.append(l.getZ() + " ,");
				str.append(l.getWorld().getName() + " ,");
			}else{
				str.append(" , , , ,");
			}
			
			writer.write(str.toString());
			str = null;
			
			writer.write("\n");
			
			writer.write("\t");
			//NOTIFICATIONS
			if(e.getNotifications() != null){
				Iterator<Date> i = e.getNotifications().iterator();
				Date alert;
				while(i.hasNext()){
					 alert = i.next();
					 writer.write(alert.getTime() + "");
					 writer.write(",");
				}
				alert = null;
			}
			
			writer.write("\n");
			writer.write("\t");
			//MESSAGE
			writer.write(e.getMessage());
			
			
			writer.write("\n");
		}
		
		writer.close();
	}
	
	private Event parseEvent(String[] eventDef) {
		String[] pieces = eventDef[0].split(",");
		
		Event e = new Event(pieces[0].trim());
		
		//parse date
		Date date = new Date();
		try {
			Long time = Long.parseLong(pieces[1]);
			date.setTime(time);
		} catch (NumberFormatException e1) {
			date = null;
		}
		e.setDate(date);
		
		//parse location
		double x,y,z;
		boolean locNull = false;
		x = y = z = 0;
		try{
			x = Double.parseDouble(pieces[2]);
			y = Double.parseDouble(pieces[3]);
			z = Double.parseDouble(pieces[4]);
		}catch (NumberFormatException e1){
			locNull = true;
		}
		
		
		World world = plugin.getServer().getWorld(pieces[5]);
		
		if (locNull || world == null){
			e.setLocation(null);
		}else{
			e.setLocation(new Location(world,x,y,z));
		}
		
		//Notifications
		String[] alerts = eventDef[1].trim().split(",");
		for(String str : alerts){
			try{
				Long time = Long.parseLong(str);
				Date alertDate = new Date(time);
				
				e.addNotification(alertDate);
			}catch(NumberFormatException err){
				continue;  //TODO -- capture all errors?
			}
		}
		
		//Message
		String message = eventDef[2].trim();
		e.setMessage(message);
		return e;
	}

}
