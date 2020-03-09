package com.Sagacious_.KitpvpStats.api.hook;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class PlaceholderAPIHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion{
	private DecimalFormat df = new DecimalFormat("##0.0");
	private String getKDR(UserData p) {
		if(p.getKills()==0&&p.getDeaths()==0) {return "0.0";}
		if(p.getKills()>p.getDeaths()&&p.getDeaths()==0) {return p.getKills()+".0";}
		if(p.getKills()==p.getDeaths()&&p.getKills()==0) {return "0.0";}
		return df.format(Double.valueOf((double)p.getKills()/(double)p.getDeaths()));
	}
	@Override
	public String getAuthor() {
		return "Sagacious_";
	}

	@Override
	public String getIdentifier() {
		return "pvpstats";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
    public String onPlaceholderRequest(Player p, String id){
		if(p!=null) {
		UserData ps = Core.getInstance().dh.getData(p);
		if(id.equals("level")) {
			return "" + Core.getInstance().getLevel(ps.getKills());
		}
		if(id.equals("kills")) {
			return "" + ps.getKills();
		}
		if(id.equals("deaths")) {
			return "" + ps.getDeaths();
		}
		if(id.equals("kdr")) {
			return "" + getKDR(ps);
		}
		if(id.equals("killstreak")) {
			return "" + ps.getKillstreak();
		}
		if(id.equals("topkillstreak")) {
			return "" + ps.getTopKillstreak();
		}
		if(id.equals("level_progress")) {
			return Core.getInstance().getLevelProgress(ps.getKills());
		}
        if(id.equals("level_progress_percent")) {
			return Core.getInstance().getLevelProgressPercent(ps.getKills());
		}
        if(id.equals("kills_tonextlevel")) {
        	return ""+Core.getInstance().getKillsToNextLevel(ps.getKills());
        }
        if(id.contains("kills_")) {
        	int f = Integer.parseInt(id.split("_")[1])-1;
        	if(Core.getInstance().lh.killTop.size()<=f) {return "None";}
			UserData d = Core.getInstance().lh.killTop.get(f);
			return "" + d.getName();
		}
		if(id.contains("deaths_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.killTop.size()<=f){return "None";}
			UserData d = Core.getInstance().lh.deathTop.get(f);
			return "" + d.getName();
		}
		if(id.contains("killstreak_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.killTop.size()<=f){return "None";}
			UserData d = Core.getInstance().lh.killstreakTop.get(f);
			return "" + d.getName();
		}
		}
		return null;
		
	}
	
	 @Override
	    public boolean persist(){
	        return true;
	    }

	    @Override
	    public boolean canRegister(){
	        return true;
	    }


}
