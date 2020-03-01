package com.Sagacious_.KitpvpStats.command;

import java.text.DecimalFormat;
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
	private DecimalFormat df = new DecimalFormat("##0.0");
	private String getKDR(UserData p) {
		if(p.getKills()==0&&p.getDeaths()==0) {return "0.0";}
		if(p.getKills()>p.getDeaths()&&p.getDeaths()==0) {return p.getKills()+".0";}
		if(p.getKills()==p.getDeaths()&&p.getKills()==0) {return "0.0";}
		return df.format(Double.valueOf((double)p.getKills()/(double)p.getDeaths()));
	}
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
				if(arg3[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("pvpstats.reload")) {
						sender.sendMessage("§aReloading plugin...");
						Core.getInstance().onDisable();
						Core.getInstance().onEnable();
						sender.sendMessage("§aReloaded plugin!");
						return true;
					}
				}
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
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", pl).replace("%kills%", ""+data.getKills()).replace("%deaths%", ""+data.getDeaths()).replace("%killstreak%", ""+data.getKillstreak()).replace("%kdr%", getKDR(data))
						.replace("%top_killstreak%", ""+data.getTopKillstreak()).replace("%level%", Core.getInstance().getLevel(data.getKills())).replace("%level_progress%", Core.getInstance().getLevelProgress(data.getKills()))
								.replace("%level_progress_percent%", Core.getInstance().getLevelProgressPercent(data.getKills())).replace("%kills_tonextlevel%", ""+Core.getInstance().getKillsToNextLevel(data.getKills()))));
			}
			return true;
		}
		return false;
	}

}
