package com.nqeron.eventplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EventPlugin extends JavaPlugin{
	HashMap<String,Event> events = new HashMap<String,Event>();
	
	
	public void onEnable() {
		//read and setup config
		//read and setup events
		getLogger().info("Event Plugin enabled");
		getServer().getPluginManager().registerEvents(new EventsListeners(), this);
		
		getCommand("event").setExecutor(new EventCommands(this));
		getCommand("event").setTabCompleter(new EventTabCompleter(this));
	}
	public void onDisable(){
		//save config changes
		//save event changes
		//clean-up
	}
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		return false;
		//return executor.onCommand(sender, command, label, args);
	}
	public boolean addEvent(String name) {
		if(events.containsKey(name)){
			return false;
		}
		
		Event e = new Event(name);
		events.put(name, e);
		
		return true;
		
	}
	public Event getEvent(String string) {
		return events.get(string);
		
	}
	public List<String> getEventNames() {
		// TODO Auto-generated method stub
		List<String> names = new ArrayList<String>();
		for(Event e: events.values()){
			names.add(e.getName());
		}
		return names;
	}
}
