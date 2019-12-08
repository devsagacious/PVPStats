package com.Sagacious_.KitpvpStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Sagacious_.KitpvpStats.Updater.Result;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholderAPIHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholdersHook;
import com.Sagacious_.KitpvpStats.api.hook.VaultHook;
import com.Sagacious_.KitpvpStats.command.CommandAdminreset;
import com.Sagacious_.KitpvpStats.command.CommandLeaderboardrefresh;
import com.Sagacious_.KitpvpStats.command.CommandMoveleaderboard;
import com.Sagacious_.KitpvpStats.command.CommandStats;
import com.Sagacious_.KitpvpStats.command.CommandStatsreset;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.handler.ActivityHandler;
import com.Sagacious_.KitpvpStats.handler.AntistatsHandler;
import com.Sagacious_.KitpvpStats.handler.KillstreakHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;
import com.Sagacious_.KitpvpStats.util.FileUtil;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public DataHandler dh;
	public LeaderboardHandler lh;
	public KillstreakHandler kh;
	
	public PlaceholderAPIHook pa = null;
	public PlaceholdersHook ph = null;
	public String version = "1.1";
	private Updater update;
	
	@Override
	public void onEnable() {
		instance = this;
		setupConfig();
		if (!getConfig().getString("version").equals(version)) {
            getLogger().info("Your configuration file was not up to date. Updating it now...");
            new FileUtil().updateConfig();
            getLogger().info("Configuration file updated.");
        }
		if(!getConfig().getBoolean("auto-update")) {
			update = new Updater(this, 70578, getFile(), Updater.UpdateType.VERSION_CHECK, false);
		if(!update.getResult().equals(Result.UPDATE_FOUND)) {
			getLogger().info("###################");
			getLogger().info("New version of PVPStatistics available");
			getLogger().info("Current: " + getDescription().getVersion() + ", newest: " + update.getVersion());
			getLogger().info("###################");
		}
		}else {
			update = new Updater(this, 70578, getFile(), Updater.UpdateType.CHECK_DOWNLOAD, false);
			if(!update.getResult().equals(Result.SUCCESS)) {
				getLogger().info("###################");
				getLogger().info("Installed newest version of PVPStatistics");
				getLogger().info("###################");
			}
		}
		dh = new DataHandler();
	    new ActivityHandler(); new CommandStats(); new CommandMoveleaderboard();
	    new CommandAdminreset(); new CommandStatsreset();
		lh = new LeaderboardHandler();
		kh = new KillstreakHandler();
		new AntistatsHandler();
		getCommand("leaderboardrefresh").setExecutor(new CommandLeaderboardrefresh());
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			new VaultHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			ph = new PlaceholdersHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			pa = new PlaceholderAPIHook();pa.register();
		}
	}

	@Override
	public void onDisable() {
		for(UserData d : dh.data) {d.save();}
		lh.save();lh.killAll();
	}
	private int interval = getConfig().getInt("level-kills-interval");
	private List<String> ranks = getConfig().getStringList("levels");
	
	public String getLevel(int kills) {
	   int lvl = kills/interval;
	   if(lvl<ranks.size()) {
		   return ChatColor.translateAlternateColorCodes('&', ranks.get(lvl)).split(":")[0];
	   }
	   return ChatColor.translateAlternateColorCodes('&', ranks.get(ranks.size()-1)).split(":")[0];
	}
	public int getLevelInt(int kills) {
		  return kills/interval;
		}
	
	private File dataFolder;
	private File config;
	
	private void setupConfig() {
		dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		config = new File(dataFolder, "config.yml");
		if(!config.exists()) {
			try(InputStream in = Core.getInstance().getResource("config.yml")){
			    Files.copy(in, config.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
