package com.Sagacious_.KitpvpStats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandSetstats implements CommandExecutor{
	
	public CommandSetstats() {
		Core.getInstance().getCommand("setstats").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!sender.hasPermission("pvpstats.setstats")) {
			return false;
		}
			if(arg3.length < 3) {
				sender.sendMessage("�a/setstats kills <player> <amount>");
				sender.sendMessage("�a/setstats deaths <player> <amount>");
				sender.sendMessage("�a/setstats killstreak <player> <amount>");
				sender.sendMessage("�a/setstats topkillstreak <player> <amount>");
				return true;
			}else {
				UserData d = Core.getInstance().dh.getData(arg3[1]);
				if(d==null) {
					sender.sendMessage("�cUnknown player �4" + arg3[1]);
					return true;
				}
				int amount = -1;
				try {
					amount = Integer.valueOf(arg3[2]);
				}catch(NumberFormatException e) {sender.sendMessage("�cPlease specify a correct number!"); return true;}
				if(arg3[0].equalsIgnoreCase("kills")) {d.setKills(amount);}
				if(arg3[0].equalsIgnoreCase("deaths")) {d.setDeaths(amount);}
				if(arg3[0].equalsIgnoreCase("killstreak")) {d.setKillstreak(amount);}
				if(arg3[0].equalsIgnoreCase("topkillstreak")) {d.setTopKillstreak(amount);}
				sender.sendMessage("�aUpdated �2" + arg3[1] + "�a's statistics!");
			}
		return true;
	}

}
