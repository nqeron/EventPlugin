package com.nqeron.eventplugin;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
		List<String> events = plugin.getEventNames();
		if (events.size() == 0){
			sender.sendMessage("No events created!");
			return true;
		}
		
		StringBuilder str = new StringBuilder();
		for(String event : events){
			str.append(event);
			if (!events.get(events.size()-1).equals(event)){ str.append(", "); }
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
		
		DateFormat dateParser = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat timeParser = DateFormat.getTimeInstance(DateFormat.SHORT);
		Date date;
		try {
			 date = dateParser.parse(args[2]);
			 String timeString = args[3];
			 if(args.length == 5){ timeString += " " + args[4]; }
			 else{ timeString += " am";}
			 date.setTime( timeParser.parse(timeString).getTime() );
			 
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			sender.sendMessage("Could not parse date/time. The proper format is:");
			date = new Date();
			sender.sendMessage("Date: " + dateParser.format(date));
			sender.sendMessage("Time: " + timeParser.format(date));
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
