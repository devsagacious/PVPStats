package com.Sagacious_.KitpvpStats.api;

import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class PVPStatsAPI {
	
	public static UserData getData(Player p) {
		return Core.getInstance().dh.getData(p);
	}
	
	public static int getLevelInteger(Player p) {
		return Core.getInstance().getLevelInt(getData(p).getKills());
	}
	
	public static int getLevelInteger(int kills) {
		return Core.getInstance().getLevelInt(kills);
	}
	
	public static String getLevelString(Player p) {
		return Core.getInstance().getLevel(getData(p).getKills());
	}
	
	public static String getLevelString(int kills) {
		return Core.getInstance().getLevel(kills);
	}
	
	public static int getKills(Player p) {
		return getData(p).getKills();
	}
	
	public static int getDeaths(Player p) {
		return getData(p).getDeaths();
	}
	
	public static int getKillstreak(Player p) {
		return getData(p).getKillstreak();
	}
	
	public static int getTopKillstreak(Player p) {
		return getData(p).getTopKillstreak();
	}
	
	public static int getResets(Player p) {
		return getData(p).getResets();
	}

}
