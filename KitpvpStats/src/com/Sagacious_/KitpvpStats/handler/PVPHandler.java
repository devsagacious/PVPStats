package com.Sagacious_.KitpvpStats.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class PVPHandler implements Listener{
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		UserData u = Core.getInstance().dh.getData(e.getEntity());
		u.setKillstreak(0);u.setDeaths(u.getDeaths()+1);
		if(e.getEntity().getKiller()!=null && e.getEntity().getKiller() instanceof Player) {
			UserData u2 = Core.getInstance().dh.getData(e.getEntity().getKiller());
			u2.setKillstreak(u2.getKillstreak()+1);
			u2.setKills(u2.getKills()+1);
			Core.getInstance().kh.reward(e.getEntity().getKiller(), u2.getKillstreak());
		}
	}

}
