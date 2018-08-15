package com.overload.game.entity.impl.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.overload.Server;
import com.overload.game.content.achievement.Diary.Tasks;
import com.overload.game.content.skill.slayer.SlayerTask;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.rights.Right;
import com.overload.util.Misc;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PlayerSaving {

    public static void save(Player player) {
        // Create the path and file objects.
        Path path = Paths.get(player.VPSEnvironment() ? "./data/saves/characters/" : "./data/saves/charactersdev/", player.getUsername() + ".json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);

        // Attempt to make the player save directory if it doesn't
        // exist.
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for player data!");
            }
        }
        try (FileWriter writer = new FileWriter(file)) {

            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            object.addProperty("username", player.getUsername().trim());
            object.addProperty("member-id", player.getMemberId());
            object.addProperty("password", player.getPassword().trim());
            if (player.getEmail() != null) {
            	object.addProperty("email", player.getEmail().trim());
            }
            object.addProperty("bank-pin", player.getPin().trim());
            object.addProperty("title", player.getLoyaltyTitle());
            int primary = player.getRights().getPrimary().getValue();
			object.addProperty("primary-right", Right.get(primary).name());
			StringBuilder sb1 = new StringBuilder();
			player.getRights().getSet().stream().forEach(
					r -> {
			            int right = r.getValue();
						sb1.append(Right.get(right).name() + ":");
					}
			);
            object.addProperty("secondary-rights", sb1.substring(0, sb1.length() - 1));
            object.addProperty("player-modes", player.getMode().name());
            object.addProperty("unique-string", player.getUniqueString());
			object.addProperty("discord-uid", player.getDiscordUID());
            object.addProperty("starter-state", player.getStarter());
            object.add("position", builder.toJsonTree(player.getPosition()));
            object.addProperty("spell-book", player.getSpellbook().name());
            object.addProperty("fight-type", player.getCombat().getFightType().name());
            object.addProperty("auto-retaliate", player.getCombat().autoRetaliate());
            object.addProperty("xp-locked", player.experienceLocked());
            object.addProperty("clanchat", player.getClanChatName());
            object.addProperty("target-teleport", player.isTargetTeleportUnlocked());
            object.addProperty("preserve", player.isPreserveUnlocked());
            object.addProperty("rigour", player.isRigourUnlocked());
            object.addProperty("augury", player.isAuguryUnlocked());
            object.addProperty("has-veng", player.hasVengeance());
            object.addProperty("last-veng", player.getVengeanceTimer().secondsRemaining());
            object.addProperty("running", player.isRunning());
            object.addProperty("run-energy", player.getRunEnergy());
            object.addProperty("spec-percentage", player.getSpecialPercentage());
            object.addProperty("recoil-damage", player.getRecoilDamage());
            object.addProperty("poison-damage", player.getPoisonDamage());

            object.addProperty("poison-immunity", player.getCombat().getPoisonImmunityTimer().secondsRemaining());
            object.addProperty("fire-immunity", player.getCombat().getFireImmunityTimer().secondsRemaining());
            object.addProperty("teleblock-timer", player.getCombat().getTeleBlockTimer().secondsRemaining());
            object.addProperty("prayerblock-timer", player.getCombat().getPrayerBlockTimer().secondsRemaining());
            object.addProperty("target-search-timer", player.getTargetSearchTimer().secondsRemaining());
            object.addProperty("special-attack-restore-timer", player.getSpecialAttackRestore().secondsRemaining());

            object.addProperty("skull-timer", player.getSkullTimer());
            object.addProperty("skull-type", player.getSkullType().name());

            object.addProperty("total-kills", player.getTotalKills());
            object.addProperty("target-kills", player.getTargetKills());
            object.addProperty("normal-kills", player.getNormalKills());
            object.addProperty("killstreak", player.getKillstreak());
            object.addProperty("highest-killstreak", player.getHighestKillstreak());
            object.add("recent-kills", builder.toJsonTree(player.getRecentKills()));
            object.addProperty("deaths", player.getDeaths());
            object.addProperty("points", player.getPoints());
            object.addProperty("amount-donated", player.getAmountDonated());
            object.addProperty("poison-damage", player.getPoisonDamage());
            object.addProperty("blowpipe-scales", player.getBlowpipeScales());
            object.addProperty("blowpipe-ammo", player.getBlowpipeAmmoType());
            object.addProperty("blowpipe-ammo-amount", player.getBlowpipeAmmoAmount());

            //Slayer
            String[] removed = player.getSlayer().getRemoved();
            StringBuilder sb2 = new StringBuilder();
			for (int i = 0; i < removed.length; i++) {
				sb2.append(removed[i] + ":");
			}
            object.addProperty("removed-slayer-tasks", sb2.substring(0, sb2.length() - 1));
            if (player.getSlayer().getTask().isPresent()) {
            	SlayerTask task = player.getSlayer().getTask().get();
	            object.addProperty("slayer-task", task.getPrimaryName());
	            object.addProperty("slayer-task-amount", player.getSlayer().getTaskAmount());
            }
            object.addProperty("slayer-tasks-completed", player.getSlayerTasksCompleted());
            object.addProperty("slayer-master", player.getSlayer().getMaster());
            object.addProperty("consecutive-tasks", player.getSlayer().getConsecutiveTasks());
            object.addProperty("slayer-points", player.getSlayer().getPoints());

			StringBuilder sb3 = new StringBuilder();
			if (player.getSlayer().getUnlockSet().size() > 0) {
				player.getSlayer().getUnlockSet().stream().forEach(
						u -> {
							sb3.append(u.toString() + ":");
						}
				);
	            object.addProperty("slayer-unlocks", sb3.substring(0, sb3.length() - 1));
			}

			StringBuilder sb4 = new StringBuilder();
			if (player.getSlayer().getExtendSet().size() > 0) {
				player.getSlayer().getExtendSet().stream().forEach(
						e -> {
							sb4.append(e.toString() + ":");
						}
				);
	            object.addProperty("slayer-extends", sb4.substring(0, sb4.length() - 1));
			}
            //RC pouches
            object.add("pouches", builder.toJsonTree(player.getPouches()));

            object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
            object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
            object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
            object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
            object.add("quick-prayers", builder.toJsonTree(player.getQuickPrayers().getPrayers()));

            object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
            object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));

            /** DIARIES **/
        	JsonObject parent = new JsonObject();
        	for (int i = 0; i < player.getDiaryManager().getDiaries().size()-1; i++) {
        		Map<String, Integer> aMap = new HashMap<String, Integer>();
        		final int index = i;
	        	player.getDiaries().entrySet().stream().forEach(d -> {
	        		if (Tasks.valueOf(d.getKey()).getDiaryLocation().name() == player.getDiaryManager().getDiaries().get(index).getRegions().name())
	        			aMap.put(d.getKey(), d.getValue());
	        	});
	        	
        		parent.add(
    				player.getDiaryManager().getDiaries().get(i).getRegions().name(),
    				builder.toJsonTree(aMap)
            	);
        	}
        	object.add(
        		"DIARIES",
        		parent
        	);
            
            /** BANK **/
            for (int i = 0; i < player.getBanks().length; i++) {
                if (i == Bank.BANK_SEARCH_TAB_INDEX) {
                    continue;
                }
                if (player.getBank(i) != null) {
                    object.add("bank-" + i, builder.toJsonTree(player.getBank(i).getValidItems()));
                }
            }

            writer.write(builder.toJson(object));
            writer.close();


        } catch (Exception e) {
            // An error happened while saving.
            Server.getLogger().log(Level.WARNING,
                    "An error has occured while saving a character file!", e);
        }
    }

    public static boolean playerExists(String p) {
        p = Misc.formatPlayerName(p.toLowerCase());
        return new File("./data/saves/characters/" + p + ".json").exists();
    }
}
