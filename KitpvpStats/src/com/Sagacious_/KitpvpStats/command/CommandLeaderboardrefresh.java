package com.Sagacious_.KitpvpStats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Sagacious_.KitpvpStats.Core;

public class CommandLeaderboardrefresh implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg0.hasPermission("kitstats.leaderboardrefresh")) {
			return false;
		}
		Core.getInstance().lh.refreshDeaths();
		Core.getInstance().lh.refreshKills();
		Core.getInstance().lh.refreshKillstreak();
		arg0.sendMessage("§cManually refreshed scoreboards");
		return true;
	}

}
