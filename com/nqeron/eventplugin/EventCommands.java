package com.nqeron.eventplugin;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommands implements CommandExecutor{
	
	EventPlugin plugin;
	public EventCommands(EventPlugin plugin){
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
		//we know that command is 'event', but check anyways
		if(!command.getName().equalsIgnoreCase("event")){ return false; }
		
		if (args.length < 1){return false;}
		
		String subCommand = args[0];

		switch(subCommand){
			case "create": return createEvent(sender,args);
			case "setLocation": return setLocation(sender,args);
			case "setTime": return setTime(sender,args);
			case "list": return listEvents(sender,args);
		}
		return false;
	}

	private boolean listEvents(CommandSender sender, String[] args) {
		Collection<Event> events = plugin.getEvents();
		if (events.size() == 0){
			sender.sendMessage("No events created!");
			return true;
		}
		
		boolean showDetails = false;
		if(args.length >= 2 && "-a".equals(args[1])){
			showDetails = true;
		}
		
		StringBuilder str = new StringBuilder();
		
		for(Event event : events){
			
			if(showDetails){
				str.append(event.toString());
			}else{
				str.append(event.getName());
			}
			
			//TODO get list events to stop delimiting after last event
			//if (!events.get(events.size()-1).equals(event)){
				
				if(showDetails){
					str.append("\n");
				}else{
					str.append(", ");
				}
			//}
		}
		sender.sendMessage(str.toString());
		return true;
	}

	private boolean setTime(CommandSender sender, String[] args) {
		
		String name = args[1];
		Event e = plugin.getEvent(name);
		if(e == null){
			sender.sendMessage(String.format("Could not find event named %s", name));
			return true;
		}
		
		if(args.length < 4 || args.length > 5){
			sender.sendMessage("Usage: /event setTime <event> <date> <time>");
			return true;
		}
		
		DateFormat datetimeParser = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		Date date;
		try {
			 String datetimeString = args[2] + " "+ args[3];
			 if(args.length == 5){ datetimeString += " " + args[4]; }
			 else{ datetimeString += " am";}
			 date =  datetimeParser.parse(datetimeString);
			 
		} catch (ParseException e1) {
			sender.sendMessage("Could not parse date/time. The proper format is:");
			date = new Date();
			sender.sendMessage(datetimeParser.format(date));
			return true;
		}
		
		e.setDate(date);	
		sender.sendMessage(String.format("set event %s to date: %s", e.getName(),date)) ;
		
		return true;
	}

	private boolean setLocation(CommandSender sender, String[] args) {
		if( (args.length < 2) || (args.length > 5) ){
			sender.sendMessage("Usage: /event setLocation <event> [x] [y] [z]");
			return true;
		}
		
		
		if (! (sender instanceof Player) ){
			sender.sendMessage("Usable only by players!");
			return true;
		}
		
		String name = args[1];
		Event e = plugin.getEvent(name);
		if(e == null){
			sender.sendMessage(String.format("Could not find event named %s", name));
			return true;
		}
		
		double x,y,z;
		Location l;
		Player p = (Player) sender; //already checked for this
		l = p.getLocation();
		if (args.length >= 3) {
			x = Double.parseDouble(args[2]);
			y = Double.parseDouble(args[3]);
			z = Double.parseDouble(args[4]);
			l = new Location(p.getWorld(), x, y, z);
		}
		
		e.setLocation(l);
		//to-do config notify? / permissions
		return true;
	}

	private boolean createEvent(CommandSender sender, String[] args) {
		if (args.length !=2){
			sender.sendMessage("Usage: /event create <name>");
			return true;
		}
		
		boolean success = plugin.addEvent(args[1]);
		
		if(!success){
			sender.sendMessage("An event with that name already exists!");
		}
		
		return true;
	}
	
	
	
	

}
