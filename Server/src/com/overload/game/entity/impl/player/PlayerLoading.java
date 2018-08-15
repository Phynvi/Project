package com.overload.game.entity.impl.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.overload.game.content.PrayerHandler.PrayerData;
import com.overload.game.content.achievement.DiaryRegions;
import com.overload.game.content.combat.FightType;
import com.overload.game.content.skill.SkillManager.Skills;
import com.overload.game.content.skill.runecrafting.Runecrafting.PouchContainer;
import com.overload.game.content.skill.slayer.Extends;
import com.overload.game.content.skill.slayer.SlayerMaster;
import com.overload.game.content.skill.slayer.SlayerTask;
import com.overload.game.content.skill.slayer.Unlocks;
import com.overload.game.model.Item;
import com.overload.game.model.MagicSpellbook;
import com.overload.game.model.PlayerModes;
import com.overload.game.model.Position;
import com.overload.game.model.SkullType;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.rights.Right;
import com.overload.net.codec.LoginResponse;
import com.overload.util.Misc;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class PlayerLoading {

    public static int getResult(Player player) {
        // Create the path and file objects.
    	Path path = Paths.get(player.VPSEnvironment() ? "./data/saves/characters/" : "./data/saves/charactersdev/", player.getUsername() + ".json");
        
        File file = path.toFile();

        // If the file doesn't exist, we're logging in for the first
        // time and can skip all of this.
        if (!file.exists()) {
            return LoginResponse.NO_RESPONSE.getOpcode();
        }
        // Now read the properties from the json parser.
        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);

            if (reader.has("username")) {
                player.setUsername(reader.get("username").getAsString());
            }
            
            if (reader.has("member-id")) {
                player.setMemberId(reader.get("member-id").getAsInt());
            }
            
            if (reader.has("password")) {
                String password = reader.get("password").getAsString();
                if (!player.getPassword().equals(password)) {
                    return LoginResponse.INVALID_CREDENTIALS.getOpcode();
                }
                player.setPassword(password);
            }
            
            
            if (reader.has("email")) {
                player.setEmail(reader.get("email").getAsString());
            }
            
            if (reader.has("bank-pin")) {
                player.setPin(reader.get("bank-pin").getAsString());
            }

            if (reader.has("title")) {
                player.setLoyaltyTitle(reader.get("title").getAsString());
            }

            if (reader.has("primary-right")) {
                String rights = reader.get("primary-right").getAsString();
                Right primary = (Right)Right.valueOf(rights);
                player.getRights().setPrimary(primary);
            }
                
            if (reader.has("secondary-rights")) {
                String[] secondaryRights = reader.get("secondary-rights").getAsString().split(":");
                for (int i = 0; i < secondaryRights.length; i++) {
                	Right secondary = (Right)Right.valueOf(secondaryRights[i]);
                	player.getRights().add(secondary);
                }
            }
            

            if (reader.has("player-modes")) {
                String rights = reader.get("player-modes").getAsString();
                player.setMode(PlayerModes.valueOf(rights));
            }
            
            if (reader.has("unique-string")) {
				player.setUniqueString(reader.get("unique-string").getAsString());
			}
			
			if (reader.has("discord-uid")) {
				player.setDiscordUID(reader.get("discord-uid").getAsString());
			}
			
			if (reader.has("starter-state")) {
				player.setStarter(reader.get("starter-state").getAsInt());
			}

            if (reader.has("position")) {
                player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
            }

            if (reader.has("spell-book")) {
                player.setSpellbook(MagicSpellbook.valueOf(reader.get("spell-book").getAsString()));
            }

            if (reader.has("fight-type")) {
                player.getCombat().setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
            }

            if (reader.has("auto-retaliate")) {
                player.getCombat().setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
            }

            if (reader.has("xp-locked")) {
                player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
            }

            if (reader.has("clanchat")) {
                player.setClanChatName(reader.get("clanchat").getAsString());
            }

            if (reader.has("target-teleport")) {
                player.setTargetTeleportUnlocked(reader.get("target-teleport").getAsBoolean());
            }

            if (reader.has("preserve")) {
                player.setPreserveUnlocked(reader.get("preserve").getAsBoolean());
            }

            if (reader.has("rigour")) {
                player.setRigourUnlocked(reader.get("rigour").getAsBoolean());
            }

            if (reader.has("augury")) {
                player.setAuguryUnlocked(reader.get("augury").getAsBoolean());
            }

            if (reader.has("has-veng")) {
                player.setHasVengeance(reader.get("has-veng").getAsBoolean());
            }

            if (reader.has("last-veng")) {
                player.getVengeanceTimer().start(reader.get("last-veng").getAsInt());
            }

            if (reader.has("spec-percentage")) {
                player.setSpecialPercentage(reader.get("spec-percentage").getAsInt());
            }

            if (reader.has("recoil-damage")) {
                player.setRecoilDamage(reader.get("recoil-damage").getAsInt());
            }

            if (reader.has("poison-damage")) {
                player.setPoisonDamage(reader.get("poison-damage").getAsInt());
            }

            if (reader.has("blowpipe-scales")) {
                player.setBlowpipeScales(reader.get("blowpipe-scales").getAsInt());
            }

            if (reader.has("blowpipe-ammo")) {
                player.setBlowpipeAmmoType(reader.get("blowpipe-ammo").getAsInt());
            }

            if (reader.has("blowpipe-ammo-amount")) {
                player.setBlowpipeAmmoAmount(reader.get("blowpipe-ammo-amount").getAsInt());
            }

            if (reader.has("poison-immunity")) {
                player.getCombat().getPoisonImmunityTimer().start(reader.get("poison-immunity").getAsInt());
            }

            if (reader.has("fire-immunity")) {
                player.getCombat().getFireImmunityTimer().start(reader.get("fire-immunity").getAsInt());
            }

            if (reader.has("teleblock-timer")) {
                player.getCombat().getTeleBlockTimer().start(reader.get("teleblock-timer").getAsInt());
            }

            if (reader.has("prayerblock-timer")) {
                player.getCombat().getPrayerBlockTimer().start(reader.get("prayerblock-timer").getAsInt());
            }

            if (reader.has("target-search-timer")) {
                player.getTargetSearchTimer().start(reader.get("target-search-timer").getAsInt());
            }

            if (reader.has("special-attack-restore-timer")) {
                player.getSpecialAttackRestore().start(reader.get("special-attack-restore-timer").getAsInt());
            }

            if (reader.has("skull-timer")) {
                player.setSkullTimer(reader.get("skull-timer").getAsInt());
            }

            if (reader.has("skull-type")) {
                player.setSkullType(SkullType.valueOf(reader.get("skull-type").getAsString()));
            }

            if (reader.has("running")) {
                player.setRunning(reader.get("running").getAsBoolean());
            }

            if (reader.has("run-energy")) {
                player.setRunEnergy(reader.get("run-energy").getAsInt());
            }
            if (reader.has("total-kills")) {
                player.setTotalKills(reader.get("total-kills").getAsInt());
            }
            if (reader.has("target-kills")) {
                player.setTargetKills(reader.get("target-kills").getAsInt());
            }
            if (reader.has("normal-kills")) {
                player.setNormalKills(reader.get("normal-kills").getAsInt());
            }
            if (reader.has("killstreak")) {
                player.setKillstreak(reader.get("killstreak").getAsInt());
            }
            if (reader.has("highest-killstreak")) {
                player.setHighestKillstreak(reader.get("highest-killstreak").getAsInt());
            }
            if (reader.has("recent-kills")) {
                String[] recentKills = builder.fromJson(
                        reader.get("recent-kills").getAsJsonArray(), String[].class);
                for (String l : recentKills) {
                    player.getRecentKills().add(l);
                }
            }
            if (reader.has("deaths")) {
                player.setDeaths(reader.get("deaths").getAsInt());
            }

            if (reader.has("points")) {
                player.setPoints(reader.get("points").getAsInt());
            }

            if (reader.has("amount-donated")) {
                player.setAmountDonated(reader.get("amount-donated").getAsInt());
            }
            
            //Slayer Loading
            if (reader.has("removed-slayer-tasks")) {
            	String[] tasks = reader.get("removed-slayer-tasks").getAsString().split(":");
            	String[] backing = Misc.nullToEmpty(4);
            	for (int i = 0; i < tasks.length; i++) {
            		backing[i] = tasks[i];
            	}
            	player.getSlayer().setRemoved(backing);
            }
            if (reader.has("slayer-task")) {
				Optional<SlayerTask> task = SlayerMaster.get(reader.get("slayer-task").getAsString());
				player.getSlayer().setTask(task);
            }
            if (reader.has("slayer-task-amount")) {
                player.getSlayer().setTaskAmount(reader.get("slayer-task-amount").getAsInt());
            }
            if (reader.has("slayer-tasks-completed")) {
                player.setSlayerTasksCompleted(reader.get("slayer-tasks-completed").getAsInt());
            }
            if (reader.has("slayer-master")) {
                player.getSlayer().setMaster(reader.get("slayer-master").getAsInt());
            }
            if (reader.has("consecutive-task")) {
                player.getSlayer().setConsecutiveTasks(reader.get("consecutive-task").getAsInt());
            }
            if (reader.has("slayer-points")) {
                player.getSlayer().setPoints(reader.get("slayer-points").getAsInt());
            }
            
            if (reader.has("slayer-unlocks")) {
                String[] unlocks = reader.get("slayer-unlocks").getAsString().split(":");
                for (int i = 0; i < unlocks.length; i++) {
                	Unlocks unlock = (Unlocks)Unlocks.valueOf(unlocks[i]);
                	player.getSlayer().addUnlock(unlock);
                }
            }
            
            if (reader.has("slayer-extends")) {
                String[] extendsA = reader.get("slayer-extends").getAsString().split(":");
                for (int i = 0; i < extendsA.length; i++) {
                	Extends extend = (Extends)Extends.valueOf(extendsA[i]);
                	player.getSlayer().addExtend(extend);
                }
            }

            if (reader.has("pouches")) {
                player.setPouches(builder.fromJson(reader.get("pouches").getAsJsonArray(), PouchContainer[].class));
            }

            if (reader.has("inventory")) {
                player.getInventory().setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
            }

            if (reader.has("equipment")) {
                player.getEquipment().setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
            }

            if (reader.has("appearance")) {
                player.getAppearance().set(builder.fromJson(
                        reader.get("appearance").getAsJsonArray(), int[].class));
            }

            if (reader.has("skills")) {
                player.getSkillManager().setSkills(builder.fromJson(
                        reader.get("skills"), Skills.class));
            }

            if (reader.has("quick-prayers")) {
                player.getQuickPrayers().setPrayers(builder.fromJson(
                        reader.get("quick-prayers"), PrayerData[].class));
            }

            if (reader.has("friends")) {
                long[] friends = builder.fromJson(
                        reader.get("friends").getAsJsonArray(), long[].class);

                for (long l : friends) {
                    player.getRelations().getFriendList().add(l);
                }
            }

            if (reader.has("ignores")) {
                long[] ignores = builder.fromJson(
                        reader.get("ignores").getAsJsonArray(), long[].class);

                for (long l : ignores) {
                    player.getRelations().getIgnoreList().add(l);
                }
            }
            
            
            /** DIARIES **/
            if (reader.has("DIARIES")) {
                Map<String, Integer> diaryMap = new HashMap<>();
            	JsonObject diaries = reader.get("DIARIES").getAsJsonObject();
            	for (int i = 0; i < DiaryRegions.values().length; i++) {
            		String name = DiaryRegions.values()[i].name();
            		if (diaries.has(name)) {
            			JsonObject tasks = diaries.get(name).getAsJsonObject();
            			tasks.entrySet().stream().forEach(e -> {
                			diaryMap.put(e.getKey(), e.getValue().getAsInt());
            			});
            		}
            	}
                player.setDiaries(diaryMap);
            }
            

            /** BANKS **/
            for (int i = 0; i < player.getBanks().length; i++) {
                if (i == Bank.BANK_SEARCH_TAB_INDEX) {
                    continue;
                }
                if (reader.has("bank-" + i)) {
                    player.setBank(i, new Bank(player)).getBank(i).addItems(builder.fromJson(reader.get("bank-" + i).getAsJsonArray(), Item[].class), false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return LoginResponse.NORMAL.getOpcode();
        }
        return LoginResponse.NORMAL.getOpcode();
    }
}
