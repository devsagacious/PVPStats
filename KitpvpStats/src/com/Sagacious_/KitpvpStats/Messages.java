package com.Sagacious_.KitpvpStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
	public static String SYNTAX_ERROR;
	public static String RESET_NONE = "";
	public static String RESET = "";
	public static String RESET_RECEIVED = "";
	
	public Messages() {
		File f = new File(Core.getInstance().getDataFolder(), "messages.yml");
		if(!f.exists()) {
			try (InputStream in = Core.getInstance().getResource("messages.yml")) {
                Files.copy(in, f.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!conf.isSet("reset-received")) {
			conf.set("reset-received", "&c&lPVPStatistics &8| &7You have received &4%amount% &7resets");
			try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SYNTAX_ERROR = colorize(conf.getString("syntax-error"));
		RESET_NONE = colorize(conf.getString("reset-none"));
		RESET = colorize(conf.getString("reset"));
		RESET_RECEIVED = colorize(conf.getString("reset-received"));
	}
	
	private static String colorize(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
