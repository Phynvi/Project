package com.overload.game.model.rights;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.overload.util.Misc;

/**
 * The rights of a player determines their authority. Every right can be viewed with a name and a value. The value is used to separate each right from one another.
 * 
 * @author Jason MacK
 * @date January 22, 2015, 5:23:49 PM
 */

public enum Right implements Comparator<Right> {
	PLAYER(			 0, 0,   -1,  "000000",3), 
	SERVER_SUPPORT(	 1, 6,   689, "004080",35, PLAYER),
	MODERATOR(		 2, 1,   690, "919191",25, SERVER_SUPPORT), 
	ADMINISTRATOR(	 3, 2,   691, "F5FF0F",11, MODERATOR), 
	MANAGER(		 4, 5,   692, "063DCF",24, ADMINISTRATOR), 
	GAME_DEVELOPER(	 5, 4,   693, "544FBB",8, MANAGER),
	OWNER(			 6, 3,   694, "F5FF0F",4, GAME_DEVELOPER), 
	
	BRONZE_DONATOR(	 7, 7,   682, "B60818",26, PLAYER),
	IRON_DONATOR(	 8, 8,   683, "063DCF",27, BRONZE_DONATOR),
	STEEL_DONATOR(	 9, 9,   684, "118120",3, IRON_DONATOR),
	MITHRIL_DONATOR( 10, 10, 685, "9E6405",30, STEEL_DONATOR),
	ADAMANT_DONATOR( 11, 11, 686, "9E6405",31, MITHRIL_DONATOR),
	RUNITE_DONATOR(	 12, 12, 687, "9E6405",32, ADAMANT_DONATOR),
	DRACONIC_DONATOR(13, 13, 688, "9E6405",33, RUNITE_DONATOR);


	/**
	 * The position in the enum
	 */
	private final int id;
	
	/**
	 * The level of rights that define this
	 */
	private final int right;
	
	/**
	 * The the sprite id for the selected right
	 */
	private final int iconId;
	
	/**
	 * Forum id
	 */
	private final int forumId;


	/**
	 * The rights inherited by this right
	 */
	private final List<Right> inherited;

	/**
	 * The color associated with the right
	 */
	private final String color;

	/**
	 * Creates a new right with a value to differentiate it between the others
	 * 
	 * @param right the right required
	 * @param color a color thats used to represent the players name when displayed
	 * @param inherited the right or rights inherited with this level of right
	 */
	private Right(int id, int right, int iconId, String color,int forumId, Right... inherited) {
		this.id = id;
		this.right = right;
		this.iconId = iconId;
		this.inherited = Arrays.asList(inherited);
		this.color = color;
		this.forumId = forumId;
	}

	/**
	 * The rights of this enumeration
	 * 
	 * @return the rights
	 */
	public int getValue() {
		return right;
	}

	/**
	 * The position of this enumeration
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * The forum position of this enumeration
	 * 
	 * @return the forum id
	 */
	public int getForumId() {
		return forumId;
	}

	/**
	 * Returns a {@link Rights} object for the value.
	 * 
	 * @param value the right level
	 * @return the rights object
	 */
	public static Right get(int value) {
		return RIGHTS.stream().filter(element -> element.right == value).findFirst().orElse(PLAYER);
	}
	
	public static Right getForumID(int value) {
		return RIGHTS.stream().filter(element -> element.right == value).findFirst().orElse(PLAYER);
	}
	
	public static Right forForumId(int id) {
		for (Right rights : Right.values()) {
			if (rights.getForumId() == id) {
				return rights;
			}
		}
		return null;
	}

	/**
	 * A {@link Set} of all {@link Rights} elements that cannot be directly modified.
	 */
	private static final Set<Right> RIGHTS = Collections.unmodifiableSet(EnumSet.allOf(Right.class));

	/**
	 * The color associated with the right
	 * 
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Determines if this level of rights inherited another level of rights
	 * 
	 * @param rights the level of rights we're looking to determine is inherited
	 * @return {@code true} if the rights are inherited, otherwise {@code false}
	 */
	public boolean isOrInherits(Right right) {
		return this == right || inherited.size() > 0 && inherited.stream().anyMatch(r -> r.isOrInherits(right));
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain MODERATOR}
	 * @return	true if they are of type moderator
	 */
	public boolean isModerator() {
		return equals(MODERATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain HELPER}
	 * @return	true if they are of type moderator
	 */
	public boolean isSupport() {
		return equals(SERVER_SUPPORT);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain ADMINISTRATOR}
	 * @return	true if they are of type administrator
	 */
	public boolean isAdministrator() {
		return equals(ADMINISTRATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain MANAGER}
	 * @return	true if they are of type manager
	 */
	public boolean isManager() {
		return equals(MANAGER);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain GAME_DEVELOPER}
	 * @return	true if they are of type game developer
	 */
	public boolean isDeveloper() {
		return equals(GAME_DEVELOPER);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain OWNER}
	 * @return	true if they are of type owner
	 */
	public boolean isOwner() {
		return equals(OWNER);
	}

	/**
	 * Determines if the players right equal that of {@link MODERATOR}, {@link ADMINISTRATOR},
	 * and {@link OWNER}
	 * @return	true if they are any of the predefined types
	 */
	public boolean isStaff() {
		return isSupport() || isModerator() || isAdministrator() || isManager() || isDeveloper() || isOwner();
	}
	
	/**
	 * An array of {@link Right} objects that represent the order in which some rights should be prioritized over others. The index at which a {@link Right} object exists
	 * determines it's priority. The lower the index the less priority that {@link Right} has over another. The list is ordered from lowest priority to highest priority.
	 * <p>
	 * An example of this would be comparing a {@link #MODERATOR} to a {@link #ADMINISTRATOR}. An {@link #ADMINISTRATOR} can be seen as more 'powerful' when compared to a
	 * {@link #MODERATOR} because they have more power within the community.
	 * </p>
	 */

	public static final Right[] PRIORITY = {
			PLAYER, SERVER_SUPPORT,
			GAME_DEVELOPER, MODERATOR, ADMINISTRATOR, OWNER, };

	@Override
	public String toString() {
		return Misc.capitalizeWords(name().replaceAll("_", " "));
	}

	@Override
	public int compare(Right arg0, Right arg1) {
		return 0;
	}

	public int getIconId() {
		return iconId;
	}
}
