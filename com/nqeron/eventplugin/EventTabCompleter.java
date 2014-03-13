package com.nqeron.eventplugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class EventTabCompleter implements TabCompleter{
	
	private static final List<String> MAIN_CHOICES = ImmutableList.of("create", "setLocation","setTime","list");
	
	private EventPlugin plugin;
	
	
	public EventTabCompleter(EventPlugin plugin){
		super();
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,String alias, String[] args) {
		
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "args cannot be null");
		Validate.notNull(alias, "alias cannot be null");
		
		if (args.length == 1){
			return StringUtil.copyPartialMatches(args[0], MAIN_CHOICES, new ArrayList<String>());
		}
		
		String subCommand = args[0];
		
		if(subCommand.equalsIgnoreCase("create")){ return null; }
		
		if(subCommand.equalsIgnoreCase("setLocation") || 
		   subCommand.equalsIgnoreCase("setTime")
		   )
		{  //looking for events?
			if(args.length ==2){
				return StringUtil.copyPartialMatches(args[1], plugin.getEventNames(), new ArrayList<String>());
			}
		}
		
		return ImmutableList.of();
	}
}
