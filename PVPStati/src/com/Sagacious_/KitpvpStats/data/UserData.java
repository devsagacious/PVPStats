package com.Sagacious_.KitpvpStats.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.Sagacious_.KitpvpStats.Core;

public class UserData {
	
	private UUID uuid;
	private String name;
	
	private int kills;
	private int deaths;
	private int killstreak;
	private int top_killstreak;
	private int resets;
	
	public UserData(UUID uuid, String name, int kills, int deaths, int killstreak, int top_killstreak, int resets) {
		this.uuid = uuid;
		this.name = name;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.top_killstreak = top_killstreak;
		this.resets = resets;
	}
	
	public void save() {
		File f = new File(Core.getInstance().getDataFolder(), "data/" + uuid.toString() + ".yml");
		if(!f.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.println("name: '" + name + "'");pw.println("kills: " + kills);
				pw.println("deaths: " + deaths);pw.println("killstreak: " + killstreak);
				pw.println("top_killstreak: " + top_killstreak);pw.println("resets: " + resets);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			conf.set("name", name);conf.set("kills", kills);
			conf.set("deaths", deaths);conf.set("killstreak", killstreak);
			conf.set("top_killstreak", top_killstreak);conf.set("resets", resets);
			try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public int getTopKillstreak() {
		return top_killstreak;
	}
	
	public int getResets() {
		return resets;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		if(killstreak>top_killstreak) {
			top_killstreak=killstreak;
		}
	}
	
	public void setTopKillstreak(int killstreak) {
		this.top_killstreak = killstreak;
	}
	
	public void setResets(int resets) {
		this.resets = resets;
	}
	
	public void reset(boolean use) {
		this.resets=use?this.resets-1:this.resets;
		this.kills = 0;
		this.killstreak = 0;
		this.top_killstreak = 0;
		this.deaths = 0;
	}

}
