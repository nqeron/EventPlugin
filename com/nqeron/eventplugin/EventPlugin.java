package com.nqeron.eventplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class EventPlugin extends JavaPlugin{
	HashMap<String,Event> events;
	EventLoader loader;
	
	GeneralCommands genExecutor;
	BukkitScheduler scheduler;
	
	public void onEnable() {
		events = new HashMap<String,Event>();
		genExecutor = new GeneralCommands();
		scheduler = getServer().getScheduler();
		
		loadEvents(); //read and setup events
		setupNotifications(); //set up notifications for loaded events
		
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
		clearNotifications(); //clean up notifications that haven't been set
		
		//clean-up
		loader = null;
		genExecutor = null;
		events = null;
		scheduler = null;
	}
	
	private void setupNotifications() {
		for (Event event : events.values()){
			for(Date alert: event.getNotifications()){
				Integer t = scheduleNotification(alert, event);
				event.addNotificationID(t);
			}
		}
	}
	
	private void clearNotifications() {
		// TODO Auto-generated method stub
		scheduler.cancelAllTasks();
		
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

	public Integer scheduleNotification(final Date date, final Event event) {
		Date now = new Date();
		long time = date.getTime() - now.getTime(); //calculate time as ms from now
		time /= 50; //convert milliseconds --> ticks
		
		BukkitTask t =scheduler.runTaskLater(this, new Runnable(){

			@Override
			public void run() {
				getServer().broadcastMessage(event.getMessage());
				event.removeNotification(date);
				
			}
			}, time);
		return t.getTaskId();
	}

	public void deleteEvent(Event e) {
		for(Integer alertID: e.getNotificationIDs()){
			scheduler.cancelTask(alertID);
		}
		events.remove(e.getName());
		e = null; //remove e from the game
	}
}
