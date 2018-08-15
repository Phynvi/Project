package com.overload;

/**
 * The main configuration for the Client
 *
 * @author Seven
 */
public final class Configuration {

    public static final int UID = 8784521;
    public static final String CACHE_DIRECTORY = System.getProperty("user.home") + "/Overload/";
    public static final boolean ENABLE_RSA = true;
    public static final String CLIENT_NAME = "Project";
    public static final int npcBits = 14;
    public static boolean clientDebug = true;
    
    private static boolean live = true;
    public static String FILE_SERVER_ADDRESS = live ? "thedreamvps.info" : "localhost";
    public static String SERVER_ADDRESS = live ? "thedreamvps.info" : "localhost";
    public static int SERVER_PORT = 43595;
    public static int FILE_SERVER_PORT = 43596;
    public static boolean JAGCACHED_ENABLED = false;

    public static boolean DUMP_NPC_DEFS = false;
    
    public static boolean UPDATE_CACHE = true;
    
    public static boolean repackIndexOne = false, repackIndexTwo = false, repackIndexThree = false, repackIndexFour = false;
    public static boolean dumpIndexOne = false, dumpIndexTwo = false, dumpIndexThree = false, dumpIndexFour = false;
    

    public static boolean clientData = false;
    public static boolean enableMusic = true;
    public static boolean expCounterOpen = true;
    public static boolean mergeExpDrops = true;
    public static boolean escapeCloseInterface = false;
    public static boolean mediaMode = false;
    
    public static boolean dumpInterfaces = false;
    
    /**
     * Attack option priorities
     * 0 -> Depends on combat level
     * 1 -> Always right-click
     * 2 -> Left-click where available
     * 3 -> Hidden
     */
    public static int playerAttackOptionPriority = 0;
    public static int npcAttackOptionPriority = 2;
   
    public static boolean enableShiftClickDrop = true;
   
    private Configuration() {

    }
}
