package com.Sagacious_.KitpvpStats.api.hook;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

import be.maximvdw.placeholderapi.PlaceholderAPI;



public class PlaceholdersHook {
	private DecimalFormat df = new DecimalFormat("##0.0");
	private String getKDR(UserData p) {
		if(p.getKills()==0&&p.getDeaths()==0) {return "0.0";}
		if(p.getKills()>p.getDeaths()&&p.getDeaths()==0) {return p.getKills()+".0";}
		if(p.getKills()==p.getDeaths()&&p.getKills()==0) {return "0.0";}
		return df.format(Double.valueOf((double)p.getKills()/(double)p.getDeaths()));
	}

	public PlaceholdersHook() {
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getKills();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getDeaths();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_top_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getTopKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kdr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+getKDR(data);
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return Core.getInstance().getLevel(data.getKills());
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return Core.getInstance().getLevelProgress(data.getKills());
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress_percent", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return Core.getInstance().getLevelProgressPercent(data.getKills());
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills_tonextlevel", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+Core.getInstance().getKillsToNextLevel(data.getKills());
				}
			});
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						List<UserData> d = Core.getInstance().dh.getKills(Core.getInstance().lh.killTop.get(f));
						if(d.get(f)==null) {return "None";}
						return "" + d.get(f).getName();
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						List<UserData> d = Core.getInstance().dh.getDeaths(Core.getInstance().lh.deathTop.get(f));
						if(d.get(f)==null) {return "None";}
						return "" + d.get(f).getName();
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						List<UserData> d = Core.getInstance().dh.getKillstreak(Core.getInstance().lh.killstreakTop.get(f));
						if(d.get(f)==null) {return "None";}
						return "" + d.get(f).getName();
					}
				});
			}
	}
	}
	
	public String format(Player p, String s) {
		return PlaceholderAPI.replacePlaceholders(p, s);
	}
}

