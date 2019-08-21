package com.Sagacious_.KitpvpStats.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class DataHandler {

	public List<UserData> data = new ArrayList<UserData>();
	
	public DataHandler() {
		File dir = new File(Core.getInstance().getDataFolder(), "data");
		if(!dir.exists()) {
			dir.mkdir();
		}
		for(File f : dir.listFiles()) {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			int resets = 0;
			if(conf.contains("resets")) {
				resets=conf.getInt("resets");
			}
			UserData d = new UserData(UUID.fromString(f.getName().replaceAll(".yml", "")), conf.getString("name"), conf.getInt("kills"), conf.getInt("deaths"), conf.getInt("killstreak"), conf.getInt("top_killstreak"), resets);
		    data.add(d);
		}
	}
	
	public UserData getData(Player p) {
		for(UserData d : data) {
			if(d.getUniqueId().equals(p.getUniqueId())) {
				return d;
			}
		}
		return null;
	}
	
	public UserData getData(String name) {
		List<UserData> poss = new ArrayList<UserData>();
		for(UserData d : data) {
			if(d.getName().equalsIgnoreCase(name)) {
				poss.add(d);
			}
		}
		if(poss.size()>1) {
			for(UserData d : poss) {
				if(d.getName().equals(name)) {
					return d;
				}
			}
		}
		return poss.get(0);
	}
	
	public List<UserData> getKills(int kills){
		List<UserData> t = new ArrayList<UserData>();
		for(UserData u : data) {
			if(u.getKills()==kills) {
				t.add(u);
			}
		}
		return t;
	}
	
	public List<UserData> getDeaths(int kills){
		List<UserData> t = new ArrayList<UserData>();
		for(UserData u : data) {
			if(u.getDeaths()==kills) {
				t.add(u);
			}
		}
		return t;
	}
	public List<UserData> getKillstreak(int kills){
		List<UserData> t = new ArrayList<UserData>();
		for(UserData u : data) {
			if(u.getTopKillstreak()==kills) {
				t.add(u);
			}
		}
		return t;
	}
}
