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
		SYNTAX_ERROR = colorize(conf.getString("syntax-error"));
		RESET_NONE = colorize(conf.getString("reset-none"));
		RESET = colorize(conf.getString("reset"));
	}
	
	private static String colorize(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
