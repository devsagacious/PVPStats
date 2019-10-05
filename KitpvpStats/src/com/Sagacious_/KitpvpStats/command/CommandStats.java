package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandStats implements CommandExecutor{
	private List<String> stat;
	public CommandStats() {
		stat = Core.getInstance().getConfig().getStringList("stats-command");
		Core.getInstance().getCommand("stats").setExecutor(this);
		Core.getInstance().getCommand("stats").setAliases(new ArrayList<String>(Arrays.asList("statistics")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			UserData data = null;
			String pl = Core.getInstance().getConfig().getString("me");
			if(arg3.length > 0) {
				Player p = Bukkit.getPlayerExact(arg3[0]);
				if(p == null) {
					sender.sendMessage("§cUnknown player §4" + arg3[0]);
				return true;
				}
				data = Core.getInstance().dh.getData(p);
				pl = p.getName() + "'s";
			}else {
			 data = Core.getInstance().dh.getData((Player)sender);
			}
			for(String s : stat) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", pl).replace("%kills%", ""+data.getKills()).replace("%deaths%", ""+data.getDeaths()).replace("%killstreak%", ""+data.getKillstreak()).replace("%top_killstreak%", ""+data.getTopKillstreak()).replace("%level%", Core.getInstance().getLevel(data.getKills()))));
			}
			return true;
		}
		return false;
	}

}
