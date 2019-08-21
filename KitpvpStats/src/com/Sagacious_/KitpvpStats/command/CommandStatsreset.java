package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.Messages;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandStatsreset implements CommandExecutor{
	
    public CommandStatsreset() {
		Core.getInstance().getCommand("statsreset").setExecutor(this);
		Core.getInstance().getCommand("statsreset").setAliases(new ArrayList<String>(Arrays.asList("sr")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
		if(args.length<0) {
			sender.sendMessage(Messages.SYNTAX_ERROR.replaceAll("%command_usage%", "/" + label.toLowerCase()));
			return true;
		}
		UserData data = Core.getInstance().dh.getData(p);
		if(data.getResets()<1) {
			sender.sendMessage(Messages.RESET_NONE);
			return true;
		}
		data.reset(true);
		sender.sendMessage(Messages.RESET);
		return true;
		}
		return false;
	}

}
