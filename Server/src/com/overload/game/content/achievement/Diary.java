package com.overload.game.content.achievement;

import java.util.ArrayList;
import java.util.List;

import com.overload.game.content.Requirement;
import com.overload.game.content.quest.Quest;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.game.model.Skill;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;
import com.overload.util.NpcIdentifiers;
import com.overload.util.ObjectIdentifiers;

public abstract class Diary {
	
	public enum DiaryType {
		OBJECT,
		NPC,
		ITEM
	};
	
	public enum Difficulty {
		EASY,
		MEDIUM,
		HARD,
		ELITE
	};
	
	public enum OptionType {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		FIFTH,
		ATTACK,
		MAGE,
		PICKUP,
		USE_ON_ITEM,
		USE_ON_OBJECT,
		USE_ON_GROUND,
		USE_ON_NPC
	};
	
    private final DiaryRegions regions;

    public Diary(DiaryRegions regions) {
        this.regions = regions;
    }

    public abstract void process(Player player);
    
    public abstract void progress(Player player, Tasks task);
    
    public void checkCompleted(Player player, Tasks task) {
    	if (player.getDiaryAmount(task.name()) == task.getAmount()) {
    		String difficulty = task.getDifficulty().name();
    		player.sendMessage("<col=006400>Congratulations! You have completed " + Misc.anOrA(difficulty) + " " + Misc.capitalizeWords(difficulty.toLowerCase()) + " task in " + Misc.capitalizeWords(task.getDiaryLocation().name().toLowerCase()));
    	}
    }
    
    public DiaryRegions getRegions() {
        return regions;
    }
    
    public enum Tasks {
		/** VARROCK **/
		BROWSE_THESSALIAS_STORE(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.NPC,
				OptionType.SECOND,
				1,
				null,
				null,
				null,
                false,
                NpcIdentifiers.THESSALIA
		),
		TELEPORT_TO_ESSENCE_MINE(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.NPC,
				OptionType.THIRD,
				1,
				null,
				null,
				null,
                false,
                NpcIdentifiers.AUBURY
		),
		MINE_SOME_IRON(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.OBJECT,
				OptionType.FIRST,
				1,
				null,
				null,
				null,
                false,
                ObjectIdentifiers.ROCKS_49
		),
		MAKE_A_PLANK(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.NPC,
				OptionType.SECOND,
				1,
				null,
				null,
				null,
                false,
                NpcIdentifiers.SAWMILL_OPERATOR
		),
		STRONGHOLD_OF_SECURITY(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.OBJECT,
				OptionType.FIRST,
				1,
				null,
				null,
				null,
                false,
                -1
		),
		JUMP_THE_FENCE(
				DiaryRegions.VARROCK,
				Difficulty.EASY,
				DiaryType.OBJECT,
				OptionType.FIRST,
				1,
				null,
				null,
				null,
                false,
                ObjectIdentifiers.FENCE_3
		),
        LUMBER_YARD_DYING_TREE(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.OBJECT,
                OptionType.FIRST,
                1,
                null,
                null,
                null,
                false,
                -1
        ),
        NEWSPAPER(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.NPC,
                OptionType.FIRST,
                1,
                null,
                null,
                new Item[] {new Item(995, 50)},
                true,
                NpcIdentifiers.BENNY
        ),
        GIVE_A_DOG_A_BONE(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.ITEM,
                OptionType.USE_ON_NPC,
                1,
                null,
                null,
                new Item[] {new Item(526, 1)},
                true,
                ItemIdentifiers.BONES,
                NpcIdentifiers.STRAY_DOG
        ),
        FIRE_A_BOWL_IN_BARBARIAN_VILLAGE(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.ITEM,
                OptionType.USE_ON_OBJECT,
                1,
                null,
                new Requirement[] {new Requirement(Skill.CRAFTING, 8)},
                null,
                false,
                ItemIdentifiers.UNFIRED_BOWL,
                ObjectIdentifiers.POTTERY_OVEN_2
        ),
        CRAFT_EARTH_RUNES(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.OBJECT,
                OptionType.USE_ON_OBJECT,
                1,
                null,
                null,
                null,
                false,
                ObjectIdentifiers.ALTAR_33
        ),
        START_FISHING(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.NPC,
                OptionType.FIRST,
                1,
                null,
                null,
                new Item[] {new Item(ItemIdentifiers.FEATHER), new Item(ItemIdentifiers.FLY_FISHING_ROD)},
                false,
                NpcIdentifiers.FISHING_SPOT_26

        ),
        STEAL_FROM_A_STALL(
                DiaryRegions.VARROCK,
                Difficulty.EASY,
                DiaryType.OBJECT,
                OptionType.SECOND,
                1,
                null,
                new Requirement[] {new Requirement(Skill.THIEVING, 5)},
                null,
                false,
                ObjectIdentifiers.TEA_STALL

        ),
        /*
        Medium
         */
        GET_A_FREE_POTION(
                DiaryRegions.VARROCK,
                Difficulty.MEDIUM,
                DiaryType.NPC,
                OptionType.FIRST,
                1,
                null,
                null,
                null,
                false,
                NpcIdentifiers.APOTHECARY

        ),
        ENTER_THE_CHAMPIONS_GUILD(
                DiaryRegions.VARROCK,
                Difficulty.MEDIUM,
                DiaryType.OBJECT,
                OptionType.FIRST,
                1,
                null,
                null,
                null,
                false,
                ObjectIdentifiers.DOOR_45

        ),
        PICKPOCKET_A_GUARD(
                DiaryRegions.VARROCK,
                Difficulty.MEDIUM,
                DiaryType.NPC,
                OptionType.SECOND,
                1,
                null,
                new Requirement[] {new Requirement(Skill.THIEVING, 40)},
                null,
                false,
                NpcIdentifiers.GUARD_36

        ),
        CAST_VARROCK_TELEPORT(
                DiaryRegions.VARROCK,
                Difficulty.MEDIUM,
                DiaryType.NPC, // will be button
                OptionType.FIRST,
                1,
                null,
                new Requirement[] {new Requirement(Skill.MAGIC, 25)},
                null,
                false,
                -1

        ),;
		private DiaryRegions location;
		private Difficulty difficulty;
		private DiaryType diaryType;
		private OptionType optionType;
		private int amount;
		private Quest questReq;
		private Requirement[] skillReq;
		private Item[] itemReq;
        private boolean removeItems;
        private int[] actionId;
		
		Tasks(DiaryRegions location, Difficulty difficulty, DiaryType diaryType, OptionType optionType, int amount, Quest questReq, Requirement[] skillReq, Item[] itemReq, boolean removeItems, int... actionId) {
			this.location = location;
			this.difficulty = difficulty;
			this.diaryType = diaryType;
			this.optionType = optionType;
			this.amount = amount;
			this.questReq = questReq;
			this.skillReq = skillReq;
			this.itemReq = itemReq;
			this.removeItems = removeItems;
            this.actionId = actionId;
		}
		
		public DiaryRegions getDiaryLocation() {
			return location;
		}
		
		public Difficulty getDifficulty() {
			return difficulty;
		}
		
		public DiaryType getDiaryType() {
			return diaryType;
		}
		
		public OptionType getOptionType() {
			return optionType;
		}
		
		public int[] getActionId() {
			return actionId;
		}

		public boolean removeItems() {
		    return removeItems;
        }
		
		public int getAmount() {
			return amount;
		}
		
		public Quest getQuestReqs() {
			return questReq;
		}
		
		public Requirement[] getSkillReqs() {
			return skillReq;
		}
		
		public Item[] getItemReqs() {
			return itemReq;
		}
		
		public static List<Tasks> getByDiaryType(DiaryType type) {
			List<Tasks> tasks = new ArrayList<>();
			for (Tasks task : Tasks.values()) {
				if (type == task.getDiaryType()) {
					tasks.add(task);
				}
			}
			return tasks;
		}
		
		public static List<Tasks> getByLocation(DiaryRegions region) {
			List<Tasks> tasks = new ArrayList<>();
			for (Tasks task : Tasks.values()) {
				if (region == task.getDiaryLocation()) {
					tasks.add(task);
				}
			}
			return tasks;
		}
		
	};
}
