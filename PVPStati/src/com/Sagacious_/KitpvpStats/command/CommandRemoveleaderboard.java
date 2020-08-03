package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class CommandRemoveleaderboard implements CommandExecutor{
	
	public CommandRemoveleaderboard() {
		Core.getInstance().getCommand("removeleaderboard").setExecutor(this);
		Core.getInstance().getCommand("removeleaderboard").setAliases(new ArrayList<String>(Arrays.asList("removelb")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(!p.hasPermission("pvpstats.removeleaderboard")) {
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
				Core.getInstance().h.lke_l=new Location(Core.getInstance().h.lke_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().h.deathsHologram;
				Core.getInstance().h.lde_l=new Location(Core.getInstance().h.lde_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().h.killstreakHologram;
				Core.getInstance().h.lkie_l=new Location(Core.getInstance().h.lkie_l.getWorld(), 0, 5, 0);
			}
			if(s==null&&!f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak>");
				return true;
			}
			if(s.isDeleted()) {
				p.sendMessage("§c§lPVPStats §8| §rThat leaderboard is already deleted!");
				return true;
			}
			s.delete();
			p.sendMessage("§c§lPVPStats §8| §rRemoved leaderboard!");
			return true;
			}
		
		return false;
	}
}
