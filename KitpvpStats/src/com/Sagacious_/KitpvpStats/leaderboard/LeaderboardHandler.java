package com.Sagacious_.KitpvpStats.leaderboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class LeaderboardHandler implements Listener{
	public class Hologram {
		private ArmorStand as;
		private Location loc;
		public Hologram(Location loc, String text) {
			this.loc = loc;
			as = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setVisible(false);
			as.setBasePlate(false);
			as.setCustomName(text);
			as.setCustomNameVisible(true);
			as.setRemoveWhenFarAway(false);
		}
		
		public Location getLocation() {
			return loc;
		}
		
		public void teleport(Location loc) {
			this.loc = loc;
			as.teleport(loc);
		}
		
		public ArmorStand getStand() {
			return as;
		}
		
		public void kill() {
			as.remove();
		}
		
		public void setText(String text) {
			as.setCustomName(text);
		}
	}
	
	public boolean isLeaderboard(ArmorStand as) {
		for(Hologram k : kill_hologram) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : deaths_hologram) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : killstreak_hologram) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		return false;
	}
	

	private boolean lke = false;
	private boolean lde = false;
	private boolean lkie = false;
	
	private Location lke_l;
	private Location lde_l;
	private Location lkie_l;
	
	public List<Hologram> kill_hologram = new ArrayList<Hologram>();
	public List<Hologram> deaths_hologram = new ArrayList<Hologram>();
	public List<Hologram> killstreak_hologram = new ArrayList<Hologram>();
	private String format;
	
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
	
	// Zet uit dat je armor enzo kan equippen!
	public LeaderboardHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = Core.getInstance().getConfig();
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
		lke = conf.getBoolean("leaderboard-kills-enabled");
		if(lke) {
			String[] l = conf.getString("leaderboard-kills-location").split(",");
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			kill_hologram.add(new Hologram(lke_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			for(int i = 0; i < 10; i++) {
				lke_l.setY(lke_l.getY()-0.2D);
				kill_hologram.add(new Hologram(lke_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshKills();
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		if(lde) {
			String[] l = conf.getString("leaderboard-deaths-location").split(",");
			lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			deaths_hologram.add(new Hologram(lde_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			for(int i = 0; i < 10; i++) {
				lde_l.setY(lde_l.getY()-0.2D);
				deaths_hologram.add(new Hologram(lde_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshDeaths();
				}
			}, 60L, 20L*conf.getInt("leaderboard-update-time"));
		}
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		if(lkie) {
			String[] l = conf.getString("leaderboard-killstreak-location").split(",");
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			killstreak_hologram.add(new Hologram(lkie_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header"))));
			for(int i = 0; i < 10; i++) {
				lkie_l.setY(lkie_l.getY()-0.2D);
				killstreak_hologram.add(new Hologram(lkie_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshKillstreak();
				}
			}, 80L, 20L*conf.getInt("leaderboard-update-time"));
		}
	}
	
	public void save() {
		File f = new File(Core.getInstance().getDataFolder(), "config.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!kill_hologram.isEmpty()) {
			Location l = kill_hologram.get(0).getLocation();
			conf.set("leaderboard-kills-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2) + "," + l.getZ());
		}
        if(!deaths_hologram.isEmpty()) {
        	Location l = deaths_hologram.get(0).getLocation();
			conf.set("leaderboard-deaths-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2) + "," + l.getZ());
		}
        if(!killstreak_hologram.isEmpty()) {
        	Location l = killstreak_hologram.get(0).getLocation();
			conf.set("leaderboard-killstreak-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2) + "," + l.getZ());
        }
        try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		for(int i = 1; i < kill_hologram.size(); i++) {
			if(i-1 < temp.size()) {
			Hologram h = kill_hologram.get(i);
			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
			}
		}
		s.clear();
		temp.clear();
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
for(int i = 1; i < deaths_hologram.size(); i++) {
	if(i-1 < temp.size()) {
	Hologram h = deaths_hologram.get(i);
	h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
	}
}
s.clear();
temp.clear();
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
for(int i = 1; i < killstreak_hologram.size(); i++) {
	if(i-1 < temp.size()) {
	Hologram h = killstreak_hologram.get(i);
	h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getTopKillstreak()));
	}
}
s.clear();
temp.clear();
}
	
	public void killAll() {
		for(Hologram h : kill_hologram) {
			h.kill();
		}
		for(Hologram h : deaths_hologram) {
			h.kill();
		}
		for(Hologram h : killstreak_hologram) {
			h.kill();
		}
	}
}
