package com.Sagacious_.KitpvpStats.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class KillstreakHandler {
	
	public HashMap<Integer, HashMap<List<String>, List<String>>> killstreaks = new HashMap<Integer, HashMap<List<String>,List<String>>>();
	private File dataFolder = new File(Core.getInstance().getDataFolder(), "killstreak");
	
	public KillstreakHandler() {
		 if(!dataFolder.exists()) {
			 dataFolder.mkdir();
			 File f = new File(dataFolder, "example.yml");
			 if(!f.exists()) {
				 try (InputStream in = Core.getInstance().getResource("example.yml")) {
	                  Files.copy(in, f.toPath());
	              } catch (IOException e) {
	                  e.printStackTrace();
	              }
			 }
		 }
		 for(File f : dataFolder.listFiles()) {
         FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
         int ss = conf.getInt("perform-at");
         HashMap<List<String>, List<String>> s = new HashMap<List<String>, List<String>>();
         s.put(conf.getStringList("perform"), conf.getStringList("end-reward"));
         killstreaks.put(ss, s);
		 }
	}
	
	private int getLastSurpassedKillStreak(int streak) {
		Set<Integer> s = killstreaks.keySet();
		int selected = -1;
		for(Integer f : s) {
			if(f<=streak&&f>selected) {
				selected=f;
			}
		}
		return selected;
	}
	
	public void reward(Player p, int streak) {
		if(killstreaks.containsKey(streak)) {
			for(List<String> f : killstreaks.get(streak).keySet()) {
				for(String s : f) {
					if(s.startsWith("cmd:")) {
						   s = s.substring(4);
						   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName())));
						}else if(s.startsWith("chat:")){
						   s = s.substring(5);
						   p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%player%", p.getName())));
						}else if(s.startsWith("bc:")) {
							s = s.substring(3);
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%player%", p.getName())));
						}
				}
			}
		}
	}
	
	public void endkillstreak(Player killer, Player p, int streak) {
		boolean b = false;
		if(getLastSurpassedKillStreak(streak)>0) {
			for(Entry<Integer, HashMap<List<String>, List<String>>> f : killstreaks.entrySet()) {
				if(f.getKey()==getLastSurpassedKillStreak(streak)) {
				for(List<String> z : f.getValue().values()) {
					if(!b) {
						b=true;
					for(String s : z) {
						if(s.startsWith("cmd:")) {
							   s = s.substring(4);
							   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							}else if(s.startsWith("chat:")){
							   s = s.substring(5);
							   killer.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							}else if(s.startsWith("bc:")) {
								s = s.substring(3);
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							}
					}
					}
					}
				}
			}
		}
	}

}

