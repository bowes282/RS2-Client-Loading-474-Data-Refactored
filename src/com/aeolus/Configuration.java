package com.aeolus;
/**
 * The main configuration for the Client
 * @author 7Winds
 */
public class Configuration {
	
	public static String server_address = "localhost";
	
	public static int server_port = 43594;
	
	/**
	 * The url that the users will get redirected to after clicking "New User"
	 */
	public static final String REGISTER_ACCOUNT = "www.google.com";
	
	/**
	 * A string which indicates the Client's name.
	 */
	public static String clientName = "Project Aeolus - Official";
	
	public static boolean normalLogin = true;
	
	/**
	 * Displays debug messages on loginscreen and in-game
	 */
	public static boolean clientData = false;
	
	/**
	 * Enables the use of music played through the client
	 */
	public static boolean enableMusic = true;
	
	/**
	 * Used for change worlds button on login screen
	 */
	public static boolean worldSwitch = false;
	
	/**
	 * Enables extra frames in-between animations to give the animation a smooth look
	 */
	public static boolean enableTweening = true;	
	
	/**
	 * Used to repack indexes 
	 * Index 1 = Models
	 * Index 2 = Animations
	 * Index 3 = Sounds/Music
	 * Index 4 = Maps
	 */
	public static boolean repackIndexOne = false, repackIndexTwo = false,
			repackIndexThree = false, repackIndexFour = false;

	/**
	 * Dump Indexes
	 */
	public static boolean dumpIndexOne = false, dumpIndexTwo = false,
			dumpIndexThree = false, dumpIndexFour = false;
	
	/**
	 * Shows the ids of items, objects, and npcs on right click
	 */
	public static boolean showIds = false;	
	
	public static boolean xp_merge = false;
	
	/**
	 * Enables fog effects
	 */
	public static boolean enableFog = true;	
	
	/**
	 * newDamage enables or disables fake constitution.
	 */
	public static boolean newDamage = true;

	/**
	 * npcBits can be changed to what your server's bits are set to.
	 */
	public static final int npcBits = 12;
	
	/**
	 * Enables the use of run energy
	 */
	public static boolean runEnergy = false;
	
	/**
	 * Displays health above entities heads
	 */
	public static boolean HPaboveHeads = false;
	
	/**
	 * Displays names above entities
	 */
	public static boolean namesAboveHeads = false;
	
	/**
	 * Displays OS Buddy orbs on HUD
	 */
	public static boolean enableOrbs = true;
	
	/**
	 * Enables/Disables Revision 554 hitmarks
	 */
	public static boolean hitmarks554 = false;
	
	/**
	 * Enables/Disables Revision 554 health bar
	 */
	public static boolean hpBar554 = false;
	
	/**
	 * Enables the HUD to display 10 X the amount of hitpoints
	 */
	public static boolean TenXHP = false;

}
