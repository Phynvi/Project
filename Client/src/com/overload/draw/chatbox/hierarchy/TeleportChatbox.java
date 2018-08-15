package com.overload.draw.chatbox.hierarchy;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import com.overload.Client;

public class TeleportChatbox extends HierarchyChatboxDrawing {

	public static void open(int index) {
		if (index < 0 || index >= HIERARCHY_OPTIONS.length) {
			INSTANCE.setSelectedHierarchy(null);
			Client.chatboxDrawing = INSTANCE;
			return;
		}
		INSTANCE.setSelectedHierarchy(HIERARCHY_OPTIONS[index]);
		Client.chatboxDrawing = INSTANCE;
	}
	
	@Override
	public void draw(int chatboxWidth, int chatboxHeight, int offsetY) {
		super.draw(chatboxWidth, chatboxHeight, offsetY);
		
		Client.instance.newRegularFont.drawBasicString("Teleportation" 
				+ (getSelectedHierarchy() != null ? " -> " + getSelectedHierarchy().getName() : ""),
				28, 21 + offsetY, Client.instance.isFixed() ? 0x000080 : 0x778899,
				Client.instance.isFixed() ? -1 : 0);
	}

	@Override
	public HierarchyOption[] getOptions() {
		return HIERARCHY_OPTIONS;
	}
	
	private static final int SUPER_WIDTH = 100;
	
	private static final int SUPER_HEIGHT = 13;
	
	private static final String LABEL_COLOR = "<col=778899>";
	
	private static final String NEW_LINE_DELIMITER = " <n>";
	
	private static final TeleportChatbox INSTANCE = new TeleportChatbox();
	
	private static final HierarchyOption[] HIERARCHY_OPTIONS = {
		new TrainingTeleport(),
		new DungeonTeleport(),
		new BossTeleport(),
		new PvPTeleport(),
		new CityTeleport(),
		new MinigameTeleport(),
		new SkillingTeleport(),
	};

	private static final class TrainingTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "Training";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_T;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 0;
		
		private enum Option implements HierarchyOption {
			
			ROCK_CRAB(KeyEvent.VK_R, "Rock Crabs",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Rock crabs are one of the most used methods "
					+ "for training. These npcs are not only weak, "
					+ "they are also aggressive towards players."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>13"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>XP Training"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>AFK Training, Multi-combat"),
			
			YAKS(KeyEvent.VK_Y, "Yaks",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Yaks are a great way of training and also a great"
					+ " source of income. These npcs drop yak-hide which can be"
					+ " used to train Crafting or selling to Sigmund for a good amount"
					+ " of cash."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>22"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>XP Training and Cash"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>None"),
					
			DESERT_BANDITS(KeyEvent.VK_B, "Desert Bandits",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Desert bandits are a great source of "
					+ "training due to them being aggressive "
					+ "while wearing saradomin or zamorak "
					+ 	"armor or weapons."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>57-74"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>XP Training"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>AFK Training, Multi-combat"),
			
			GORILLAS(KeyEvent.VK_G, "Gorillas",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Gorillas are located near ape atoll. "
					+ "These npcs are great if you wish "
					+ "to train on monsters with high hp."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>167"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>XP Training"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>Nearby Altar, Low Defence"),
			
			EXPERIMENTS(KeyEvent.VK_E, "Experiments",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Experiments are the best choice for "
					+ "those who will be paying attention as "
					+ "these monsters drop potions quite often."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>51"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>XP Training"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>Weak, Potion Drops"),
			
			FIRE_GIANTS(KeyEvent.VK_F, "Fire Giants",
					LABEL_COLOR + "Description:</col> " + NEW_LINE_DELIMITER
					+ "Fire Giants are a great source of income for new players "
					+ "as they drops rune and dragon armor/weapons quite frequently."
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Level: </col>86"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Type: </col>Money Making"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Attributes: </col>None"),
			
			;
			
			private Option(int shortcutKey, String name, String description) {
				this.shortcutKey = shortcutKey;
				this.name = name;
				this.description = description;
			}
						
			private final int shortcutKey;
			
			private final String name;
			
			private final String description;

			@Override
			public Dimension getDimension() {
				return new Dimension(125, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class DungeonTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "Dungeon";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_D;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 1;
		
		private enum Option implements HierarchyOption {
			
			BRIMHAVEN_DUNGEON(KeyEvent.VK_B, "Brimhaven Dungeon",
					LABEL_COLOR + "This dungeon contains the following: </col>"
					+ NEW_LINE_DELIMITER + "- Red, Bronze, Iron and Steel Dragons"
					+ NEW_LINE_DELIMITER + "- Wild Dogs"
					+ NEW_LINE_DELIMITER + "- Black Demons"),
			
			TAVERLEY_DUNGEON(KeyEvent.VK_V, "Taverley Dungeon",
					LABEL_COLOR + "This dungeon contains the following: </col>"
					+ NEW_LINE_DELIMITER + "- Blue and Black Dragons"
					+ NEW_LINE_DELIMITER + "- Dark Beasts" 
					+ NEW_LINE_DELIMITER + "- Hellhounds"
					+ NEW_LINE_DELIMITER + "- Black Demons"
					+ NEW_LINE_DELIMITER + "- Poison Spiders"
					+ NEW_LINE_DELIMITER + "- Giant Bats"),
			
			DUNGEON_OF_ABYSS(KeyEvent.VK_A, "Dungeon of Abyss",
					LABEL_COLOR + "This dungeon contains the following: </col>"
					+ NEW_LINE_DELIMITER + "- Aquanites"),
			;
						
			private Option(int shortcutKey, String name,
					String description) {
				this.shortcutKey = shortcutKey;
				this.name = name;
				this.description = description;
			}
						
			private final int shortcutKey;
			
			private final String name;

			private final String description;
			
			@Override
			public Dimension getDimension() {
				return new Dimension(140, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class BossTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "Boss";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_B;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 2;
		
		private enum Option implements HierarchyOption {
			
			CORPOREAL_BEAST(KeyEvent.VK_C, "Corporeal Beast",
					LABEL_COLOR + "Level: </col>785"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Best Drop(s): </col>Spirit shields, Sigils "
					+ "and Pet corporeal beast"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Prayers: </col>Melee and Magic"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Notes: </col>All weapons have a 30% damage"
					+ " reduction against this monster. This "
					+ "reduction can be avoided by using Statius' "
					+ "Warhammer's special or using a Zamorakian "
					+ "Spear which has a 30% damage boost"),
			
			GOD_WARS_DUNGEON(KeyEvent.VK_W, "God Wars Dungeon",
					LABEL_COLOR + "Level: </col>Bandos (624) - Zamorak (650)"
					+ NEW_LINE_DELIMITER + "Saradomin (596) - Armadyl (580)"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Best Drop(s): </col>God's armor, shards, hilts "
					+ "and corresponding pet"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Prayers: </col>Bandos (Melee) - Zamorak (Melee)"
					+ " - Saradomin (Magic) - Armadyl (Ranged)"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Notes: </col>You must get a killcount of 10"
					+ " for the respective boss before entering"
					+ " their rooms. These bosses can be quite"
					+ " difficult and a team is suggested."),
			
			DAGANNOTH_KINGS(KeyEvent.VK_D, "Dagannoth Kings",
					LABEL_COLOR + "Level: </col>303"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Best Drop(s): </col>Seer rings and Dragon Hatchet"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Prayers: </col>Magic, Ranged and Melee"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Notes: </col>You will need to defeat all 3 dagannoth"
					+ " kings. Supreme is vulnerable to Melee, Rex is vulnerable to Magic and"
					+ " Prime is vulnerable to Ranged."
					+ " A Balmung can be used against all 3 dagannoths."),
			
			KING_BLACK_DRAGON(KeyEvent.VK_K, "King Black Dragon",
					LABEL_COLOR + "Level: </col>276"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Best Drop(s): </col>Visage, Dragon Platelegs, Dragon Full Helmet, "
					+ "Ornament kits and Prince black dragon"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Prayers: </col>Magic"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Notes: </col>None"),
					
			SEA_TROLL_QUEEN(KeyEvent.VK_Q, "Sea Troll Queen",
					LABEL_COLOR + "Level: </col>170"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Best Drop(s): </col>Abyssal tentacle and Pet kraken"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Prayers: </col>Magic and Ranged"
					+ NEW_LINE_DELIMITER + LABEL_COLOR + "Notes: </col>You cannot melee this npc."),

			;
						
			private Option(int shortcutKey, String name, String description) {
				this.shortcutKey = shortcutKey;
				this.name = name;
				this.description = description;
			}
						
			private final int shortcutKey;
			
			private final String name;

			private String description;
			
			@Override
			public Dimension getDimension() {
				return new Dimension(160, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class PvPTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "PvP";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_P;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 3;
		
		private enum Option implements HierarchyOption {
			
			WEST_DRAGONS(KeyEvent.VK_W, "W. Green Dragons"),
			
			EAST_DRAGONS(KeyEvent.VK_E, "E. Green Dragons"),	
			
			EDGEVILLE_DITCH(KeyEvent.VK_D, "Edgeville Ditch"),
			
			MAGE_BANK(KeyEvent.VK_M, "Mage Bank"),			
			;
						
			private Option(int shortcutKey, String name) {
				this.shortcutKey = shortcutKey;
				this.name = name;
			}
						
			private final int shortcutKey;
			
			private final String name;

			@Override
			public Dimension getDimension() {
				return new Dimension(118, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class CityTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "City";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_C;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 4;
		
		private enum Option implements HierarchyOption {
			
			AL_KHARID(KeyEvent.VK_K, "Al-Kharid"),
			
			ARDOUGNE(KeyEvent.VK_A, "Ardougne"),
			
			CAMELOT(KeyEvent.VK_C, "Camelot"),
			
			DRAYNOR(KeyEvent.VK_D, "Draynor"),
			
			EDGEVILLE(KeyEvent.VK_E, "Edgeville"),
			
			FALADOR(KeyEvent.VK_F, "Falador"),
			
			KARAMJA(KeyEvent.VK_J, "Karamja"),
			
			LUMBRIDGE(KeyEvent.VK_L, "Lumbridge"),
			
			POLLNIVNEACH(KeyEvent.VK_P, "Pollnivneach"),
			
			RELLEKA(KeyEvent.VK_R, "Relleka"),
			
			RIMMINGTON(KeyEvent.VK_I, "Rimmington"),
															
			TAVERLEY(KeyEvent.VK_T, "Taverley"),
			
			VARROCK(KeyEvent.VK_V, "Varrock"),
			
			YANILLE(KeyEvent.VK_Y, "Yanille"),
			;
						
			private Option(int shortcutKey, String name) {
				this.shortcutKey = shortcutKey;
				this.name = name;
			}
						
			private final int shortcutKey;
			
			private final String name;

			@Override
			public Dimension getDimension() {
				return new Dimension(88, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class MinigameTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "Minigame";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_M;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 5;
		
		private enum Option implements HierarchyOption {
			
			BARROWS(KeyEvent.VK_B, "Barrows", 
					LABEL_COLOR + "Rewards: </col>                                                      "
					+ NEW_LINE_DELIMITER + "- Barrows' Armor"),
					
			CLAN_WARS(KeyEvent.VK_C, "Clan Wars"),
			
			DUEL_ARENA(KeyEvent.VK_D, "Duel Arena"),
			
			FIGHT_CAVE(KeyEvent.VK_F, "Fight Cave", 
					LABEL_COLOR + "Rewards: </col>        "
					+ NEW_LINE_DELIMITER + "- Fire Cape"
					+ NEW_LINE_DELIMITER + "- Tokkul Cape"
					+ NEW_LINE_DELIMITER + "- TokHaar-Kal (tokkul shop)"),
			
			PEST_CONTROL(KeyEvent.VK_P, "Pest Control", 
					LABEL_COLOR + "Rewards: </col>        "
					+ NEW_LINE_DELIMITER + "- Void Armor"
					+ NEW_LINE_DELIMITER + "- Elite Void Armor"
					+ NEW_LINE_DELIMITER + "- Experience"
					+ NEW_LINE_DELIMITER + "- Supplies"),
					
			WARRIORS_GUILD(KeyEvent.VK_W, "Warrior's Guild", 
					LABEL_COLOR + "Rewards: </col>        "
					+ NEW_LINE_DELIMITER + "- Defenders (Bronze-Dragon)"
					+ NEW_LINE_DELIMITER + "- Cyclossus Helmet (Cyclossus Drop)"),

						
			;
						
			private Option(int shortcutKey, String name,
					String description) {
				this.shortcutKey = shortcutKey;
				this.name = name;
				this.description = description;
			}
			
			private Option(int shortcutKey, String name) {
				this(shortcutKey, name, null);
			}
						
			private final int shortcutKey;
			
			private final String name;

			private final String description;
			
			@Override
			public Dimension getDimension() {
				return new Dimension(110, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public int[] getIndex() {
				return new int[] { SUPER_INDEX, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return null;
			}
		}
	}
	
	private static final class SkillingTeleport extends ParentHierarchyOption {
		
		@Override
		public Dimension getDimension() {
			return new Dimension(SUPER_WIDTH, SUPER_HEIGHT);
		}
		
		@Override
		public String getName() {
			return "Skilling";
		}
		
		@Override
		public int getShortcutKey() {
			return KeyEvent.VK_S;
		}

		@Override
		public HierarchyOption[] getOptions() {
			return Option.values();
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
		private static final int SUPER_INDEX = 6;
				
		private enum Option implements HierarchyOption {
			
			AGILITY(0, KeyEvent.VK_A, "Agility", AGILITY_OPTIONS),
			
			COOKING(1, KeyEvent.VK_C, "Cooking", COOKING_OPTIONS),
			
			CRAFTING(2, KeyEvent.VK_T, "Crafting", CRAFTING_OPTIONS),
			
			FISHING(3, KeyEvent.VK_F, "Fishing", FISHING_OPTIONS),
			
			HUNTER(4, KeyEvent.VK_U, "Hunter", HUNTER_OPTIONS),
			
			MINING(5, KeyEvent.VK_M, "Mining", MINING_OPTIONS),
			
			SLAYER(6, KeyEvent.VK_S, "Slayer", SLAYER_OPTIONS),
			
			SMITHING(7, KeyEvent.VK_O, "Smithing", SMITHING_OPTIONS),
			
			THIEVING(8, KeyEvent.VK_I, "Thieving", THIEVING_OPTIONS),
						
			WOODCUTTING(9, KeyEvent.VK_W, "Woodcutting", WOODCUTTING_OPTIONS),

            FARMING(10, KeyEvent.VK_V, "Farming", FARMING_OPTIONS),
		
			;
						
			private Option(int index, int shortcutKey, String name,
					HierarchyOption... options) {
				this.index = SUPER_INDEX + index;
				this.shortcutKey = shortcutKey;
				this.name = name;
				this.options = options;
			}
			
			private final int index;
						
			private final int shortcutKey;
			
			private final String name;
			
			private final HierarchyOption[] options;

			@Override
			public Dimension getDimension() {
				return new Dimension(100, 13);
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public int getShortcutKey() {
				return shortcutKey;
			}
			
			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public int[] getIndex() {
				return options != null ? null : new int[] { index, ordinal() };
			}

			@Override
			public HierarchyOption[] getOptions() {
				return options;
			}
		}
		
		private static final Dimension MINING_DIMENSION = new Dimension(115, 13);
		
		private static final HierarchyOption[] MINING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return MINING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Varrock Mine";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_V;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Tin & Copper Ore (Level 1)"
							+ NEW_LINE_DELIMITER + "- Iron Ore (Level 15)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.MINING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return MINING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Falador Mine";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Tin & Copper Ore (Level 1)"
							+ NEW_LINE_DELIMITER + "- Iron Ore (Level 15)"
							+ NEW_LINE_DELIMITER + "- Coal (Level 30)"
							+ NEW_LINE_DELIMITER + "- Gold Ore (Level 40)"
							+ NEW_LINE_DELIMITER + "- Mithril Ore (Level 55)"
							+ NEW_LINE_DELIMITER + "- Adamantite Ore (Level 70)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.MINING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return MINING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Al-Kharid Mine";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_A;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Tin & Copper Ore (Level 1)"
							+ NEW_LINE_DELIMITER + "- Iron Ore (Level 15)"
							+ NEW_LINE_DELIMITER + "- Silver Ore (Level 20)"
							+ NEW_LINE_DELIMITER + "- Coal (Level 30)"
							+ NEW_LINE_DELIMITER + "- Gold Ore (Level 40)"
							+ NEW_LINE_DELIMITER + "- Mithril Ore (Level 55)"
							+ NEW_LINE_DELIMITER + "- Adamantite Ore (Level 70)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.MINING.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return MINING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Hero's Guild Mine";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_H;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>            "
							+ NEW_LINE_DELIMITER + "- Coal (Level 30)"
							+ NEW_LINE_DELIMITER + "- Runite Ore (Level 85)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.MINING.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return MINING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Essence Mine";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_E;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>                               "
							+ NEW_LINE_DELIMITER + "- Pure Essence (Level 40)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.MINING.index, 4 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};
		
		private static final Dimension FISHING_DIMENSION = new Dimension(110, 13);
		
		private static final HierarchyOption[] FISHING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FISHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Fishing Guild";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_G;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Shrimps (Level 1)"
							+ NEW_LINE_DELIMITER + "- Anchovies (Level 15)"
							+ NEW_LINE_DELIMITER + "- Trout (Level 20)"
							+ NEW_LINE_DELIMITER + "- Pike (Level 25)"
							+ NEW_LINE_DELIMITER + "- Tuna (Level 35)"
							+ NEW_LINE_DELIMITER + "- Lobster (Level 40)"
							+ NEW_LINE_DELIMITER + "- Swordfish (Level 50)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.FISHING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FISHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Karamja Port";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_K;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Pike (Level 25)"
							+ NEW_LINE_DELIMITER + "- Tuna (Level 35)"
							+ NEW_LINE_DELIMITER + "- Lobster (Level 40)"
							+ NEW_LINE_DELIMITER + "- Swordfish (Level 50)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.FISHING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FISHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Catherby Shore";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_C;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Shrimps (Level 1)"
							+ NEW_LINE_DELIMITER + "- Pike (Level 25)"
							+ NEW_LINE_DELIMITER + "- Tuna (Level 35)"
							+ NEW_LINE_DELIMITER + "- Lobster (Level 40)"
							+ NEW_LINE_DELIMITER + "- Swordfish (Level 50)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.FISHING.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FISHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Fremmenik Dock";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Cavefish (Level 85)"
							+ NEW_LINE_DELIMITER + "- Rocktail (Level 90)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.FISHING.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};
		
		private static final Dimension WOODCUTTING_DIMENSION = new Dimension(140, 13);
		
		private static final HierarchyOption[] WOODCUTTING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return WOODCUTTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Lumbridge";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_L;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Normal Tree (Level 1)"
							+ NEW_LINE_DELIMITER + "- Oak Tree (Level 15)"
							+ NEW_LINE_DELIMITER + "- Yew Tree (Level 60)"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.WOODCUTTING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return WOODCUTTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Draynor";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_D;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Normal Tree (Level 1)"
							+ NEW_LINE_DELIMITER + "- Willow Tree (Level 30)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.WOODCUTTING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return WOODCUTTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Seers' Village";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_S;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Normal Tree (Level 1)"
							+ NEW_LINE_DELIMITER + "- Oak Tree (Level 15)"
							+ NEW_LINE_DELIMITER + "- Maple Tree (Level 45)"
							+ NEW_LINE_DELIMITER + "- Yew Tree (Level 60)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.WOODCUTTING.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return WOODCUTTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Seers' Village (Magic)";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_M;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Resources: </col>"
							+ NEW_LINE_DELIMITER + "- Normal Tree (Level 1)"
							+ NEW_LINE_DELIMITER + "- Oak Tree (Level 15)"
							+ NEW_LINE_DELIMITER + "- Magic Tree (Level 75)"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.WOODCUTTING.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};
		
		private static final Dimension AGILITY_DIMENSION = new Dimension(120, 13);
		
		private static final HierarchyOption[] AGILITY_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return AGILITY_DIMENSION;
				}

				@Override
				public String getName() {
					return "Gnome Course";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_G;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Level: </col> 1                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Lap Completion: </col>gold and experience"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.AGILITY.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return AGILITY_DIMENSION;
				}

				@Override
				public String getName() {
					return "Penguin Course";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_P;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Level: </col> 30                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Lap Completion: </col>gold and experience"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.AGILITY.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return AGILITY_DIMENSION;
				}

				@Override
				public String getName() {
					return "Barbarian Course";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_B;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Level: </col> 35                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Lap Completion: </col>gold and experience"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.AGILITY.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return AGILITY_DIMENSION;
				}

				@Override
				public String getName() {
					return "Agility Pyramid";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_A;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Level: </col> 50                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Lap Completion: </col>gold and experience"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.AGILITY.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return AGILITY_DIMENSION;
				}

				@Override
				public String getName() {
					return "Wilderness Course";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_W;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Level: </col> 50                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Lap Completion: </col>gold and experience"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.AGILITY.index, 4 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};
		
		private static final Dimension COOKING_DIMENSION = new Dimension(90, 13);
		
		private static final HierarchyOption[] COOKING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return COOKING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Rogues' Den";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_R;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Requirement: </col> None                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Perks: </col>Unlimited burning fire"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.COOKING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return COOKING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Cooks' Guild";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_G;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Requirement: </col> Level 32 & Chef's Hat                    "
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Perks: </col>Various free ingredients"
					;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.COOKING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};
		
		private static final Dimension CRAFTING_DIMENSION = new Dimension(95, 13);
		
		private static final HierarchyOption[] CRAFTING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return CRAFTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Al-Kharid";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_K;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Activity: </col> Tanning"
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Requirement: </col>"
							+ NEW_LINE_DELIMITER + "- Cowhide, Snakeskin or D'hide"
							+ NEW_LINE_DELIMITER + "- Cash"
							
							+ NEW_LINE_DELIMITER + NEW_LINE_DELIMITER + "                                                                                            "
							+ LABEL_COLOR + "Activity: </col> Smelting"
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Item Requirement: </col>"
							+ NEW_LINE_DELIMITER + "- Ores (Metal Bar Smelting)"
							+ NEW_LINE_DELIMITER + "- Gold Bars (Jewellery Crafting)"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.CRAFTING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return CRAFTING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Seers' Village";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_R;
				}
				
				@Override
				public String getDescription() {
					return LABEL_COLOR + "Activity: </col> Spinning"
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Materials : Product: </col>"
							+ NEW_LINE_DELIMITER + "- Wool : Ball of Wool (Level 1)"
							+ NEW_LINE_DELIMITER + "- Flax : Bow String (Level 10)"
							+ NEW_LINE_DELIMITER + "- Ball of Wool : Strip of Cloth (Level 10)"
							+ NEW_LINE_DELIMITER + "- Sinew : Crossbow String (Level 10)"
							+ NEW_LINE_DELIMITER + "- Magic Roots : Magic String (Level 19)"
							+ NEW_LINE_DELIMITER + "- Yak-hide : Rope (Level 30)"
							;
				}

				@Override
				public int[] getIndex() {
					return new int[] { Option.CRAFTING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}
		};

		private static final Dimension HUNTER_DIMENSION = new Dimension(105, 13);
		
		private static final HierarchyOption[] HUNTER_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return HUNTER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Feldip Area";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Birds (Bird Snare required): </col>             " 
							+ NEW_LINE_DELIMITER + "- Crimson Swift (Level 1) - Golden Warbler (Level 5) - Cerulean Twith (Level 11) - Tropical Wagtail (Level 19)"
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Chinchompas (Box Trap required): </col>             " 
							+ NEW_LINE_DELIMITER + "- Grey Chinchompas (Level 53) - Red Chinchompas (Level 63)"
							+ NEW_LINE_DELIMITER + LABEL_COLOR + "Kebbits (Logs and Knife required): </col>             " 
							+ NEW_LINE_DELIMITER + "- Wild Kebbit (Level 23) - Prickly Kebbit (Level 37) - Diseased Kebbit (Level 44) - Sabre Toothed Kebbit (Level 51)"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.HUNTER.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return HUNTER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Wild Camp (<col=FF0000>PvP</col>)";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_W;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Chinchompas (Box Trap required): </col>             " 
							+ NEW_LINE_DELIMITER + "- Red Chinchompas (Level 63)"
							+ NEW_LINE_DELIMITER + "- Black Chinchompas (Level 90)"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.HUNTER.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}
		};

		private static final Dimension SLAYER_DIMENSION = new Dimension(130, 13);
		
		private static final HierarchyOption[] SLAYER_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SLAYER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Slayer Tower";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_C;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Monsters:"
							+ NEW_LINE_DELIMITER + "- Crawling Hand"
							+ NEW_LINE_DELIMITER + "- Aberrant Spectres"
							+ NEW_LINE_DELIMITER + "- Banshees"
							+ NEW_LINE_DELIMITER + "- Dust Devils"
							+ NEW_LINE_DELIMITER + "- Bloodveld"
							+ NEW_LINE_DELIMITER + "- Infernal Mages"
							+ NEW_LINE_DELIMITER + "- Gargoyles"
							+ NEW_LINE_DELIMITER + "- Nechryels"
							+ NEW_LINE_DELIMITER + "- Dark Beasts"
							+ NEW_LINE_DELIMITER + "- Abyssal Demons"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SLAYER.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SLAYER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Icey Cavern";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_I;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Monsters:"
							+ NEW_LINE_DELIMITER + "- Ice Soldiers"
							+ NEW_LINE_DELIMITER + "- Ice Giants"
							+ NEW_LINE_DELIMITER + "- Ice Strykewyrm"
							+ NEW_LINE_DELIMITER + "- Frost Dragons"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SLAYER.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SLAYER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Fremmenik Dungeon";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Monsters:"
							+ NEW_LINE_DELIMITER + "- Cave Crawler"
							+ NEW_LINE_DELIMITER + "- Rock slug"
							+ NEW_LINE_DELIMITER + "- Cockatrice"
							+ NEW_LINE_DELIMITER + "- Pyrefiend"
							+ NEW_LINE_DELIMITER + "- Basilisk"
							+ NEW_LINE_DELIMITER + "- Jelly"
							+ NEW_LINE_DELIMITER + "- Turoth"
							+ NEW_LINE_DELIMITER + "- Kurask"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SLAYER.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SLAYER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Lumbridge Swamp";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_L;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Monsters:"
							+ NEW_LINE_DELIMITER + "- Cave Bug"
							+ NEW_LINE_DELIMITER + "- Cave Slime"
							+ NEW_LINE_DELIMITER + "- Cave Crawler"
							+ NEW_LINE_DELIMITER + "- Cockatrice"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SLAYER.index, 4 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SLAYER_DIMENSION;
				}

				@Override
				public String getName() {
					return "Mos Le'Harmless";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_H;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Monsters:"
							+ NEW_LINE_DELIMITER + "- Jungle Strykewyrm"
							+ NEW_LINE_DELIMITER + "- Jungle Horror"
							+ NEW_LINE_DELIMITER + "- Giant Wasp"
							+ NEW_LINE_DELIMITER + "- Giant Mosquito"
							+ NEW_LINE_DELIMITER + "- Harpie Bug Swarm"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SLAYER.index, 5 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}
		};
		
		private static final Dimension SMITHING_DIMENSION = new Dimension(70, 13);
		
		private static final HierarchyOption[] SMITHING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SMITHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Edgeville";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_E;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Description: </col>This smithing house is located near Edgeville's bank and contains an anvil and a furnace."
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SMITHING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return SMITHING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Varrock";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_V;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Description: </col>This smithing house is located near Varrock's West Bank and contains anvils."
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.SMITHING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}
		};
		
		private static final Dimension THIEVING_DIMENSION = new Dimension(105, 13);
		
		private static final HierarchyOption[] THIEVING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return THIEVING_DIMENSION;
				}

				@Override
				public String getName() {
					return "General Stalls";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_G;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Description: </col>These stalls are great for starters to make money as you get gp everytime you steal from them. You will also receive a respective type of item, which you can sell to Merchant Zakia for extra cash."
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.THIEVING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return THIEVING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Ardougne";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_A;
				}

				@Override
				public String getDescription() {
					return null;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.THIEVING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}
			
		};
		
		private static final Dimension FARMING_DIMENSION = new Dimension(105, 13);
		
		private static final HierarchyOption[] FARMING_OPTIONS = {
			new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Falador";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 2 Vegetable Patches"
							+ NEW_LINE_DELIMITER + "- 1 Herb Patch"
							+ NEW_LINE_DELIMITER + "- 1 Flower Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 0 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Catherby";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_C;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 2 Vegetable Patches"
							+ NEW_LINE_DELIMITER + "- 1 Herb Patch"
							+ NEW_LINE_DELIMITER + "- 1 Flower Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 1 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Ardougne";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_A;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 2 Vegetable Patches"
							+ NEW_LINE_DELIMITER + "- 1 Herb Patch"
							+ NEW_LINE_DELIMITER + "- 1 Flower Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 2 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Canifis";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_F;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 2 Vegetable Patches"
							+ NEW_LINE_DELIMITER + "- 1 Herb Patch"
							+ NEW_LINE_DELIMITER + "- 1 Flower Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 3 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "Rimmington";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_R;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 1 Bush Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 4 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
				
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "South Varrock";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_V;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 1 Bush Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 5 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}, new HierarchyOption() {

				@Override
				public Dimension getDimension() {
					return FARMING_DIMENSION;
				}

				@Override
				public String getName() {
					return "South Ardougne";
				}

				@Override
				public int getShortcutKey() {
					return KeyEvent.VK_S;
				}

				@Override
				public String getDescription() {
					return LABEL_COLOR + "Patches: </col>"
							+ NEW_LINE_DELIMITER + "- 1 Bush Patch"
					;
				}
				
				@Override
				public int[] getIndex() {
					return new int[] { Option.FARMING.index, 6 };
				}

				@Override
				public HierarchyOption[] getOptions() {
					return null;
				}
			}
		};
	}
}