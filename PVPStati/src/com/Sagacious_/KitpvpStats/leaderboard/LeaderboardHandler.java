package com.Sagacious_.KitpvpStats.leaderboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.data.UserData;

public class LeaderboardHandler{
	
	private List<Integer> sortKills(){
	      ArrayList<Integer> temp = new ArrayList<Integer>();
	      for(UserData p : Core.getInstance().dh.data) {
	    	  temp.add(p.getKills());
	      }
	      Collections.sort(temp, Collections.reverseOrder());
        return temp;
	}
	
	private List<Integer> sortDeaths(){
	      ArrayList<Integer> temp = new ArrayList<Integer>();
	      for(UserData p : Core.getInstance().dh.data) {
	    	  temp.add(p.getDeaths());
	      }
	      Collections.sort(temp, Collections.reverseOrder());
      return temp;
	}
	
	private List<Integer> sortKillstreak(){
	      ArrayList<Integer> temp = new ArrayList<Integer>();
	      for(UserData p : Core.getInstance().dh.data) {
	    	  temp.add(p.getTopKillstreak());
	      }
	      Collections.sort(temp, Collections.reverseOrder());
    return temp;
	}
	
	
	public List<UserData> killTop = new ArrayList<UserData>();
	public List<UserData> deathTop = new ArrayList<UserData>();
	public List<UserData> killstreakTop = new ArrayList<UserData>();
	
	public LeaderboardHandler() {
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
			public void run() {
				refreshKills();
			}
		}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
			public void run() {
				refreshDeaths();
			}
		}, 60L, 20L*conf.getInt("leaderboard-update-time"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
			public void run() {
				refreshKillstreak();
			}
		}, 80L, 20L*conf.getInt("leaderboard-update-time"));
	}
	
	public void refreshKills() {
		
		List<Integer> s = sortKills();
		List<UserData> temp = new ArrayList<UserData>();
		for(int i = 0; i < s.size(); i++) {
			int curr = s.get(i);
			if(temp.size() < 10) {
			List<UserData> temp2 = Core.getInstance().dh.getKills(curr);
			for(UserData t : temp2) {
				if(!temp.contains(t)) {
				temp.add(t);
				}
			}
			}
		}
		killTop = temp;
    	Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(0));
}

public void refreshDeaths() {
List<Integer> s = sortDeaths();
List<UserData> temp = new ArrayList<UserData>();
for(int i = 0; i < s.size(); i++) {
	int curr = s.get(i);
	if(temp.size() < 10) {
	List<UserData> temp2 = Core.getInstance().dh.getDeaths(curr);
	for(UserData t : temp2) {
		if(!temp.contains(t)) {
		temp.add(t);
		}
	
	}
	}
}

deathTop = temp;
Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(1));
}

public void refreshKillstreak() {
List<Integer> s = sortKillstreak();
List<UserData> temp = new ArrayList<UserData>();
for(int i = 0; i < s.size(); i++) {
	int curr = s.get(i);
	if(temp.size() < 10) {
	List<UserData> temp2 = Core.getInstance().dh.getKillstreak(curr);
	for(UserData t : temp2) {
		if(!temp.contains(t)) {
		temp.add(t);
		}
	}
	}
}

killstreakTop = temp;
Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(2));
}

}
