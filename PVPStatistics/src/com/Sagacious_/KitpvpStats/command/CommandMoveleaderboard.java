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
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}
			if(!Core.getInstance().useHolographic) {
			String toMove = null;
			boolean f = false;
			boolean k=false;
			boolean d=false;
			boolean ki=false;
			if(args[0].equalsIgnoreCase("kills")) {
				toMove = args[0];f=true;k=true;
			}else if(args[0].equalsIgnoreCase("deaths")) {
				toMove = args[0];f=true;d=true;
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				toMove = args[0];f=true;ki=true;
			}
			if(!Core.getInstance().useHolographic) {
			if(toMove == null || !f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}else {
				List<Hologram> moving = toMove.equalsIgnoreCase("kills")?Core.getInstance().lh.kill_hologram:toMove.equalsIgnoreCase("kills")?Core.getInstance().lh.deaths_hologram:Core.getInstance().lh.killstreak_hologram;
				if(f&&toMove.isEmpty()) {
					Core.getInstance().lh.setupLeaderboard(k, d, ki, p.getLocation());
				}else {
				Location tp = new Location(p.getWorld(), Double.valueOf(df.format(p.getLocation().getX()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getY()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getZ()).replaceAll(",", ".")));
				moving.get(0).teleport(tp);
				for(int i = 1; i < moving.size(); i++) {
					tp.setY(tp.getY()-0.3D);
					moving.get(i).teleport(tp);
				}
				p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
				return true;
			}
			}
			}else {
				if(toMove.equalsIgnoreCase("kills")) {
					Core.getInstance().h.killHologram.teleport(p.getLocation());
				}
				if(toMove.equalsIgnoreCase("deaths")) {
					Core.getInstance().h.deathsHologram.teleport(p.getLocation());
				}
				if(toMove.equalsIgnoreCase("killstreak")) {
					Core.getInstance().h.killstreakHologram.teleport(p.getLocation());
				}
				p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
				return true;
			}
			}
			com.gmail.filoghost.holographicdisplays.api.Hologram s = null;
			if(args[0].equalsIgnoreCase("kills")) {
				s = Core.getInstance().h.killHologram;
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().h.deathsHologram;
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().h.killstreakHologram;
			}
			if(s==null) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr>");
				return true;
			}
			s.teleport(p.getLocation());
			p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
			return true;
			}
		
		return false;
	}

}
