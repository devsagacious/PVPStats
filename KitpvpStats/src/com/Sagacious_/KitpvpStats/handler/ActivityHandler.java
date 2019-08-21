package com.Sagacious_.KitpvpStats.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class ActivityHandler implements Listener{

	
	public ActivityHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Core.getInstance().dh.getData(e.getPlayer())==null) {
			Core.getInstance().dh.data.add(new UserData(e.getPlayer().getUniqueId(), e.getPlayer().getName(), 0, 0, 0, 0, 0));
		}else {
			Core.getInstance().dh.getData(e.getPlayer()).setName(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			if(Core.getInstance().lh.isLeaderboard((ArmorStand)e.getRightClicked())){
				e.setCancelled(true);
			}
		}
	}
}
