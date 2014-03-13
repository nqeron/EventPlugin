package com.nqeron.eventplugin;

import java.text.DateFormat;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GeneralCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
		switch(command.getName().toLowerCase()){
		 case "now": return getNow(sender);
		}
		return false;
	}

	private boolean getNow(CommandSender sender) {
		Date date = new Date();
		DateFormat dFormat =  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		sender.sendMessage(dFormat.format(date));
		return true;
	}

}
