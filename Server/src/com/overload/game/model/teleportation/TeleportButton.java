package com.overload.game.model.teleportation;

import java.util.HashMap;
import java.util.Map;

import com.overload.game.model.Position;

/**
 * Represents a button related to teleports.
 * 
 * @author Professor Oak
 */
public enum TeleportButton {
	
	HOME_NORMAL(			0, new Position(3088, 3502, 0),	false, 19210),
	VARROCK_NORMAL(			0, new Position(3212, 3423, 0),	false, 1164),
	LUMBRIDGE_NORMAL(		0, new Position(3221, 3218, 0),	false, 1167),
	FALADOR_NORMAL(			0, new Position(2965, 3380, 0),	false, 1170),
	CAMELOT_NORMAL(			0, new Position(2757, 3478, 0),	false, 1174),
	ARDOUGNE_NORMAL(		0, new Position(2662, 3307, 0),	false, 1540),
	WATCHTOWER_NORMAL(		0, new Position(2728, 3349, 0),	false, 1541),
	TROLLHEIM_NORMAL(		0, new Position(2890, 3676, 0),	false, 7455),
	APEATOLL_NORMAL(		0, new Position(2662, 3307, 0),	false, 18470),
	KOUREND_NORMAL(			0, new Position(2662, 3307, 0),	false, 1188),
	
	HOME_ANCIENTS0	(		0, new Position(3088, 3502, 0),	false, 21741),
	PADDEWWA_ANCIENTS(		0, new Position(3212, 3423, 0),	false, 13035),
	SENNITISTEN_ANCIENTS(	0, new Position(3221, 3218, 0),	false, 13045),
	KHARYRLL_ANCIENTS(		0, new Position(3221, 3218, 0),	false, 13053),
	LASSAR_ANCIENTS(		0, new Position(2965, 3380, 0),	false, 13061),
	DAREEYAK_ANCIENTS(		0, new Position(2757, 3478, 0),	false, 13069),
	CARRALLANGAR_ANCIENTS(	0, new Position(2890, 3676, 0),	false, 13079),
	ANNAKARL_ANCIENTS(		0, new Position(2662, 3307, 0),	false, 13087),
	GHORROCK_ANCIENTS(		0, new Position(2728, 3349, 0),	false, 13095),
	
	HOME_LUNAR(				0, new Position(3088, 3502, 0),	false, 62000),
	MOONCLAN_LUNAR(			0, new Position(3212, 3423, 0),	false, 62104),
	OURANIA_LUNAR(			0, new Position(3221, 3218, 0),	false, 62143),
	WATERBIRTH_LUNAR(		0, new Position(2965, 3380, 0),	false, 62169),
	BARBARIAN_LUNAR(		0, new Position(3221, 3218, 0),	false, 62221),
	FISHING_GUILD_LUNAR(	0, new Position(2757, 3478, 0),	false, 62390),
	CATHERBY_LUNAR(			0, new Position(2890, 3676, 0),	false, 62442),
	ICE_PLATEAU_LUNAR(		0, new Position(2662, 3307, 0),	false, 62481),

	HOME_ARCEUUS(			0, new Position(3088, 3502, 0),	false, 63000),
	LUMBRIDGEGRAVE_ARCEUUS(	0, new Position(2662, 3307, 0),	false, 63026),
	DRAYNORMANOR_ARCEUUS(	0, new Position(2662, 3307, 0),	false, 63078),
	MINDALTAR_ARCEUUS(		0, new Position(2662, 3307, 0),	false, 63143),
	RESPAWN_ARCEUUS(		0, new Position(1629, 3743, 0),	false, 63169),
	SALVEGRAVE_ARCEUUS(		0, new Position(2662, 3307, 0),	false, 63195),
	FENKENSTRAINS_ARCEUUS(	0, new Position(2662, 3307, 0),	false, 63247),
	WESTARDOUGNE_ARCEUUS(	0, new Position(2662, 3307, 0),	false, 63286),
	HARMONYISLAND_ARCEUUS(	0, new Position(2662, 3307, 0),	false, 63325),
	CEMETERY_ARCEUUS(		0, new Position(2662, 3307, 0),	false, 63351),
	BARROWS_ARCEUUS(		0, new Position(2662, 3307, 0),	false, 63403),
	APEATOLL_ARCEUUS(		0, new Position(2662, 3307, 0),	false, 63429),
	
	;

	public final int menu;
	private final Position destination;
	private final boolean dangerous;
	public final int[] ids;

	TeleportButton(int menu, Position destination, boolean dangerous, int... ids) {
		this.menu = menu;
		this.destination = destination;
		this.dangerous = dangerous;
		this.ids = ids;
	}

	public static final Map<Integer, TeleportButton> teleports = new HashMap<>();
	static {
		for (TeleportButton b : TeleportButton.values()) {
			for (int i : b.ids) {
				teleports.put(i, b);
			}
		}
	}

	public static TeleportButton get(int buttonId) {
		return teleports.get(buttonId);
	}

	public Position getDestination() {
		return destination;
	}
	
	public boolean isDangerous() {
		return dangerous;
	}
}
