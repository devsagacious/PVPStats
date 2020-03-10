package com.Sagacious_.KitpvpStats.api.hook;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HolographicHook implements Listener{
	public Hologram killHologram = null;
	public Hologram deathsHologram = null;
	public Hologram killstreakHologram = null;
	
	private String format;
	private Location lke_l;
	private Location lde_l;
	private Location lkie_l;
	
	private boolean lke = false;
	private boolean lde = false;
	private boolean lkie = false;
	
	public HolographicHook(){
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = Core.getInstance().getConfig();
		lke = conf.getBoolean("leaderboard-kills-enabled");
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
        if(lke) {
        	String[] l = conf.getString("leaderboard-kills-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lke_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header")));
			killHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer")));
        }
        if(lde) {
        	String[] l = conf.getString("leaderboard-deaths-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lde_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header")));
			deathsHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer")));
        }
        if(lkie) {
        	String[] l = conf.getString("leaderboard-killstreak-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lkie_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header")));
			killstreakHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer")));
        }
	}

	@EventHandler
	public void onUpdate(LeaderboardUpdateEvent e) {
		List<UserData> temp = new ArrayList<UserData>();
		if(e.getLeaderboard()==0) {
			if(killHologram!=null) {
				temp = Core.getInstance().lh.killTop;
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killHologram.getLine(i).removeLine();
				killHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
				}
			}
			}
		}else if(e.getLeaderboard()==1) {
			if(deathsHologram!=null) {
				temp = Core.getInstance().lh.deathTop;
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				deathsHologram.getLine(i).removeLine();
				deathsHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
				}
			}
			}
		}else if(e.getLeaderboard()==2) {
			if(killstreakHologram!=null) {
				temp = Core.getInstance().lh.killstreakTop;
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killstreakHologram.getLine(i).removeLine();
				killstreakHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKillstreak()));
				}
			}
			}
		}
	}
	
	public void killAll() {
		if(killHologram!=null) {killHologram.delete();}
		if(deathsHologram!=null) {deathsHologram.delete();}
		if(killstreakHologram!=null) {killstreakHologram.delete();}
	}
}
