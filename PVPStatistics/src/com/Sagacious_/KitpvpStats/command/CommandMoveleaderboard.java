package com.Sagacious_.KitpvpStats.command;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler.Hologram;

public class CommandMoveleaderboard implements CommandExecutor{
	private DecimalFormat df = new DecimalFormat("####0.0##############");
	
	public CommandMoveleaderboard() {
		Core.getInstance().getCommand("moveleaderboard").setExecutor(this);
		Core.getInstance().getCommand("moveleaderboard").setAliases(new ArrayList<String>(Arrays.asList("movelb")));
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player) {
			Player p = (Player)cs;
			if(!p.hasPermission("pvpstats.moveleaderboard")) {
				return false;
			}
			if(args.length == 0) {
				p.sendMessage("�c�lPVPStats �8| �rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}
			List<Hologram> toMove = null;
			boolean f = false;
			boolean k=false;
			boolean d=false;
			boolean ki=false;
			if(args[0].equalsIgnoreCase("kills")) {
				toMove = Core.getInstance().lh.kill_hologram;f=true;k=true;
			}else if(args[0].equalsIgnoreCase("deaths")) {
				toMove = Core.getInstance().lh.deaths_hologram;f=true;d=true;
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				toMove = Core.getInstance().lh.killstreak_hologram;f=true;ki=true;
			}
			if(toMove == null || !f) {
				p.sendMessage("�c�lPVPStats �8| �rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}else {
				if(f&&toMove.isEmpty()) {
					Core.getInstance().lh.setupLeaderboard(k, d, ki, p.getLocation());
				}else {
				Location tp = new Location(p.getWorld(), Double.valueOf(df.format(p.getLocation().getX()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getY()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getZ()).replaceAll(",", ".")));
				toMove.get(0).teleport(tp);
				for(int i = 1; i < toMove.size(); i++) {
					tp.setY(tp.getY()-0.3D);
					toMove.get(i).teleport(tp);
				}
				p.sendMessage("�c�lPVPStats �8| �rMoved leaderboard to your location!");
				return true;
			}
			}
		}
		return false;
	}

}
