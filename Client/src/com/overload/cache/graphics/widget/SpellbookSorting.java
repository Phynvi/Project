package com.overload.cache.graphics.widget;

import com.overload.Client;

/**
 * Simple system for sorting spellbook interfaces.
 * 
 * @author Professor Oak
 */
public class SpellbookSorting {

	public static final int CONFIG_START = 1012;
	public static final int DEFAULT_ORDERING = 19361;
	public static final int COMBAT_ORDERING = 19363;
	public static final int TELEPORT_ORDERING = 19365;
	public static final int SKILL_ORDERING = 19367;

	// The player's currently selected ordering choice
	public static int selectedOrdering = DEFAULT_ORDERING;

	/**
	 * Checks if the spellbook should be sorted depending on the button which was
	 * pressed.
	 * 
	 * @param buttonId
	 * @return
	 */
	public static boolean sort(int buttonId) {
		for (SortableSpellbook s : SortableSpellbook.values()) {
			if (Client.tabInterfaceIDs[6] == s.interfaceId) {
				int[][] sortData = null;
				int configId = 1012;

				if (buttonId == DEFAULT_ORDERING) {
					sortData = s.defaultSort;
				} else if (buttonId == COMBAT_ORDERING) {
					sortData = s.combatSort;
					configId = 1013;
				} else if (buttonId == TELEPORT_ORDERING) {
					sortData = s.teleportSort;
					configId = 1014;
				} else if (buttonId == SKILL_ORDERING) {
					sortData = s.skillsSort;
					configId = 1015;
				}
				if (sortData != null) {
					SpellbookSorting.sort(s.interfaceId, sortData);

					// Reset other configs
					for (int j = 1012; j < 1016; j++) {
						Client.instance.settings[j] = 0;
					}

					// Update new config
					Client.instance.settings[configId] = 1;

					// Update selected ordering
					selectedOrdering = buttonId;
				} else {
					Client.instance.sendMessage("This spellbook cannot be sorted in that order.", 0, "");
					sort(DEFAULT_ORDERING);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Resorts the given parent interfaceId with the given data.
	 * 
	 * The data should contain childId aswell as the new x and y position.
	 * 
	 * @param parentInterfaceId
	 * @param data
	 */
	public static void sort(int parentInterfaceId, int[][] data) {
		Widget spellbook = Widget.interfaceCache[parentInterfaceId];
		for (int i = 0; i < spellbook.children.length; i++) {
			int childId = spellbook.children[i];
			int childX = spellbook.childX[i];
			int childY = spellbook.childY[i];

			for (int j = 0; j < data.length; j++) {
				int orderChildId = data[j][0];
				if (childId == orderChildId) {
					childX = data[j][1];
					childY = data[j][2];
					break;
				}
			}

			spellbook.childX[i] = childX;
			spellbook.childY[i] = childY;
		}
	}

	public enum SortableSpellbook {
		MODERN(1151, MODERN_SPELLBOOK_REGULAR_ORDERING, MODERN_SPELLBOOK_COMBAT_ORDERING, MODERN_SPELLBOOK_TELEPORTS_ORDERING, MODERN_SPELLBOOK_SKILL_ORDERING),
		ANCIENT(12855, ANCIENT_NO_MIASMICS_SPELLBOOK_REGULAR_ORDERING, null, null, null),
		LUNAR(29999, LUNAR_SPELLBOOK_REGULAR_ORDERING, null, null, null);

		public final int interfaceId;
		public final int[][] defaultSort, combatSort, teleportSort, skillsSort;

		SortableSpellbook(int interfaceId, int[][] defaultSort, int[][] combat, int[][] teleport, int[][] skills) {
			this.interfaceId = interfaceId;
			this.defaultSort = defaultSort;
			this.combatSort = combat;
			this.teleportSort = teleport;
			this.skillsSort = skills;
		}
	}

	private static final int[][] MODERN_SPELLBOOK_REGULAR_ORDERING = { { 19210, 14, 12 }, { 1152, 38, 12 },
			{ 1153, 62, 12 }, { 1154, 86, 12 }, { 1155, 110, 12 }, { 1156, 134, 12 }, { 1157, 159, 12 },
			{ 1158, 14, 36 }, { 1159, 39, 36 }, { 1160, 62, 36 }, { 1161, 86, 36 }, { 1572, 110, 36 },
			{ 1162, 134, 36 }, { 1163, 158, 36 }, { 1164, 14, 60 }, { 1165, 38, 60 }, { 1166, 62, 60 },
			{ 1167, 86, 60 }, { 1168, 110, 60 }, { 1169, 134, 60 }, { 1170, 159, 60 }, { 1171, 14, 84 },
			{ 1172, 38, 84 }, { 1173, 62, 84 }, { 1174, 86, 84 }, { 1175, 110, 84 }, { 1176, 134, 84 },
			{ 1539, 158, 84 }, { 1582, 14, 108 }, { 12037, 38, 108 }, { 1540, 62, 108 }, { 1177, 86, 108 },
			{ 1178, 110, 108 }, { 1179, 134, 108 }, { 1180, 158, 108 }, { 1541, 14, 132 }, { 1181, 38, 132 },
			{ 1182, 62, 132 }, { 15877, 86, 132 }, { 1190, 110, 132 }, { 1191, 134, 132 }, { 1192, 158, 132 },
			{ 7455, 14, 156 }, { 1183, 38, 156 }, { 1184, 62, 156 }, { 1185, 86, 156 }, { 1186, 109, 156 },
			{ 1542, 133, 156 }, { 1187, 156, 156 }, { 1188, 14, 181 }, { 1543, 38, 181 }, { 12425, 63, 181 },
			{ 1189, 86, 181 }, { 1592, 110, 181 }, { 1562, 134, 181 }, { 1193, 158, 181 }, { 12435, 14, 205 },
			{ 12445, 39, 205 }, { 6003, 62, 205 }, { 12455, 86, 205 }, };

	private static final int[][] MODERN_SPELLBOOK_COMBAT_ORDERING = { { 1175, 110, 84 }, { 1176, 134, 84 },
			{ 1539, 158, 84 }, { 1582, 14, 108 }, { 12037, 38, 108 }, { 1540, 62, 108 }, { 1177, 86, 108 },
			{ 1178, 110, 108 }, { 1179, 134, 108 }, { 1180, 158, 108 }, { 1541, 14, 132 }, { 1181, 38, 132 },
			{ 1182, 62, 132 }, { 15877, 86, 132 }, { 1190, 110, 132 }, { 1191, 134, 132 }, { 1192, 158, 132 },
			{ 7455, 14, 156 }, { 1183, 38, 156 }, { 1184, 62, 156 }, { 1185, 86, 156 }, { 1186, 109, 156 },
			{ 1542, 133, 156 }, { 1187, 156, 156 }, { 1188, 14, 181 }, { 1543, 38, 181 }, { 12425, 63, 181 },
			{ 1189, 86, 181 }, { 1592, 110, 181 }, { 1562, 134, 181 }, { 1193, 158, 181 }, { 12435, 14, 205 },
			{ 12445, 39, 205 }, { 6003, 62, 205 }, { 12455, 86, 205 }, };

	private static final int[][] MODERN_SPELLBOOK_TELEPORTS_ORDERING = { { 19210, 14, 12 }, { 1152, 38, 12 },
			{ 1153, 62, 12 }, { 1154, 86, 12 }, { 1155, 110, 12 }, { 1156, 134, 12 }, { 1157, 159, 12 },
			{ 1158, 14, 36 }, { 1159, 39, 36 }, { 1160, 62, 36 }, { 1161, 86, 36 }, { 1572, 110, 36 },
			{ 1162, 134, 36 }, { 1163, 158, 36 }, { 1164, 14, 60 }, { 1165, 38, 60 }, { 1166, 62, 60 },
			{ 1167, 86, 60 }, { 1168, 110, 60 }, { 1169, 134, 60 }, { 1170, 159, 60 }, { 1171, 14, 84 },
			{ 1172, 38, 84 }, { 1173, 62, 84 }, { 1174, 86, 84 }, { 1543, 38, 181 }, { 12425, 63, 181 },
			{ 1189, 86, 181 }, { 1592, 110, 181 }, { 1562, 134, 181 }, { 1193, 158, 181 }, { 12435, 14, 205 },
			{ 12445, 39, 205 }, { 6003, 62, 205 }, { 12455, 86, 205 }, };

	private static final int[][] MODERN_SPELLBOOK_SKILL_ORDERING = { { 1562, 134, 181 }, { 1193, 158, 181 }, { 12435, 14, 205 },
			{ 12445, 39, 205 }, { 6003, 62, 205 }, { 12455, 86, 205 }, };

	private static final int[][] LUNAR_SPELLBOOK_REGULAR_ORDERING = { { 19210, 11, 10 }, // Home teleport
			{ 30017, 40, 9 }, // Bake pie
			{ 30025, 71, 12 }, // Cure plant
			{ 30032, 103, 10 }, // Monster examine
			{ 30040, 135, 12 }, // NPC contact
			{ 30048, 165, 10 }, // Cure other
			{ 30056, 8, 38 }, // Humidify
			{ 30064, 39, 39 }, // Training teleport
			{ 30075, 71, 39 }, // Minigame teleport
			{ 30083, 103, 39 }, // Pvp teleport
			{ 30091, 135, 39 }, // Cure me
			{ 30099, 165, 37 }, // Hunter kit
			{ 30106, 12, 68 }, // Skill teleport
			{ 30114, 42, 68 }, // Slayer teleport
			{ 30122, 71, 68 }, // Cure group
			{ 30130, 103, 68 }, // Stat spy
			{ 30138, 135, 68 }, // Boss teleport
			{ 30146, 165, 68 }, // City teleport
			{ 30154, 14, 97 }, // Superglass make
			{ 30162, 42, 97 }, // Ourania teleport
			{ 30178, 71, 97 }, // Dream
			{ 30186, 105, 98 }, // String jewellery
			{ 30194, 138, 98 }, // Stat restore pot share
			{ 30202, 160, 98 }, // Magic imbue
			{ 30210, 12, 124 }, // Fertile soil
			{ 30218, 44, 125 }, // Boost potion share
			{ 30242, 72, 126 }, // Plank make
			{ 30282, 106, 125 }, // Energy transfer
			{ 30290, 135, 125 }, // Heal other
			{ 30298, 170, 125 }, // Vengeance other
			{ 30306, 12, 155 }, // Vengeance
			{ 30314, 41, 154 }, // Heal group
			{ 30322, 74, 154 }, // Spellbook swap
	};

	private static final int[][] ANCIENT_NO_MIASMICS_SPELLBOOK_REGULAR_ORDERING = { { 21741, 18, 8 }, // Home teleport
			{ 12939, 63, 8 }, // Smoke rush
			{ 12987, 108, 8 }, // Shadow rush
			{ 13035, 153, 8 }, // Training teleport
			{ 12901, 18, 36 }, // Blood rush
			{ 12861, 63, 36 }, // Ice rush
			{ 13045, 108, 36 }, // Mninigame teleport
			{ 12963, 153, 36 }, // Smoke burst
			{ 13011, 18, 64 }, // Shadow burst
			{ 13053, 63, 64 }, // PvP teleport
			{ 12919, 108, 64 }, // Blood burst
			{ 12881, 153, 64 }, // Ice burst
			{ 13061, 18, 92 }, // Slayer teleport
			{ 12951, 63, 92 }, // Smoke blitz
			{ 12999, 108, 92 }, // Shadow blitz
			{ 13069, 153, 92 }, // Skill teleport
			{ 12911, 18, 120 }, // Blood blitz
			{ 12871, 63, 120 }, // Ice blitz
			{ 13079, 108, 120 }, // City teleport
			{ 12975, 153, 120 }, // Smoke barrage
			{ 13023, 18, 148 }, // Shadow barrage
			{ 13087, 63, 148 }, // Boss teleport
			{ 12929, 108, 148 }, // Blood barrage
			{ 12891, 153, 148 }, // Ice barrage
	};
}
