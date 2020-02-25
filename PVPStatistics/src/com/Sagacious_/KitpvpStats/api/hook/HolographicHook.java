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
			for(int i = 0; i < Core.getInstance().lh.killTop.size(); i++) {
				int curr = Core.getInstance().lh.killTop.get(i);
				if(temp.size() < 10) {
				List<UserData> temp2 = Core.getInstance().dh.getKills(curr);
				for(UserData t : temp2) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killHologram.getLine(i).removeLine();
				killHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==1) {
			if(deathsHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.deathTop.size(); i++) {
				int curr = Core.getInstance().lh.deathTop.get(i);
				if(temp.size() < 10) {
				List<UserData> temp2 = Core.getInstance().dh.getDeaths(curr);
				for(UserData t : temp2) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				deathsHologram.getLine(i).removeLine();
				deathsHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==2) {
			if(killstreakHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.killstreakTop.size(); i++) {
				int curr = Core.getInstance().lh.killstreakTop.get(i);
				if(temp.size() < 10) {
				List<UserData> temp2 = Core.getInstance().dh.getKillstreak(curr);
				for(UserData t : temp2) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killstreakHologram.getLine(i).removeLine();
				killstreakHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKillstreak()));
				}
			}
			temp.clear();
			}
		}
	}
	
	public void killAll() {
		if(killHologram!=null) {killHologram.delete();}
		if(deathsHologram!=null) {deathsHologram.delete();}
		if(killstreakHologram!=null) {killstreakHologram.delete();}
	}
}