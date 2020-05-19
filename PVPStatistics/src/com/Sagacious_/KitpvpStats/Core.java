package com.Sagacious_.KitpvpStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Sagacious_.KitpvpStats.Updater.Result;
import com.Sagacious_.KitpvpStats.api.hook.HolographicHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholderAPIHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholdersHook;
import com.Sagacious_.KitpvpStats.api.hook.VaultHook;
import com.Sagacious_.KitpvpStats.api.hook.WorldguardHook;
import com.Sagacious_.KitpvpStats.command.CommandAddstats;
import com.Sagacious_.KitpvpStats.command.CommandAdminreset;
import com.Sagacious_.KitpvpStats.command.CommandLeaderboardrefresh;
import com.Sagacious_.KitpvpStats.command.CommandMoveleaderboard;
import com.Sagacious_.KitpvpStats.command.CommandRemovestats;
import com.Sagacious_.KitpvpStats.command.CommandSetstats;
import com.Sagacious_.KitpvpStats.command.CommandStats;
import com.Sagacious_.KitpvpStats.command.CommandStatsreset;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.handler.ActivityHandler;
import com.Sagacious_.KitpvpStats.handler.AntistatsHandler;
import com.Sagacious_.KitpvpStats.handler.KillstreakHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler.Hologram;
import com.Sagacious_.KitpvpStats.metrics.Metrics;
import com.Sagacious_.KitpvpStats.util.FileUtil;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public DataHandler dh;
	public LeaderboardHandler lh;
	public KillstreakHandler kh;
	public boolean useHolographic = false;
	
	public PlaceholderAPIHook pa = null;
	public PlaceholdersHook ph = null;
	public WorldguardHook wh = null;
	public String version = "1.6";
	private Updater update;
	
	private String level_progress_identifier = "|";
	private int level_progress_blocks = 10;
	private String level_progress_color = "§c";
	private String level_progress_noncolor = "§7";
	public HolographicHook h = null;
	
	@Override
	public void onEnable() {
		instance = this;
		new Metrics(this, 7500);
		setupConfig();
		if (!getConfig().getString("version").equals(version)) {
            getLogger().info("Your configuration file was not up to date. Updating it now...");
            new FileUtil().updateConfig();
            getLogger().info("Configuration file updated.");
        }
		if(!getConfig().getBoolean("auto-update")) {
			update = new Updater(this, 70578, this.getFile(), Updater.UpdateType.VERSION_CHECK, false);
		if(update.getResult().equals(Result.UPDATE_FOUND)) {
			getLogger().info("###################");
			getLogger().info("New version of PVPStatistics available");
			getLogger().info("Current: " + getDescription().getVersion() + ", newest: " + update.getVersion());
			getLogger().info("###################");
		}
		}else {
			update = new Updater(this, 70578, this.getFile(), Updater.UpdateType.CHECK_DOWNLOAD, false);
			if(update.getResult().equals(Result.SUCCESS)) {
				getLogger().info("###################");
				getLogger().info("Installed newest version of PVPStatistics");
				getLogger().info("###################");
			}
		}
		if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			useHolographic = getConfig().getBoolean("use-holographicdisplays");
			if(useHolographic) {
			     h = new HolographicHook();
			}
		}
		level_progress_identifier = getConfig().getString("level-progress-identifier");
		level_progress_blocks = getConfig().getInt("level-progress-blocks");
		level_progress_color = ChatColor.translateAlternateColorCodes('&', getConfig().getString("level-progress-color"));
		level_progress_noncolor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("level-progress-noncolor"));
		wh = new WorldguardHook();
		dh = new DataHandler();
	    new ActivityHandler(); new CommandStats(); new CommandMoveleaderboard();
	    new CommandAdminreset(); new CommandStatsreset(); new CommandAddstats();
	    new CommandSetstats(); new CommandRemovestats();
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
		List<String> ids = new ArrayList<String>();
		for(Hologram h : lh.kill_hologram) {
			ids.add(""+h.getStand().getEntityId());
		}
		for(Hologram h : lh.deaths_hologram) {
			ids.add(""+h.getStand().getEntityId());
		}
		for(Hologram h : lh.killstreak_hologram) {
			ids.add(""+h.getStand().getEntityId());
		}
		for(UserData d : dh.data) {d.save();}
		lh.save();lh.killAll();
		if(h!=null) {
			h.killAll();
		}
	}
	private int interval = getConfig().getInt("level-kills-interval");
	private List<String> ranks = getConfig().getStringList("levels");
	
	public String getLevel(int kills) {
	   int lvl = 0;
	   if(kills>0) {lvl=kills/interval;}
	   if(lvl<ranks.size()) {
		   return ChatColor.translateAlternateColorCodes('&', ranks.get(lvl)).split(":")[0];
	   }
	   return ChatColor.translateAlternateColorCodes('&', ranks.get(ranks.size()-1)).split(":")[0];
	}
	public int getLevelInt(int kills) {
		if(kills==0) {return 0;}
		  return (int)(kills/interval);
		}
	
	public int getKillsToNextLevel(int kills) {
		return (interval*(getLevelInt(kills))+interval)-kills;
	}
	
	public String getLevelProgress(int kills) {
		double percent = Double.valueOf(getLevelProgressPercent(kills));
		String returned = "";
		for(int i = 0; i < level_progress_blocks; i++) {
			boolean color = false;
			if((double)(i+1)/(double)level_progress_blocks*(double)100<=percent) {
				color = true;
			}
			returned=returned+(color?level_progress_color:level_progress_noncolor)+level_progress_identifier;
		}
		return returned;
	}
	
	private DecimalFormat df = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.ENGLISH));
	public String getLevelProgressPercent(int kills) {
		if(getKillsToNextLevel(kills)==interval) {return "0.0";}
		double percent = 0.0;
		int fs = interval-getKillsToNextLevel(kills);
		if(fs>0) {
			percent = (double)fs/(double)interval*(double)100;
		}
		double f = Double.valueOf(String.valueOf(percent).replace(",", "."));
		return df.format(f);
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
