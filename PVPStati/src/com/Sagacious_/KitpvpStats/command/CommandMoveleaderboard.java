package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class CommandMoveleaderboard implements CommandExecutor{
	
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
			com.gmail.filoghost.holographicdisplays.api.Hologram s = null;
			boolean f = false;
			if(args[0].equalsIgnoreCase("kills")) {
				s = Core.getInstance().h.killHologram;
				if(s==null||s.getX()==0&&s.getY()==5&&s.getZ()==0) {
					Core.getInstance().h.setupNew("lke", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().h.deathsHologram;
				if(s==null||s.getX()==0&&s.getY()==5&&s.getZ()==0) {
					Core.getInstance().h.setupNew("lde", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().h.killstreakHologram;
				if(s==null||s.getX()==0&&s.getY()==5&&s.getZ()==0) {
					Core.getInstance().h.setupNew("lkie", p.getLocation());
					f=true;
				}
			}
			if(s==null&&!f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}
			Core.getInstance().h.teleport(args[0], s, p.getLocation());
			p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
			return true;
			}
		
		return false;
	}

}
