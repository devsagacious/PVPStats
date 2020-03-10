package com.Sagacious_.KitpvpStats.leaderboard;

import java.io.File;
/*import java.io.FileWriter;*/
import java.io.IOException;
/*import java.io.PrintWriter;*/
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
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
			((Entity)as).remove();
			if(!as.isDead()) {
				as.setHealth(0);
			}
			for(Entity e : loc.getWorld().getEntities()) {
				if(e.getEntityId()==as.getEntityId()) {
					e.remove();
				}
			}
		}
		
		public void setText(String text) {
			as.setCustomName(text);
		}
	}
	
	@EventHandler
	public void onArmorDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof ArmorStand) {
			ArmorStand as = (ArmorStand)e.getEntity();
			if(isLeaderboard(as)) {
				e.setDroppedExp(0);
				e.getDrops().clear();
			}
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
	
	
	public List<UserData> killTop = new ArrayList<UserData>();
	public List<UserData> deathTop = new ArrayList<UserData>();
	public List<UserData> killstreakTop = new ArrayList<UserData>();
	
	private DecimalFormat df = new DecimalFormat("####0.0##############");
	public void setupLeaderboard(boolean kills, boolean deaths, boolean killstreak, Location loc) {
		FileConfiguration conf = Core.getInstance().getConfig();
		if(loc==null||loc.getWorld()==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
		if(kills) {
			if(!Core.getInstance().useHolographic) {
			lke_l = new Location(loc.getWorld(), Double.valueOf(df.format(loc.getX()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getY()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getZ()).replaceAll(",", ".")));
			kill_hologram.add(new Hologram(lke_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			for(int i = 0; i < 10; i++) {
				lke_l.setY(lke_l.getY()-0.3D);
				kill_hologram.add(new Hologram(lke_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lke_l.setY(lke_l.getY()-0.3D);
			kill_hologram.add(new Hologram(lke_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshKills();
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		else if(deaths) {
			if(!Core.getInstance().useHolographic) {
			lde_l = new Location(loc.getWorld(), Double.valueOf(df.format(loc.getX()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getY()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getZ()).replaceAll(",", ".")));

			deaths_hologram.add(new Hologram(lde_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			for(int i = 0; i < 10; i++) {
				lde_l.setY(lde_l.getY()-0.3D);
				deaths_hologram.add(new Hologram(lde_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lde_l.setY(lde_l.getY()-0.3D);
			deaths_hologram.add(new Hologram(lde_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshDeaths();
				}
			}, 60L, 20L*conf.getInt("leaderboard-update-time"));
		}
		else if(killstreak) {
			if(!Core.getInstance().useHolographic) {
			lkie_l = new Location(loc.getWorld(), Double.valueOf(df.format(loc.getX()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getY()).replaceAll(",", ".")), Double.valueOf(df.format(loc.getZ()).replaceAll(",", ".")));

			killstreak_hologram.add(new Hologram(lkie_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header"))));
			for(int i = 0; i < 10; i++) {
				lkie_l.setY(lkie_l.getY()-0.3D);
				killstreak_hologram.add(new Hologram(lkie_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lkie_l.setY(lkie_l.getY()-0.3D);
			killstreak_hologram.add(new Hologram(lkie_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshKillstreak();
				}
			}, 80L, 20L*conf.getInt("leaderboard-update-time"));
		}
	}
	
	public LeaderboardHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = Core.getInstance().getConfig();
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
		lke = conf.getBoolean("leaderboard-kills-enabled");
		if(lke) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-kills-location").split(",");
			if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));

			kill_hologram.add(new Hologram(lke_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			for(int i = 0; i < 10; i++) {
				lke_l.setY(lke_l.getY()-0.3D);
				kill_hologram.add(new Hologram(lke_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lke_l.setY(lke_l.getY()-0.3D);
			kill_hologram.add(new Hologram(lke_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshKills();
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		if(lde) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-deaths-location").split(",");
			if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));

			deaths_hologram.add(new Hologram(lde_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			for(int i = 0; i < 10; i++) {
				lde_l.setY(lde_l.getY()-0.3D);
				deaths_hologram.add(new Hologram(lde_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lde_l.setY(lde_l.getY()-0.3D);
			deaths_hologram.add(new Hologram(lde_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshDeaths();
				}
			}, 60L, 20L*conf.getInt("leaderboard-update-time"));
		}
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		if(lkie) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-killstreak-location").split(",");
			if(Bukkit.getWorld(l[0])==null) {Core.getInstance().getLogger().info("World does not exist, leaderboards will not work (change in config.yml)");return;}
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));

			killstreak_hologram.add(new Hologram(lkie_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header"))));
			for(int i = 0; i < 10; i++) {
				lkie_l.setY(lkie_l.getY()-0.3D);
				killstreak_hologram.add(new Hologram(lkie_l, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			lkie_l.setY(lkie_l.getY()-0.3D);
			killstreak_hologram.add(new Hologram(lkie_l, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer"))));
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
			conf.set("leaderboard-kills-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
        if(!deaths_hologram.isEmpty()) {
        	Location l = deaths_hologram.get(0).getLocation();
			conf.set("leaderboard-deaths-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
        if(!killstreak_hologram.isEmpty()) {
        	Location l = killstreak_hologram.get(0).getLocation();
			conf.set("leaderboard-killstreak-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
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
		if(!Core.getInstance().useHolographic) {
		for(int i = 1; i < kill_hologram.size()-1; i++) {
			if(i-1 < temp.size()) {
			Hologram h = kill_hologram.get(i);
			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
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
if(!Core.getInstance().useHolographic) {
for(int i = 1; i < deaths_hologram.size()-1; i++) {
	if(i-1 < temp.size()) {
	Hologram h = deaths_hologram.get(i);
	h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
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
if(!Core.getInstance().useHolographic) {
for(int i = 1; i < killstreak_hologram.size()-1; i++) {
	if(i-1 < temp.size()) {
	Hologram h = killstreak_hologram.get(i);
	h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getTopKillstreak()));
	}
}
}
killstreakTop = temp;
Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(2));
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
