package com.nqeron.eventplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EventPlugin extends JavaPlugin{
	HashMap<String,Event> events = new HashMap<String,Event>();
	EventLoader loader;
	
	GeneralCommands genExecutor = new GeneralCommands();
	
	public void onEnable() {
		loadEvents(); //read and setup events
		
		//read and setup config
		
		getLogger().info("Event Plugin enabled");
		getServer().getPluginManager().registerEvents(new EventsListeners(), this);
		
		getCommand("event").setExecutor(new EventCommands(this));
		getCommand("event").setTabCompleter(new EventTabCompleter(this));
		
	}
	
	public void onDisable(){
		//save config changes
		//save event changes
		saveEvents();
		
		//clean-up
		loader = null;
		genExecutor = null;
		events = null;
	}
	
	private void saveEvents() {
		try {
			loader.saveEvents(events.values());
		} catch (IOException e) {
			getLogger().info("Unable to save events!");
		}
		
	}

	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		return genExecutor.onCommand(sender, command, label, args);
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
		List<String> names = new ArrayList<String>();
		for(Event e: events.values()){
			names.add(e.getName());
		}
		return names;
	}
	
	private void loadEvents() {
		File dFolder = getDataFolder();
		File loadFile = new File(dFolder.getPath() + File.pathSeparator + "events.db");
		loader = new EventLoader(loadFile, this);
		ArrayList<Event> loadedEvents;
		try {
			loadedEvents = loader.loadEvents();
		} catch (IOException e) {
			loadedEvents = null;
		}
		if(loadedEvents != null){
			for(Event e: loadedEvents){
				events.put(e.getName(), e);
			}
		}else{
			getLogger().info("Failed to load events from file");
		}
		loadedEvents = null;
	}

	public Collection<Event> getEvents() {
		return events.values();
	}
}
