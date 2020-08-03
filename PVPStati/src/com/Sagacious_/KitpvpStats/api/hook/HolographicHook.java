package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
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
	public Location lke_l;
	public Location lde_l;
	public Location lkie_l;
	
	private boolean lke = false;
	private boolean lde = false;
	private boolean lkie = false;
	
	private void deleteIfExists(Location l) {
		if(l!=null) {
		for(Entity e : l.getWorld().getEntities()) {
			if(e.getLocation().equals(l)) {
				if(HologramsAPI.isHologramEntity(e)) {
					e.remove();
				}
			}
		}
		}
	}

	public void teleport(String f, Hologram h, Location l) {
		h.teleport(l);
		if(f.equals("kills")) {
			lke_l =l;
		}else if(f.equals("deaths")) {
			lde_l =l;
		}else if(f.equals("killstreak")) {
			lkie_l =l;
		}
	}
	
	public void setupNew(String holo, Location l) {
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(holo.equals("lke")) {
			lke_l=l;
			deleteIfExists(lke_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lke_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header")));
			killHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer")));
		}else if(holo.equals("lde")) {
			lde_l=l;
			deleteIfExists(lde_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lde_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header")));
			deathsHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer")));
		}else if(holo.equals("lkie")) {
			lkie_l=l;
			deleteIfExists(lkie_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lkie_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header")));
			killstreakHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer")));
		}
	}
	
	public HolographicHook(){
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		if(!f.exists()) {
			 try (InputStream in = Core.getInstance().getResource("leaderboard.yml")) {
                 Files.copy(in, f.toPath());
             } catch (IOException e) {
                 e.printStackTrace();
             }
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		lke = conf.getBoolean("leaderboard-kills-enabled");
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
        if(lke) {
        	String[] l = conf.getString("leaderboard-kills-location").split(",");
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lke", lke_l);
        }
        if(lde) {
        	String[] l = conf.getString("leaderboard-deaths-location").split(",");
		lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lde", lde_l);
        }
        if(lkie) {
        	String[] l = conf.getString("leaderboard-killstreak-location").split(",");
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lkie", lkie_l);
        }
	}
	

	@EventHandler
	public void onUpdate(LeaderboardUpdateEvent e) {
		List<UserData> temp = new ArrayList<UserData>();
		if(e.getLeaderboard()==0) {
			if(killHologram!=null) {
			temp.addAll(Core.getInstance().lh.killTop);
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
					if(killHologram.getLine(i)!=null) {killHologram.getLine(i).removeLine();}
				killHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==1) {
			if(deathsHologram!=null) {
				temp.addAll(Core.getInstance().lh.deathTop);
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				if(deathsHologram.getLine(i)!=null) {deathsHologram.getLine(i).removeLine();}
				deathsHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==2) {
			if(killstreakHologram!=null) {
				temp.addAll(Core.getInstance().lh.killstreakTop);
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
					if(killstreakHologram.getLine(i)!=null) {killstreakHologram.getLine(i).removeLine();}
				killstreakHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getTopKillstreak()));
				}
			}
			temp.clear();
			}
		}
	}
	
	public void killAll() {
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(lke_l!=null) {if(killHologram!=null) {killHologram.delete();}conf.set("leaderboard-kills-location", lke_l.getWorld().getName() + "," + lke_l.getX() + "," + (lke_l.getY()) + "," + lke_l.getZ());}
		if(lde_l!=null) {if(deathsHologram!=null) {deathsHologram.delete();}conf.set("leaderboard-deaths-location", lde_l.getWorld().getName() + "," + lde_l.getX() + "," + (lde_l.getY()) + "," + lde_l.getZ());}
		if(lkie_l!=null) {if(killstreakHologram!=null) {killstreakHologram.delete();}conf.set("leaderboard-killstreak-location", lkie_l.getWorld().getName() + "," + lkie_l.getX() + "," + (lkie_l.getY()) + "," + lkie_l.getZ());}
		try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
