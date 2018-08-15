package com.overload.settings;

/**
 * 
 * @author Zion
 * 
 * This class stores the account data
 * 
 */
public class AccountData {
	
	/**
	 * Stores the account rank
	 */
	public int rank;
	
	/**
	 * Stores the amount of uses of the account
	 */
	public int mode;
	
	/**
	 * Stores account username
	 */
	public String username;
	
	/**
	 * Stores account password
	 */
	public String password;
	
	/**
	 * Stores account Profile Picture
	 */
	public String profileUrl;
	
	/**
	 * Stores the new account data
	 * 
	 * @param rank
	 * @param uses
	 * @param username
	 * @param password
	 */
	public AccountData(int rank, int mode, String username, String password, String profileUrl) {
		this.rank = rank;
		this.mode = mode;
		this.username = username;
		this.password = password;
		this.profileUrl = profileUrl;
	}
}