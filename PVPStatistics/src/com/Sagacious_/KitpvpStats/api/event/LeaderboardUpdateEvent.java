package com.Sagacious_.KitpvpStats.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeaderboardUpdateEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private int leaderboard;
	
	public LeaderboardUpdateEvent(int leaderboard) {
		this.leaderboard = leaderboard;
	}
	
	public int getLeaderboard() {
		return leaderboard;
	}
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	

}
