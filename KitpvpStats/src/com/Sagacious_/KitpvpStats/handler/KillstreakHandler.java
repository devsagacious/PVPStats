package com.Sagacious_.KitpvpStats.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class KillstreakHandler {
	
	public HashMap<Integer, List<String>> killstreaks = new HashMap<Integer, List<String>>();
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
         killstreaks.put(ss, conf.getStringList("perform"));
		 }
	}
	
	public void reward(Player p, int streak) {
		if(killstreaks.containsKey(streak)) {
			for(int i = 0; i < killstreaks.get(streak).size(); i++) {
				String s = killstreaks.get(streak).get(i);
				if(s.startsWith("cmd:")) {
				   s = s.substring(4);
				   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replaceAll("%player%", p.getName())));
				}else if(s.startsWith("chat:")){
				   s = s.substring(5);
				   p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%player%", p.getName())));
				}
			}
		}
	}

}

