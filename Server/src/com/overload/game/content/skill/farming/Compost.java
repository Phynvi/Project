package com.overload.game.content.skill.farming;

import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Skill;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

import java.util.HashMap;
import java.util.Map;

public class Compost {

    public static final int BUCKET = 1925;

    private static Player player;

    @SuppressWarnings("static-access")
	public Compost(Player player) {
        this.player = player;
    }

    public static int[] compostBins = new int[4];
    public static long[] compostBinsTimer = new long[4];
    public static int[] organicItemAdded = new int[4];
    public static boolean[] compostClosed = { false, false, false, false };

    /* setting up the experiences constants */

    public static final double COMPOST_EXP_RETRIEVE = 4.5;
    public static final double SUPER_COMPOST_EXP_RETRIEVE = 8.5;
    public static final double COMPOST_EXP_USE = 18;
    public static final double SUPER_COMPOST_EXP_USE = 26;

    public static final double ROTTEN_TOMATOES_EXP_RETRIEVE = 8.5;

    /* these are the constants related to compost making */

    public static final int COMPOST = 6032;

    public static final int SUPER_COMPOST = 6034;

    public static final int ROTTE_TOMATO = 2518;

    public static final int TOMATO = 1982;

    public static final int FIRST_TYPE_COMPOST_BIN = 7808;

    public static final int SECOND_TYPE_COMPOST_BIN = 7818;

    public static final int[] COMPOST_ORGANIC = {6055, 1942, 1957, 1965, 5986, 5504, 5982, 249, 251, 253, 255, 257, 2998, 259, 261, 263, 3000, 265, 2481, 267, 269, 1951, 753, 2126, 247, 239, 6018};

    public static final int[] SUPER_COMPOST_ORGANIC = {2114, 5978, 5980, 5982, 6004, 247, 6469};

    /* this is the enum that stores the different locations of the compost bins */

    public enum CompostBinLocations {
        NORTH_ARDOUGNE(0, FIRST_TYPE_COMPOST_BIN, 3, 2661, 3375),
        PHASMATYS(1, SECOND_TYPE_COMPOST_BIN, 1, 3610, 3522),
        FALADOR(2, FIRST_TYPE_COMPOST_BIN, 4, 3056, 3312),
        CATHERBY(3, FIRST_TYPE_COMPOST_BIN, 3, 2804, 3464);

        private int compostIndex;
        private int binObjectId;
        private int objectFace;
        private int x, y;
        private static Map<Integer, CompostBinLocations> bins = new HashMap<Integer, CompostBinLocations>();

        static {
            for (CompostBinLocations data : CompostBinLocations.values()) {
                bins.put(data.compostIndex, data);
            }
        }

        CompostBinLocations(int compostIndex, int binObjectId, int objectFace, int x, int y) {
            this.compostIndex = compostIndex;
            this.binObjectId = binObjectId;
            this.objectFace = objectFace;
            this.x = x;
            this.y = y;
        }

        public static CompostBinLocations forId(int index) {
            return bins.get(index);
        }

        public static CompostBinLocations forPosition(int x, int y) {
            for (CompostBinLocations compostBinLocations : CompostBinLocations.values()) {
                if (compostBinLocations.x == x && compostBinLocations.y == y) {
                    return compostBinLocations;
                }
            }
            return null;
        }

        public int getCompostIndex() {
            return compostIndex;
        }

        public int getBinObjectId() {
            return binObjectId;
        }

        public int getObjectFace() {
            return objectFace;
        }
    }

    /* this is the enum that stores the different compost bins stages */

    public enum CompostBinStages {
        FIRST_TYPE(7808, 7813, 7809, 7810, 7811, 7812, 7814, 7815, 7816, 7817, 7828, 7829, 7830, 7831),
        SECOND_TYPE(7818, 7823, 7819, 7820, 7821, 7822, 7824, 7825, 7826, 7827, 7832, 7833, 7834, 7835);
        private int binEmpty;
        private int closedBin;
        private int binWithCompostable;
        private int binFullOfCompostable;
        private int binWithSuperCompostable;
        private int binFullOFSuperCompostable;
        private int binWithCompost;
        private int binFullOfCompost;
        private int binWithSuperCompost;
        private int binFullOfSuperCompost;
        private int binWithTomatoes;
        private int binFullOfTomatoes;
        private int binWithRottenTomatoes;
        private int binFullOfRottenTomatoes;

        private static Map<Integer, CompostBinStages> bins = new HashMap<Integer, CompostBinStages>();

        static {
            for (CompostBinStages data : CompostBinStages.values()) {
                bins.put(data.binEmpty, data);
            }
        }

        CompostBinStages(int binEmpty, int closedBin, int binWithCompostable, int binFullOfCompostable, int binWithSuperCompostable, int binFullOFSuperCompostable, int binWithCompost, int binFullOfCompost, int binWithSuperCompost, int binFullOfSuperCompost, int binWithTomatoes, int binFullOfTomatoes, int binWithRottenTomatoes, int binFullOfRottenTomatoes) {
            this.binEmpty = binEmpty;
            this.closedBin = closedBin;
            this.binWithCompostable = binWithCompostable;
            this.binFullOfCompostable = binFullOfCompostable;
            this.binWithSuperCompostable = binWithSuperCompostable;
            this.binFullOFSuperCompostable = binFullOFSuperCompostable;
            this.binWithCompost = binWithCompost;
            this.binFullOfCompost = binFullOfCompost;
            this.binWithSuperCompost = binWithSuperCompost;
            this.binFullOfSuperCompost = binFullOfSuperCompost;
            this.binWithTomatoes = binWithTomatoes;
            this.binFullOfTomatoes = binFullOfTomatoes;
            this.binWithRottenTomatoes = binWithRottenTomatoes;
            this.binFullOfRottenTomatoes = binFullOfRottenTomatoes;
        }

        public static CompostBinStages forId(int binId) {
            return bins.get(binId);
        }

        public int getBinEmpty() {
            return binEmpty;
        }

        public int getClosedBin() {
            return closedBin;
        }

        public int getBinWithCompostable() {
            return binWithCompostable;
        }

        public int getBinFullOfCompostable() {
            return binFullOfCompostable;
        }

        public int getBinWithSuperCompostable() {
            return binWithSuperCompostable;
        }

        public int getBinFullOFSuperCompostable() {
            return binFullOFSuperCompostable;
        }

        public int getBinWithCompost() {
            return binWithCompost;
        }

        public int getBinFullOfCompost() {
            return binFullOfCompost;
        }

        public int getBinWithSuperCompost() {
            return binWithSuperCompost;
        }

        public int getBinFullOfSuperCompost() {
            return binFullOfSuperCompost;
        }

        public int getBinWithTomatoes() {
            return binWithTomatoes;
        }

        public int getBinFullOfTomatoes() {
            return binFullOfTomatoes;
        }

        public int getBinWithRottenTomatoes() {
            return binWithRottenTomatoes;
        }

        public int getBinFullOfRottenTomatoes() {
            return binFullOfRottenTomatoes;
        }
    }

    /* handle compost bin updating */
	public void updateCompostBin(int index, boolean close) {
		if (close) {
	    	player.getPacketSender().sendConfigByte(1057, 31);
		} else {
			int totalWeeds = compostBins[index];
			int binFull = 150;
			int binFullSuper = 250;
			int value = totalWeeds >= binFullSuper ? ((totalWeeds % 250) * 15) :
						totalWeeds >= binFull 	   ? ((totalWeeds % 150) * 15) : 1;
	    	player.getPacketSender().sendConfigByte(1057, value);
		}
    }

	public void openClose(final int index, int x, int y) {
		if (compostClosed[index]) {
			openCompostBin(index);
		} else {
			closeCompostBin(index);
		}
	}
	
    /* handle what happens when the player close the compost bin */
    public void closeCompostBin(final int index) {
        compostBinsTimer[index] = Farming.getMinutesCounter(player);

        player.performAnimation(new Animation(835));
        player.setCurrentTask(new Task(1, player, false) {
            @Override
            public void execute() {
                player.getPacketSender().sendMessage("You close the compost bin, and its content start to rot.");
                updateCompostBin(index, true);
                stop();
            }

            @Override
            public void stop() {
        		compostClosed[index] = true;
            	player.getCurrentTask().setEventRunning(false);
            }
        });
        TaskManager.submit(player.getCurrentTask());
        player.getCurrentTask().setEventRunning(true);
    }

    /* handle what happens when the player opens the compost bin */
    public void openCompostBin(final int index) {
        // check if the time elapsed is enough to rot the compost
        int timerRequired;
        timerRequired = compostBins[index] == 200 ? 90 : 45;
        if (Farming.getMinutesCounter(player) - compostBinsTimer[index] >= timerRequired) {
            compostBins[index] += 50;
            player.performAnimation(new Animation(834));
            player.setCurrentTask(new Task(1, player, false) {
                @Override
                public void execute() {
                    updateCompostBin(index, false);
                    stop();
                }

                @Override
                public void stop() {
            		compostClosed[index] = false;
                	player.getCurrentTask().setEventRunning(false);
                }
            });
            TaskManager.submit(player.getCurrentTask());
            player.getCurrentTask().setEventRunning(true);
        } else {
            player.getPacketSender().sendMessage("The compost bin is still rotting. I should wait until it is complete.");
        }
    }

    /* handle compost bin filling */
    public boolean fillCompostBin(int x, int y, final int organicItemUsed) {

        final CompostBinLocations compostBinLocations = CompostBinLocations.forPosition(x, y);
        if (compostBinLocations == null) {
            return false;
        }
        final int index = compostBinLocations.getCompostIndex();

        int incrementFactor = 0;
        // setting up the different increments.
        for (int normalCompost : COMPOST_ORGANIC) {
            if (organicItemUsed == normalCompost) {
                incrementFactor = 2;
            }
        }

        for (int superCompost : SUPER_COMPOST_ORGANIC) {
            if (organicItemUsed == superCompost) {
                incrementFactor = 17;
            }
        }

        if (organicItemUsed == TOMATO) {
            if (compostBins[index] % 77 == 0) {
                incrementFactor = 77;
            } else {
                incrementFactor = 2;
            }
        }

        // checking if the item used was an organic item.
        if (incrementFactor == 0) {
            player.getPacketSender().sendMessage("You need to put organic items into the compost bin in order to make compost.");
            return false;
        }
        final int factor = incrementFactor;
        // launching the main event for filling the compost bin.
        // Controller controller = player.getController();
        // player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                if (!player.getInventory().contains(organicItemUsed) || organicItemAdded[index] == 15) {
                    stop();
                    return;
                }
                organicItemAdded[index]++;
                player.performAnimation(new Animation(832));
                player.getInventory().delete(organicItemUsed, 1);
                compostBins[index] += factor;
                player.getCompost().updateCompostBin(index, false);
            }

            @Override
            public void stop() {
                player.performAnimation(new Animation(65535));
            }
        });
        return true;
    }

    public void retrieveCompost(final int index) {

        final int finalItem = compostBins[index] == 150 ? COMPOST : compostBins[index] == 250 ? SUPER_COMPOST : ROTTE_TOMATO;

        player.performAnimation(new Animation(832));
        // Controller controller = player.getController();
        // player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                if (!player.getInventory().contains(BUCKET) && compostBins[index] != 350 || organicItemAdded[index] == 0) {
                    stop();
                    return;
                }
                player.getSkillManager().addExperience(Skill.FARMING, 10);
                if (compostBins[index] != 350) {
                    player.getInventory().delete(BUCKET, 1);
                }
                player.getInventory().add(finalItem, 1);
                player.performAnimation(new Animation(832));
                organicItemAdded[index]--;
                if (organicItemAdded[index] == 0) {
                    resetVariables(index);
                }
                player.getCompost().updateCompostBin(index, false);
            }

            @Override
            public void stop() {
                player.performAnimation(new Animation(65535));
            }
        });
    }

    /* reseting the compost variables */

    public static int resetVariables(int index) {
        compostBins[index] = 0;
        compostBinsTimer[index] = 0;
        organicItemAdded[index] = 0;
        return index;
    }
}