package com.Sagacious_.KitpvpStats;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Sagacious_.KitpvpStats.api.hook.PlaceholderAPIHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholdersHook;
import com.Sagacious_.KitpvpStats.command.CommandLeaderboardrefresh;
import com.Sagacious_.KitpvpStats.command.CommandStats;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.handler.ActivityHandler;
import com.Sagacious_.KitpvpStats.handler.KillstreakHandler;
import com.Sagacious_.KitpvpStats.handler.PVPHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public DataHandler dh;
	public LeaderboardHandler lh;
	public KillstreakHandler kh;
	
	@Override
	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);saveDefaultConfig();
		dh = new DataHandler();
	    new ActivityHandler(); new CommandStats();
		lh = new LeaderboardHandler();
		kh = new KillstreakHandler();
		Bukkit.getPluginManager().registerEvents(new PVPHandler(), this);
		getCommand("leaderboardrefresh").setExecutor(new CommandLeaderboardrefresh());
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			new PlaceholdersHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PlaceholderAPIHook();
		}
	}

	@Override
	public void onDisable() {
		for(UserData d : dh.data) {d.save();}
		lh.killAll();
	}
	private int interval = getConfig().getInt("level-kills-interval");
	private List<String> ranks = getConfig().getStringList("levels");
	
	public String getLevel(int kills) {
	   int lvl = kills/interval;
	   if(lvl<ranks.size()) {
		   return ChatColor.translateAlternateColorCodes('&', ranks.get(lvl));
	   }
	   return ChatColor.translateAlternateColorCodes('&', ranks.get(ranks.size()-1));
	}
}
