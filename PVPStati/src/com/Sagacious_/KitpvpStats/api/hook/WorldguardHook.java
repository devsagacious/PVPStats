package com.Sagacious_.KitpvpStats.api.hook;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldguardHook {
	private boolean enabled = false;
	private List<String> regions;
	
	public WorldguardHook() {
		if(Bukkit.getPluginManager().isPluginEnabled("WorldEdit") && Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			enabled = Core.getInstance().getConfig().getBoolean("worldguard-enabled");
			regions = Core.getInstance().getConfig().getStringList("worldguard-regions");
		}
	}
	
	private boolean isInRegion(String regionName, Player p) {
		ProtectedRegion r = com.sk89q.worldguard.bukkit.WGBukkit.getRegionManager(p.getWorld()).getRegion(regionName);
		return r.contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
	}
	
	private boolean isInRegion14(String regionName, Player p) {
		ProtectedRegion r = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().get(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(p.getWorld())).getRegion(regionName);
		return r.contains(com.sk89q.worldedit.bukkit.BukkitAdapter.asBlockVector(p.getLocation()));
	}
	
	public boolean countstats1_14(Player p) {
		if(!enabled) {return true;}
		if(enabled) {
				for(String d : regions) {
                	if(isInRegion14(d, p)) {
                		return true;
                	}
                }
		}
		return false;
	}
	
	public boolean countstats1_8(Player p) {
		if(!enabled) {return true;}
		if(enabled) {
                for(String d : regions) {
                	if(isInRegion(d, p)) {
                		return true;
                	}
             }
		}
		return false;
	}

}
