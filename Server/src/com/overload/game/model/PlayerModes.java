package com.overload.game.model;


/**
 * Represents a player's privilege mode.
 * @author Gabriel Hannason
 */

public enum PlayerModes {

	/*
	 * A regular member of the server.
	 */
	NORMAL(0,0,-1,0, ""),
	
	/*
	 * A Player who is an IronMan
	 */
	IRON_MAN(1,1,695,37, "<col=20B2AA><shad=0>"),
	/*
	 * A Player who is a Ultimate Ironman
	 */

    ULTIMATE(2,2,696,38, "<col=20B2AA><shad=0>"),
    /*
     * A Player who is a Hard Core Ironman
     */
	HARD_CORE_IRONMAN(3,3,697,39, "<col=20B2AA><shad=0>");


	PlayerModes(int id,int rankPriority,int iconid,int forumid, String yellHexColorPrefix) {
		this.id = id;
		this.rankPriority = rankPriority;
        this.iconid = iconid;
		this.yellHexColorPrefix = yellHexColorPrefix;
		this.forumid = forumid;
	}

    public int getId() {
        return id;
    }

    public int getIconId() {
        return iconid;
    }
    
    public int getForumId() {
        return forumid;
    }


    private final int id;

	private final int rankPriority;

    private final int iconid;
    
    private final int forumid;

	
	public int getRankPriority() {
		return rankPriority;
	}

	
	private int yellDelay;
	private String yellHexColorPrefix;
	
	public int getYellDelay() {
		return yellDelay;
	}
	
	public String getYellPrefix() {
		return yellHexColorPrefix;
	}
	
	public boolean isEqualOrHigher(PlayerModes other) {
		return rankPriority >= other.rankPriority;
	}
	
	public PlayerModes forId(int id) {
		for (PlayerModes rights : PlayerModes.values()) {
			if (rights.getId() == id) {
				return rights;
			}
		}
		return null;
	}
	
	public PlayerModes forForumId(int id) {
		for (PlayerModes rights : PlayerModes.values()) {
			if (rights.getForumId() == id) {
				return rights;
			}
		}
		return null;
	}
	
	
	
}
