package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.Sagacious_.KitpvpStats.Core;

import net.milkbowl.vault.chat.Chat;

public class VaultHook implements Listener{
private Chat chat = null;
private boolean use = false;
private String format;

private boolean setupChat()
{
    RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
    if (chatProvider != null) {
        chat = chatProvider.getProvider();
}
    return (chat != null);
}

public VaultHook() {
	Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	setupChat();
	File f = new File(Core.getInstance().getDataFolder(), "config.yml");
	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		use = conf.getBoolean("use-format");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("format"));
}

@EventHandler
public void onFormat(AsyncPlayerChatEvent e) {
	if(use) {
		String f = format;
		if(Core.getInstance().ph!=null) {
			f=Core.getInstance().ph.format(e.getPlayer(), format);
		}
		if(Core.getInstance().pa!=null&&me.clip.placeholderapi.PlaceholderAPI.containsPlaceholders(format)) {
			f=me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), format);
		}
		if(chat!=null){f = f.replace("{prefix}", chat.getPlayerPrefix(e.getPlayer())).replace("{suffix}", chat.getPlayerSuffix(e.getPlayer()));}
		f=f.replace("{level}", Core.getInstance().getLevel(Core.getInstance().dh.getData(e.getPlayer()).getKills()))
				.replace("{player}", e.getPlayer().getName());
		f=f.replace("{message}", "%2$s");
		f=f.replace("%", "%%");
		e.setFormat(f);
	}
}
}
