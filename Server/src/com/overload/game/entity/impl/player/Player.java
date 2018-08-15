package com.overload.game.entity.impl.player;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jcabi.jdbc.JdbcSession;
import com.overload.game.GameConstants;
import com.overload.game.content.*;
import com.overload.game.content.achievement.AchievementManager;
import com.overload.game.content.achievement.Diary.DiaryType;
import com.overload.game.content.achievement.Diary.OptionType;
import com.overload.game.content.achievement.Diary.Tasks;
import com.overload.game.content.achievement.DiaryManager;
import com.overload.game.content.bosses.vorkath.VorkathLostItems;
import com.overload.game.content.bosses.zulrah.ZulrahLostItems;
import com.overload.game.content.PrayerHandler.PrayerData;
import com.overload.game.content.clan.ClanChat;
import com.overload.game.content.clan.ClanChatManager;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.WeaponInterfaces;
import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.content.combat.magic.Autocasting;
import com.overload.game.content.combat.method.impl.npcs.CerberusCombatMethod;
import com.overload.game.content.combat.method.impl.npcs.VorkathCombatMethod;
import com.overload.game.content.combat.method.impl.npcs.ZulrahCombatMethod;
import com.overload.game.content.minigames.Minigame;
import com.overload.game.content.minigames.MinigameAttributes;
import com.overload.game.content.minigames.PointsHandler;
import com.overload.game.content.minigames.barrows.Barrows;
import com.overload.game.content.minigames.barrows.Barrows.Brother;
import com.overload.game.content.minigames.fightcave.FightCaves;
import com.overload.game.content.minigames.godwars.Godwars;
import com.overload.game.content.minigames.nightmarezone.NightmareZone;
import com.overload.game.content.presets.Presetable;
import com.overload.game.content.presets.Presetables;
import com.overload.game.content.skill.SkillManager;
import com.overload.game.content.skill.construction.House;
import com.overload.game.content.skill.farming.Farming;
import com.overload.game.content.skill.farming.Allotments.AllotmentFieldsData;
import com.overload.game.content.skill.farming.Compost;
import com.overload.game.content.skill.hunter.Hunter;
import com.overload.game.content.skill.skillable.Skillable;
import com.overload.game.content.skill.runecrafting.Runecrafting.Pouch;
import com.overload.game.content.skill.runecrafting.Runecrafting.PouchContainer;
import com.overload.game.content.skill.slayer.Slayer;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.npc.NpcAggression;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.model.*;
import com.overload.game.model.areas.AreaManager;
import com.overload.game.model.container.impl.Bank;
import com.overload.game.model.container.impl.Equipment;
import com.overload.game.model.container.impl.Inventory;
import com.overload.game.model.container.impl.PriceChecker;
import com.overload.game.model.container.shop.Shop;
import com.overload.game.model.dialogue.Dialogue;
import com.overload.game.model.dialogue.DialogueOptions;
import com.overload.game.model.equipment.BonusManager;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.movement.MovementQueue;
import com.overload.game.model.movement.WalkToAction;
import com.overload.game.model.rights.Right;
import com.overload.game.model.rights.RightGroup;
import com.overload.game.model.syntax.EnterSyntax;
import com.overload.game.model.teleportation.TeleportButton;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.CombatPoisonEffect;
import com.overload.game.task.impl.PlayerDeathTask;
import com.overload.game.task.impl.RestoreSpecialAttackTask;
import com.overload.net.PlayerSession;
import com.overload.net.channel.ChannelEventHandler;
import com.overload.net.packet.PacketSender;
import com.overload.util.FrameUpdater;
import com.overload.util.Misc;
import com.overload.util.NpcIdentifiers;
import com.overload.util.Stopwatch;
import com.overload.util.timers.TimerKey;

import io.netty.buffer.ByteBuf;

public class Player extends Character {
	
	public int packetOpcode = -1;
	public int packetSize = -1;
    private int trapsLaid;
    public Item[] puzzleStoredItems;
    public int clue1Amount;
    public int clue2Amount;
    public int clue3Amount;
    public int clueLevel;
    public int sextantGlobalPiece;
    public double sextantBarDegree;
    public int rotationFactor;
    public int sextantLandScapeCoords;
    public int sextantSunCoords;
    private DwarfCannon cannon;
	public final SecondsTimer increaseStats = new SecondsTimer();
	public final SecondsTimer decreaseStats = new SecondsTimer();
	private final List<Player> localPlayers = new LinkedList<Player>();
	private final List<NPC> localNpcs = new LinkedList<NPC>();
	private final PacketSender packetSender = new PacketSender(this);
	private final Appearance appearance = new Appearance(this);
	private final SkillManager skillManager = new SkillManager(this);
	private final PlayerRelations relations = new PlayerRelations(this);
	private final FrameUpdater frameUpdater = new FrameUpdater();
	private final BonusManager bonusManager = new BonusManager();
	private final QuickPrayers quickPrayers = new QuickPrayers(this);
	private final Inventory inventory = new Inventory(this);
	private final Equipment equipment = new Equipment(this);
	private final PriceChecker priceChecker = new PriceChecker(this);
	private final Stopwatch clickDelay = new Stopwatch();
	private final Stopwatch lastItemPickup = new Stopwatch();
	private final SecondsTimer yellDelay = new SecondsTimer();
    private PlayerAnimations playerAnimations = new PlayerAnimations();
	private final SecondsTimer aggressionTolerance = new SecondsTimer();
	
	
	public int item, uneditItem, quantity, price, pageId = 1, searchId;
	public String lookup;
	public List<Integer> saleResults;
	public ArrayList<Integer> saleItems = new ArrayList<Integer>();
	public ArrayList<Integer> saleAmount = new ArrayList<Integer>();
	public ArrayList<Integer> salePrice = new ArrayList<Integer>();
	public int[] historyItems = new int[30];
	public int[] historyItemsN = new int[30];
	public int[] historyPrice = new int[30];
	public boolean insidePost = false;
	public boolean inSelecting = false, isListing = false;
	
	// Delay for restoring special attack
	private final SecondsTimer specialAttackRestore = new SecondsTimer();
	/*
	 * Fields
	 */
    private final PointsHandler pointsHandler = new PointsHandler(this);
    private Minigame minigame = null;
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
	private final SecondsTimer targetSearchTimer = new SecondsTimer();
	private final List<String> recentKills = new ArrayList<String>(); // Contains ip addresses of recent kills
	private final Queue<ChatMessage> chatMessageQueue = new ConcurrentLinkedQueue<>();
	private ChatMessage currentChatMessage;
	// Logout
	private final SecondsTimer forcedLogoutTimer = new SecondsTimer();
	// Trading
	private final Trading trading = new Trading(this);
	private final Dueling dueling = new Dueling(this);
	// Presets
	private Presetable currentPreset;
	private Presetable[] presets = new Presetable[Presetables.MAX_PRESETS];
	private boolean openPresetsOnDeath = true;
    private GameObject interactingObject;
	private String username;
	private String password;
	private String email;
	public String getEmail() {
		return email;
	}
    /* Thieving variable */
    public boolean isCracking;
    public void setEmail(String email) {
		this.email = email;
	}
	private String hostAddress;
	private Long longUsername;
	private PlayerSession session;
	private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
	private PlayerStatus status = PlayerStatus.NONE;
	private ClanChat currentClanChat;
	private String clanChatName;
	private Dialogue dialogue;
	private Shop shop;
	private int interfaceId = -1, walkableInterfaceId = -1, multiIcon;
	private boolean isRunning = true;
	private int runEnergy = 100;
	private Stopwatch lastRunRecovery = new Stopwatch();
	private boolean isDying;
    private PlayerProperties properties = new PlayerProperties(this);
	private boolean allowRegionChangePacket;
	private boolean experienceLocked;
	private ForceMovement forceMovement;
	private NPC currentPet;
    private Task currentTask;
	private int skillAnimation;
	private boolean drainingPrayer;
	private double prayerPointDrain;
    private boolean settingUpCannon;
	private WalkToAction walkToTask;
	private EnterSyntax enterSyntax;
	private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
	private final Map<TeleportButton, Position> previousTeleports = new HashMap<>();
	private boolean teleportInterfaceOpen;
	private DialogueOptions dialogueOptions;
	private Action dialogueContinueAction;
	private int destroyItem = -1;
	private boolean updateInventory; // Updates inventory on next tick
	private boolean newPlayer;
	private boolean packetsBlocked = false;
	private int regionHeight;
	// Skilling
	private Optional<Skillable> skill = Optional.empty();
	private Optional<CreationMenu> creationMenu = Optional.empty();
	// Time the account was created
	private Timestamp creationDate;
    private int selectedSkillingItem;
    private int memberId = -1;
	// RC
	private PouchContainer[] pouches = new PouchContainer[] { new PouchContainer(Pouch.SMALL_POUCH),
			new PouchContainer(Pouch.MEDIUM_POUCH), new PouchContainer(Pouch.LARGE_POUCH),
			new PouchContainer(Pouch.GIANT_POUCH), };
	// Combat
	private SkullType skullType = SkullType.WHITE_SKULL;
	private CombatSpecial combatSpecial;
	private int recoilDamage;
	private SecondsTimer vengeanceTimer = new SecondsTimer();
    @SuppressWarnings("unused")
	private SecondsTimer chargetimer = new SecondsTimer();
	private int wildernessLevel;
	private int skullTimer;
	private int points;
	private int amountDonated;
	//Agility
    private boolean[] crossedObstacles = new boolean[7];
    private boolean crossingObstacle;
	// Blowpipe
	private int blowpipeScales;
	private int blowpipeAmmoType;
	private int blowpipeAmmoAmount;
	// Bounty hunter
	private int targetKills;
	private int normalKills;
	private int totalKills;
	private int killstreak;
	private int highestKillstreak;
	private int deaths;
	private int safeTimer = 180;
	// Barrows
	private int barrowsCrypt;
	private int barrowsChestsLooted;
	private boolean[] killedBrothers = new boolean[Brother.values().length];
	private NPC currentBrother;
	private boolean preserveUnlocked;
	private boolean rigourUnlocked;
	private boolean auguryUnlocked;
	private boolean targetTeleportUnlocked;
	// Banking
	private int currentBankTab;
	private Bank[] banks = new Bank[Bank.TOTAL_BANK_TABS]; // last index is for bank searches
	private boolean noteWithdrawal, insertMode, searchingBank;
	private String searchSyntax = "";
	private boolean placeholders = true;
	private boolean enteredPin = false;

	private boolean invulnerable;
	
	// Rights
	private RightGroup rights = null;
	private PlayerModes mode = PlayerModes.NORMAL;
	
	public PlayerModes getMode() {
		return mode;
	}

	public void setMode(PlayerModes mode) {
		this.mode = mode;
	}

	
	public int getStarter() {
		return starter;
	}
	
	public void setStarter(int starter) {
		this.starter = starter;
	}
	
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getMemberId() {
        return memberId;
    }

	
	private int starter = -1;
	
	public boolean VPSEnvironment() {
		File onvps = new File(GameConstants.DATA + "onvps.txt");
		if(onvps.exists()) {
			return true;
		}
		return false;
	}
	
	public static boolean VPSEnvironment1() {
		File onvps = new File(GameConstants.DATA + "onvps.txt");
		if(onvps.exists()) {
			return true;
		}
		return false;
	}


	/**
	 * The cached player update block for updating.
	 */
	private ByteBuf cachedUpdateBlock;
	private String loyaltyTitle = "empty";
	private boolean spawnedBarrows;
	private Position oldPosition;
	
	/**
	 * Creates this player.
	 *
	 * @param playerIO
	 */
	public Player(PlayerSession playerIO) {
		super(NodeType.PLAYER, GameConstants.DEFAULT_POSITION.clone());
		this.session = playerIO;
	}

	/**
	 * Actions that should be done when this character is added to the world.
	 */
	@Override
	public void onAdd() {
		onLogin();
	}

	
	/**
	 * Actions that should be done when this character is removed from the world.
	 */
	@Override
	public void onRemove() {
		onLogout();
	}

	@Override
	public void appendDeath() {
		if (!isDying) {
			TaskManager.submit(new PlayerDeathTask(this));
			isDying = true;
		}
	}

	@Override
	public int getHitpoints() {
		return getSkillManager().getCurrentLevel(Skill.HITPOINTS);
	}

	@Override
	public int getAttackAnim() {
		int anim = getCombat().getFightType().getAnimation();
		return anim;
	}

	@Override
	public int getBlockAnim() {
		final Item shield = getEquipment().getItems()[Equipment.SHIELD_SLOT];
		final Item weapon = getEquipment().getItems()[Equipment.WEAPON_SLOT];
		ItemDefinition definition = shield.getId() > 0 ? shield.getDefinition() : weapon.getDefinition();
		return definition.getBlockAnim();
	}

	@Override
	public Character setHitpoints(int hitpoints) {
		if (isDying) {
			return this;
		}

		if (invulnerable) {
			if (skillManager.getCurrentLevel(Skill.HITPOINTS) > hitpoints) {
				return this;
			}
		}

		skillManager.setCurrentLevel(Skill.HITPOINTS, hitpoints);
		packetSender.sendSkill(Skill.HITPOINTS);
		if (getHitpoints() <= 0 && !isDying)
			appendDeath();
		return this;
	}

	@Override
	public void heal(int amount) {
		int level = skillManager.getMaxLevel(Skill.HITPOINTS);
		if ((skillManager.getCurrentLevel(Skill.HITPOINTS) + amount) >= level) {
			setHitpoints(level);
		} else {
			setHitpoints(skillManager.getCurrentLevel(Skill.HITPOINTS) + amount);
		}
	}

	@Override
	public int getBaseAttack(CombatType type) {
		if (type == CombatType.RANGED)
			return skillManager.getCurrentLevel(Skill.RANGED);
		else if (type == CombatType.MAGIC)
			return skillManager.getCurrentLevel(Skill.MAGIC);
		return skillManager.getCurrentLevel(Skill.ATTACK);
	}

	@Override
	public int getBaseDefence(CombatType type) {
		if (type == CombatType.MAGIC)
			return skillManager.getCurrentLevel(Skill.MAGIC);
		return skillManager.getCurrentLevel(Skill.DEFENCE);
	}

	@Override
	public int getBaseAttackSpeed() {

		// Gets attack speed for player's weapon
		// If player is using magic, attack speed is
		// Calculated in the MagicCombatMethod class.

		int speed = getCombat().getWeapon().getSpeed();

		if (getCombat().getFightType().toString().toLowerCase().contains("rapid")) {
			speed--;
		}

		return speed;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Player)) {
			return false;
		}
		Player p = (Player) o;
		return p.getUsername().equals(username);
	}

	@Override
	public int getSize() {
		return 1;
	}
	
	
	
	public static boolean updatePlayerInfo(Player player,boolean isEmail) {
		final String username = player.getUsername();
		try {
			final boolean response = new JdbcSession(GameConstants.fs.getConnection())
					.sql(isEmail ? 
					"SELECT pp_main_photo,email,member_id, member_group_id, mgroup_others, members_pass_hash, name, temp_ban FROM core_members WHERE UPPER(email) = ?" : 
					"SELECT pp_main_photo,email,member_id, member_group_id, mgroup_others, members_pass_hash, temp_ban FROM core_members WHERE UPPER(name) = ?")
					.set(username.toUpperCase())
					.select((rset, stmt) -> {
						if (rset.next()) {
							final String profilePicture = rset.getString(1);
							final String fullemail = rset.getString(2);
							final int right = rset.getInt(4);
							player.setEmail(fullemail);
								
							String playerRights = Right.forForumId(right).toString().replace(" ", "_");
							int playerRights1 = Right.forForumId(right).getValue();
							player.getRights().setPrimary(Right.valueOf(playerRights));
								
							player.getPacketSender().sendRights();
							player.getPacketSender().saveAccount(playerRights1,player.getMode().getId(),player.getUsername(),profilePicture);
							return true;
						}
						return true;
					});
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void process() {
		// Timers
		getTimers().process();

		// Process incoming packets...
		getSession().processPackets();

		// Process walk to task..
		if (walkToTask != null) {
			walkToTask.process();
		}

		//Process instanced region..
		if(getInstancedRegion().isPresent()) {
			getInstancedRegion().get().sequence(this);
		}
		
		// Process walking queue..
		getMovementQueue().process();

		// Process combat
		getCombat().process();

		// Process aggression
		NpcAggression.process(this);

		// Process areas..
		AreaManager.process(this);

		// Process Bounty Hunter
		BountyHunter.process(this);
		
		getDiaryManager().process();

        if (ZulrahCombatMethod.isInToxicLocation(this)) {
            this.getCombat().getHitQueue().addPendingDamage(new HitDamage(1 + Misc.random(3), HitMask.GREEN));//1 + Misc.random(3), Hitmark.VENOM);
        }
        if (VorkathCombatMethod.isInToxicLocation(this)) {
            this.getCombat().getHitQueue().addPendingDamage(new HitDamage(1 + Misc.random(6), HitMask.GREEN));//1 + Misc.random(3), Hitmark.VENOM);
        }
        if (CerberusCombatMethod.isInLavaLocation(this)) {
            this.getCombat().getHitQueue().addPendingDamage(new HitDamage(1 + Misc.random(6), HitMask.RED));//1 + Misc.random(3), Hitmark.VENOM);
        }

		// Updates inventory if an update
		// has been requested
		if (isUpdateInventory()) {
			getInventory().refreshItems();
			setUpdateInventory(false);
		}

		// Updates appearance if an update
		// has been requested
		// or if skull timer hits 0.
		if (isSkulled() && getAndDecrementSkullTimer() == 0) {
			getUpdateFlag().flag(Flag.APPEARANCE);
		}

		// Send queued chat messages
		if (!getChatMessageQueue().isEmpty()) {
			setCurrentChatMessage(getChatMessageQueue().poll());
			getUpdateFlag().flag(Flag.CHAT);
		} else {
			setCurrentChatMessage(null);
		}

		// Increase run energy
		if (runEnergy < 100 && (!getMovementQueue().isMoving() || !isRunning)) {
			if (lastRunRecovery.elapsed(MovementQueue.runEnergyRestoreDelay(this))) {
				runEnergy++;
				getPacketSender().sendRunEnergy();
				lastRunRecovery.reset();
			}
		}

		/**
		 * Decrease boosted stats Increase lowered stats
		 */
		if (getHitpoints() > 0) {
			if (increaseStats.finished() || decreaseStats
					.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60)) {
				for (Skill skill : Skill.values()) {
					int current = getSkillManager().getCurrentLevel(skill);
					int max = getSkillManager().getMaxLevel(skill);

					// Should lowered stats be increased?
					if (current < max) {
						if (increaseStats.finished()) {
							int restoreRate = 1;

							// Rapid restore effect - 2x restore rate for all stats except hp/prayer
							// Rapid heal - 2x restore rate for hitpoints
							if (skill != Skill.HITPOINTS && skill != Skill.PRAYER) {
								if (PrayerHandler.isActivated(this, PrayerHandler.RAPID_RESTORE)) {
									restoreRate = 2;
								}
							} else if (skill == Skill.HITPOINTS) {
								if (PrayerHandler.isActivated(this, PrayerHandler.RAPID_HEAL)) {
									restoreRate = 2;
								}
							}
							if (skill != Skill.PRAYER)
								getSkillManager().increaseCurrentLevel(skill, restoreRate, max);
						}
					} else if (current > max) {

						// Should boosted stats be decreased?
						if (decreaseStats
								.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72
										: 60)) {

							// Never decrease Hitpoints / Prayer
							if (skill != Skill.HITPOINTS && skill != Skill.PRAYER) {
								getSkillManager().decreaseCurrentLevel(skill, 1, 1);
							}

						}
					}
				}

				// Reset timers
				if (increaseStats.finished()) {
					increaseStats.start(60);
				}
				if (decreaseStats
						.secondsElapsed() >= (PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60)) {
					decreaseStats.start((PrayerHandler.isActivated(this, PrayerHandler.PRESERVE) ? 72 : 60));
				}
			}
		} else {
			if (!TaskManager.hasTask(new PlayerDeathTask(this))) {
				appendDeath();
			}
		}
	}

	private boolean keepLoggedIn = false;
	
	public boolean keepLoggedIn() {
		return keepLoggedIn;
	}
	public void setKeepLoggedIn(boolean keepLoggedIn) {
		this.keepLoggedIn = keepLoggedIn;
	}
	
	/**
	 * Can the player logout?
	 *
	 * @return Yes if they can logout, false otherwise.
	 */
	public boolean canLogout() {
		if (keepLoggedIn()) {
			return false;
		}
		if (CombatFactory.isBeingAttacked(this)) {
			getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return false;
		}
		if (busy()) {
			getPacketSender().sendMessage("You cannot log out at the moment.");
			return false;
		}
		return true;
	}

	/**
	 * Requests a logout by sending the logout packet to the client. This leads to
	 * the connection being closed. The {@link ChannelEventHandler} will then add
	 * the player to the remove characters queue.
	 */
	public void requestLogout() {
		getPacketSender().sendLogout();
	}

	/**
	 * Handles the actual logging out from the game.
	 */
	public void onLogout() {
		// Notify us
		System.out.println(
				"[World] Deregistering player - [username, host] : [" + getUsername() + ", " + getHostAddress() + "]");

		getPacketSender().sendInterfaceRemoval();

		// Leave area
		if (getArea() != null) {
			getArea().leave(this);
		}

		// Do stuff...
        Hunter.handleLogout(this);
		Barrows.brotherDespawn(this);
		PetHandler.pickup(this, getCurrentPet());
		getRelations().updateLists(false);
		BountyHunter.unassign(this);
		ClanChatManager.leave(this, false);
		TaskManager.cancelTasks(this);
		PlayerSaving.save(this);

		if (getSession().getChannel().isOpen()) {
			getSession().getChannel().close();
		}
	}

	/**
	 * Called by the world's login queue!
	 */
	public void onLogin() {
		// Attempt to register the player..
		System.out.println("[World] Registering player - [username, host] : [" + getUsername() + ", " + getHostAddress() + "]");

		setNeedsPlacement(true);
		getPacketSender().sendMapRegion().sendDetails(); // Map region, player index and player rights
		getPacketSender().sendTabs(); // Client sideicons
		
		String rightsIcon = getRights().getIconId() != -1 ? "<img=" + getRights().getIconId() + "> " : "";
		String modeIcon =   getMode().getIconId()   != -1 ? "<img=" + getMode().getIconId()   + ">"  : "";
		
		getPacketSender().sendMessage("Welcome " + rightsIcon + "" + modeIcon + " " + getMode().getYellPrefix() + getUsername() + "</col></shad>" + " to Overload!");
			
//		if(getUsername().equalsIgnoreCase("mitch")) {
//			getPacketSender().sendMessage("Hey Sexy Boy, Wanna have some Finger Sex");
//		}
		
		long totalExp = 0;
		for (Skill skill : Skill.values()) {
			getSkillManager().updateSkill(skill);
			totalExp += getSkillManager().getExperience(skill);
		}
		getPacketSender().sendTotalExp(totalExp);

		// Send friends and ignored players lists...
		getRelations().setPrivateMessageId(1).onLogin(this).updateLists(true);

		// Reset prayer configs...
		PrayerHandler.resetAll(this);
		getPacketSender().sendConfigByte(709, PrayerHandler.canUse(this, PrayerData.PRESERVE, false) ? 1 : 0);
		getPacketSender().sendConfigByte(711, PrayerHandler.canUse(this, PrayerData.RIGOUR, false) ? 1 : 0);
		getPacketSender().sendConfigByte(713, PrayerHandler.canUse(this, PrayerData.AUGURY, false) ? 1 : 0);

		// Refresh item containers..
		getInventory().refreshItems();
		getEquipment().refreshItems();

		// Interaction options on right click...
		getPacketSender().sendInteractionOption("Follow", 3, false);
		getPacketSender().sendInteractionOption("Trade With", 4, false);

		// Sending run energy attributes...
		getPacketSender().sendRunStatus();
		getPacketSender().sendRunEnergy();
		// Sending player's rights..
		if (VPSEnvironment())
			updatePlayerInfo(this,false);
		else {
			this.getPacketSender().sendRights();
		}


		// Close all interfaces, just in case...
		getPacketSender().sendInterfaceRemoval();

		// Update weapon data and interfaces..
		WeaponInterfaces.assign(this);
		// Update weapon interface configs
		getPacketSender().sendConfigByte(getCombat().getFightType().getParentId(), getCombat().getFightType().getChildId())
				.sendConfigByte(172, getCombat().autoRetaliate() ? 1 : 0).updateSpecialAttackOrb();

		// Reset autocasting
		Autocasting.setAutocast(this, null);

		// Send pvp stats..
		getPacketSender().sendString(52029, "@or1@Killstreak: " + getKillstreak())
				.sendString(52030, "@or1@Kills: " + getTotalKills()).sendString(52031, "@or1@Deaths: " + getDeaths())
				.sendString(52033, "@or1@K/D Ratio: " + getKillDeathRatio())
				.sendString(52034, "@or1@Donated: " + getAmountDonated());

		// Join clanchat
		ClanChatManager.onLogin(this);

		// Handle timers and run tasks
		if (isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(this));
		}
		if (getSpecialPercentage() < 100) {
			TaskManager.submit(new RestoreSpecialAttackTask(this));
		}

		if (!getVengeanceTimer().finished()) {
			getPacketSender().sendEffectTimer(getVengeanceTimer().secondsRemaining(), EffectTimer.VENGEANCE);
		}
		if (!getCombat().getFireImmunityTimer().finished()) {
			getPacketSender().sendEffectTimer(getCombat().getFireImmunityTimer().secondsRemaining(),
					EffectTimer.ANTIFIRE);
		}
		if (!getCombat().getTeleBlockTimer().finished()) {
			getPacketSender().sendEffectTimer(getCombat().getTeleBlockTimer().secondsRemaining(),
					EffectTimer.TELE_BLOCK);
		}

		decreaseStats.start(60);
		increaseStats.start(60);

		getUpdateFlag().flag(Flag.APPEARANCE);
		starterclass.openStarter(this);

		getAggressionTolerance().start(NpcAggression.NPC_TOLERANCE_SECONDS);
		
		if (this.getRights().isOrInherits(Right.GAME_DEVELOPER))
            this.setInvulnerable(true);
	}
	
	public static String rawpass = "";
	
	public Starter starterclass = new Starter();
	
	public String uniquestring = "";
	
	public String getUniqueString() {
		return uniquestring;
	}
	
	public void setUniqueString(String UID) {
		this.uniquestring = UID;
	}
	
	public String discordUID = "";
	
	public String getDiscordUID() {
		return discordUID;
	}
	
	public void setDiscordUID(String UID) {
		this.discordUID = UID;
	}

	/**
	 * Resets the player's attributes to default.
	 */
	public void resetAttributes() {
		performAnimation(new Animation(65535));
		setSpecialActivated(false);
		CombatSpecial.updateBar(this);
		setHasVengeance(false);
		getCombat().getFireImmunityTimer().stop();
		getCombat().getPoisonImmunityTimer().stop();
		getCombat().getTeleBlockTimer().stop();
		getTimers().cancel(TimerKey.FREEZE);
		getCombat().getPrayerBlockTimer().stop();
		setPoisonDamage(0);
		setWildernessLevel(0);
		setRecoilDamage(0);
		setSkullTimer(0);
		setSkullType(SkullType.WHITE_SKULL);
		WeaponInterfaces.assign(this);
		BonusManager.update(this);
		PrayerHandler.deactivatePrayers(this);
		getEquipment().refreshItems();
		getInventory().refreshItems();
		for (Skill skill : Skill.values())
			getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
		setRunEnergy(100);
		getPacketSender().sendRunEnergy();
		getMovementQueue().setBlockMovement(false);
		getPacketSender().sendEffectTimer(0, EffectTimer.ANTIFIRE).sendEffectTimer(0, EffectTimer.FREEZE).sendEffectTimer(0, EffectTimer.VENGEANCE).sendEffectTimer(0, EffectTimer.TELE_BLOCK);
		setUntargetable(false);
		isDying = false;

		getUpdateFlag().flag(Flag.APPEARANCE);
		
	}

	/**
	 * Checks if a player is busy.
	 *
	 * @return
	 */
	public boolean busy() {
		if (interfaceId > 0) {
			return true;
		}
		if (getHitpoints() <= 0) {
			return true;
		}
		if (isNeedsPlacement() || isTeleporting()) {
			return true;
		}
		if (status != PlayerStatus.NONE) {
			return true;
		}
		if (forceMovement != null) {
			return true;
		}
		return false;
	}

	public boolean isPacketsBlocked() {
		return packetsBlocked;
	}

	public void setPacketsBlocked(boolean blocked) {
		this.packetsBlocked = blocked;
	}

	/*
	 * Getters/Setters
	 */

    public GameObject getInteractingObject() {
        return interactingObject;
    }

    public Player setInteractingObject(GameObject interactingObject) {
        this.interactingObject = interactingObject;
        return this;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public int getSelectedSkillingItem() {
        return selectedSkillingItem;
    }

    public void setSelectedSkillingItem(int selectedItem) {
        this.selectedSkillingItem = selectedItem;
    }

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp timestamp) {
		creationDate = timestamp;
	}

	public PlayerSession getSession() {
		return session;
	}

	public String getUsername() {
		return username;
	}

	public Player setUsername(String username) {
		this.username = username;
		return this;
	}
    public PlayerAnimations getAnimations() {
        return playerAnimations;
    }

	public Long getLongUsername() {
		return longUsername;
	}

	public Player setLongUsername(Long longUsername) {
		this.longUsername = longUsername;
		return this;
	}

    public int getTrapsLaid() {
        return trapsLaid;
    }

    public void setTrapsLaid(int trapsLaid) {
        this.trapsLaid = trapsLaid;
    }

    public boolean isCrossingObstacle() {
        return crossingObstacle;
    }

    public Player setCrossingObstacle(boolean crossingObstacle) {
        this.crossingObstacle = crossingObstacle;
        return this;
    }

    public boolean[] getCrossedObstacles() {
        return crossedObstacles;
    }

    public boolean getCrossedObstacle(int i) {
        return crossedObstacles[i];
    }

    public Player setCrossedObstacle(int i, boolean completed) {
        crossedObstacles[i] = completed;
        return this;
    }

    public PlayerProperties getProperties() {
        return properties;
    }

    public void setCrossedObstacles(boolean[] crossedObstacles) {
        this.crossedObstacles = crossedObstacles;
    }


    public String getPassword() {
		return password;
	}

	public Player setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public Player setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
		return this;
	}

	public RightGroup getRights() {
		if (rights == null) {
			rights = new RightGroup(this, Right.PLAYER);
		}
		return rights;
	}

	public PacketSender getPacketSender() {
		return packetSender;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public SecondsTimer getForcedLogoutTimer() {
		return forcedLogoutTimer;
	}

	public boolean isDying() {
		return isDying;
	}

	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	public List<NPC> getLocalNpcs() {
		return localNpcs;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public Player setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}

	public boolean experienceLocked() {
		return experienceLocked;
	}

	public void setExperienceLocked(boolean experienceLocked) {
		this.experienceLocked = experienceLocked;
	}

	public PlayerRelations getRelations() {
		return relations;
	}

	public Dialogue getDialogue() {
		return this.dialogue;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public DialogueOptions getDialogueOptions() {
		return dialogueOptions;
	}

	public void setDialogueOptions(DialogueOptions dialogueOptions) {
		this.dialogueOptions = dialogueOptions;
	}

	public boolean isAllowRegionChangePacket() {
		return allowRegionChangePacket;
	}

	public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
		this.allowRegionChangePacket = allowRegionChangePacket;
	}

	public int getWalkableInterfaceId() {
		return walkableInterfaceId;
	}

	public void setWalkableInterfaceId(int interfaceId2) {
		this.walkableInterfaceId = interfaceId2;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public Player setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public PlayerInteractingOption getPlayerInteractingOption() {
		return playerInteractingOption;
	}

	public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
		this.playerInteractingOption = playerInteractingOption;
		return this;
	}

	public FrameUpdater getFrameUpdater() {
		return frameUpdater;
	}

	public BonusManager getBonusManager() {
		return bonusManager;
	}

	public int getMultiIcon() {
		return multiIcon;
	}

	public Player setMultiIcon(int multiIcon) {
		this.multiIcon = multiIcon;
		return this;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public ForceMovement getForceMovement() {
		return forceMovement;
	}

	public Player setForceMovement(ForceMovement forceMovement) {
		this.forceMovement = forceMovement;
		return this;
	}

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public Player setCannon(DwarfCannon cannon) {
        this.cannon = cannon;
        return this;
    }

    public DwarfCannon getCannon() {
        return cannon;
    }

    public int getSkillAnimation() {
		return skillAnimation;
	}

	public Player setSkillAnimation(int animation) {
		this.skillAnimation = animation;
		return this;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public void setRunEnergy(int runEnergy) {
		if (runEnergy > 100)
			runEnergy = 100;
		this.runEnergy = runEnergy;
	}

	public boolean isDrainingPrayer() {
		return drainingPrayer;
	}

	public void setDrainingPrayer(boolean drainingPrayer) {
		this.drainingPrayer = drainingPrayer;
	}

	public double getPrayerPointDrain() {
		return prayerPointDrain;
	}

	public void setPrayerPointDrain(double prayerPointDrain) {
		this.prayerPointDrain = prayerPointDrain;
	}

	public Stopwatch getLastItemPickup() {
		return lastItemPickup;
	}

	public WalkToAction getWalkToTask() {
		return walkToTask;
	}

	public void setWalkToTask(WalkToAction walkToTask) {
		this.walkToTask = walkToTask;
	}

	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}

	public void setCombatSpecial(CombatSpecial combatSpecial) {
		this.combatSpecial = combatSpecial;
	}

	public int getRecoilDamage() {
		return recoilDamage;
	}

	public void setRecoilDamage(int recoilDamage) {
		this.recoilDamage = recoilDamage;
	}

	public MagicSpellbook getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(MagicSpellbook spellbook) {
		this.spellbook = spellbook;
	}

	public SecondsTimer getVengeanceTimer() {
		return vengeanceTimer;
	}
    public SecondsTimer getChargeTimer() {
        return vengeanceTimer;
    }

    public int getWildernessLevel() {
		return wildernessLevel;
	}

	public void setWildernessLevel(int wildernessLevel) {
		this.wildernessLevel = wildernessLevel;
	}

	public boolean isSpawnedBarrows() {
		return spawnedBarrows;
	}

	public void setSpawnedBarrows(boolean spawnedBarrows) {
		this.spawnedBarrows = spawnedBarrows;
	}

	public int getDestroyItem() {
		return destroyItem;
	}

	public void setDestroyItem(int destroyItem) {
		this.destroyItem = destroyItem;
	}

	public boolean isSkulled() {
		return skullTimer > 0;
	}

	public int getAndDecrementSkullTimer() {
		return this.skullTimer--;
	}

	public int getSkullTimer() {
		return this.skullTimer;
	}

	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void incrementPoints(int points) {
		this.points += points;
	}

	public boolean isUpdateInventory() {
		return updateInventory;
	}

	public void setUpdateInventory(boolean updateInventory) {
		this.updateInventory = updateInventory;
	}

	public Stopwatch getClickDelay() {
		return clickDelay;
	}

	int messageCount = 0;
	
	public int getMessageCount() {
		return messageCount;
	}
	
	public void setMessageCount(int i) {
		this.messageCount = i;
	}
	
	public void incrementMessageCount() {
		messageCount++;
	}
	
	Stopwatch messageDelay = new Stopwatch();
	public Stopwatch getMessageDelay() {
		return messageDelay;
	}
	
	Stopwatch messageBlock = new Stopwatch();
	public Stopwatch getMessageBlock() {
		return messageBlock;
	}

	public Shop getShop() {
		return shop;
	}

	public Player setShop(Shop shop) {
		this.shop = shop;
		return this;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public Player setStatus(PlayerStatus status) {
		this.status = status;
		return this;
	}

	public EnterSyntax getEnterSyntax() {
		return enterSyntax;
	}

	public void setEnterSyntax(EnterSyntax enterSyntax) {
		this.enterSyntax = enterSyntax;
	}

	public int getCurrentBankTab() {
		return currentBankTab;
	}

	public Player setCurrentBankTab(int tab) {
		this.currentBankTab = tab;
		return this;
	}

	public void setNoteWithdrawal(boolean noteWithdrawal) {
		this.noteWithdrawal = noteWithdrawal;
	}

	public boolean withdrawAsNote() {
		return noteWithdrawal;
	}

	public void setInsertMode(boolean insertMode) {
		this.insertMode = insertMode;
	}

	public boolean insertMode() {
		return insertMode;
	}

	public Bank[] getBanks() {
		return banks;
	}

	public Bank getBank(int index) {
		if (banks[index] == null) {
			banks[index] = new Bank(this);
		}
		return banks[index];
	}

	public Player setBank(int index, Bank bank) {
		this.banks[index] = bank;
		return this;
	}

    public Minigame getMinigame() {
        return minigame;
    }

    public void setMinigame(Minigame minigame) {
        this.minigame = minigame;
    }

    public MinigameAttributes getMinigameAttributes() {
        return minigameAttributes;
    }
    public PointsHandler getPointsHandler() {
        return pointsHandler;
    }
	public boolean isNewPlayer() {
		return newPlayer;
	}

	public void setNewPlayer(boolean newPlayer) {
		this.newPlayer = newPlayer;
	}

	public boolean isSearchingBank() {
		return searchingBank;
	}

	public void setSearchingBank(boolean searchingBank) {
		this.searchingBank = searchingBank;
	}

	public String getSearchSyntax() {
		return searchSyntax;
	}

	public void setSearchSyntax(String searchSyntax) {
		this.searchSyntax = searchSyntax;
	}

	public boolean isPreserveUnlocked() {
		return preserveUnlocked;
	}

	public void setPreserveUnlocked(boolean preserveUnlocked) {
		this.preserveUnlocked = preserveUnlocked;
	}

	public boolean isRigourUnlocked() {
		return rigourUnlocked;
	}

	public void setRigourUnlocked(boolean rigourUnlocked) {
		this.rigourUnlocked = rigourUnlocked;
	}

	public boolean isAuguryUnlocked() {
		return auguryUnlocked;
	}

	public void setAuguryUnlocked(boolean auguryUnlocked) {
		this.auguryUnlocked = auguryUnlocked;
	}

	public PriceChecker getPriceChecker() {
		return priceChecker;
	}

	public ClanChat getCurrentClanChat() {
		return currentClanChat;
	}

	public void setCurrentClanChat(ClanChat currentClanChat) {
		this.currentClanChat = currentClanChat;
	}

	public String getClanChatName() {
		return clanChatName;
	}

	public void setClanChatName(String clanChatName) {
		this.clanChatName = clanChatName;
	}

	public Trading getTrading() {
		return trading;
	}

	public QuickPrayers getQuickPrayers() {
		return quickPrayers;
	}

	public boolean isTargetTeleportUnlocked() {
		return targetTeleportUnlocked;
	}

	public void setTargetTeleportUnlocked(boolean targetTeleportUnlocked) {
		this.targetTeleportUnlocked = targetTeleportUnlocked;
	}


    public SecondsTimer getYellDelay() {
		return yellDelay;
	}

	public int getAmountDonated() {
		return amountDonated;
	}

	public void setAmountDonated(int amountDonated) {
		this.amountDonated = amountDonated;
	}

	public void incrementAmountDonated(int amountDonated) {
		this.amountDonated += amountDonated;
	}

	public void incrementTargetKills() {
		targetKills++;
	}

	public int getTargetKills() {
		return targetKills;
	}

	public void setTargetKills(int targetKills) {
		this.targetKills = targetKills;
	}

	public void incrementKills() {
		normalKills++;
	}

	public int getNormalKills() {
		return normalKills;
	}

	public void setNormalKills(int normalKills) {
		this.normalKills = normalKills;
	}

	public int getTotalKills() {
		return totalKills;
	}

	public void setTotalKills(int totalKills) {
		this.totalKills = totalKills;
	}

	public void incrementTotalKills() {
		this.totalKills++;
	}

	public void incrementDeaths() {
		deaths++;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void resetSafingTimer() {
		this.setSafeTimer(180);
	}

	public int getHighestKillstreak() {
		return highestKillstreak;
	}

	public void setHighestKillstreak(int highestKillstreak) {
		this.highestKillstreak = highestKillstreak;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}

	public void incrementKillstreak() {
		this.killstreak++;
	}

	public String getKillDeathRatio() {
		double kc = 0;
		if (deaths == 0) {
			kc = totalKills / 1;
		} else {
			kc = ((double) totalKills / deaths);
		}
		return Misc.FORMATTER.format(kc);
	}
	public List<String> getRecentKills() {
		return recentKills;
	}

	public int getSafeTimer() {
		return safeTimer;
	}

	public void setSafeTimer(int safeTimer) {
		this.safeTimer = safeTimer;
	}

	public int decrementAndGetSafeTimer() {
		return this.safeTimer--;
	}

	public SecondsTimer getTargetSearchTimer() {
		return targetSearchTimer;
	}

	public SecondsTimer getSpecialAttackRestore() {
		return specialAttackRestore;
	}

	public SkullType getSkullType() {
		return skullType;
	}

	public void setSkullType(SkullType skullType) {
		this.skullType = skullType;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public int getBlowpipeScales() {
		return blowpipeScales;
	}

	public void setBlowpipeScales(int blowpipeScales) {
		this.blowpipeScales = blowpipeScales;
	}

	public void incrementBlowpipeScales(int blowpipeScales) {
		this.blowpipeScales += blowpipeScales;
	}

	public int decrementAndGetBlowpipeScales() {
		return this.blowpipeScales--;
	}

	public int decrementAndGetBlowpipeAmmo() {
		return this.blowpipeAmmoAmount--;
	}
	
	public void setBlowpipeAmmoAmount(int blowpipeAmmoAmount) {
		this.blowpipeAmmoAmount = blowpipeAmmoAmount;
	}
	
	public void setBlowpipeAmmoType(int blowpipeAmmoType) {
		this.blowpipeAmmoType = blowpipeAmmoType;
	}
	
	public int getBlowpipeAmmoAmount() {
		return blowpipeAmmoAmount;
	}
	
	public int getBlowpipeAmmoType() {
		return blowpipeAmmoType;
	}

	public NPC getCurrentPet() {
		return currentPet;
	}

	public void setCurrentPet(NPC currentPet) {
		this.currentPet = currentPet;
	}

	public SecondsTimer getAggressionTolerance() {
		return aggressionTolerance;
	}

	public ByteBuf getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}

	public void setCachedUpdateBlock(ByteBuf cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}

	public int getRegionHeight() {
		return regionHeight;
	}

	public void setRegionHeight(int regionHeight) {
		this.regionHeight = regionHeight;
	}

	public Optional<Skillable> getSkill() {
		return skill;
	}

	public void setSkill(Optional<Skillable> skill) {
		this.skill = skill;
	}

	public Optional<CreationMenu> getCreationMenu() {
		return creationMenu;
	}

	public void setCreationMenu(Optional<CreationMenu> creationMenu) {
		this.creationMenu = creationMenu;
	}

	public PouchContainer[] getPouches() {
		return pouches;
	}

	public void setPouches(PouchContainer[] pouches) {
		this.pouches = pouches;
	}

	public String getLoyaltyTitle() {
		return loyaltyTitle;
	}

	public void setLoyaltyTitle(String loyaltyTitle) {
		this.loyaltyTitle = loyaltyTitle;
		this.getUpdateFlag().flag(Flag.APPEARANCE);
	}
	
	public boolean isInvulnerable() {
		return invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public NPC getCurrentBrother() {
		return currentBrother;
	}

	public void setCurrentBrother(NPC brother) {
		this.currentBrother = brother;
	}

	public int getBarrowsCrypt() {
		return barrowsCrypt;
	}

	public void setBarrowsCrypt(int crypt) {
		this.barrowsCrypt = crypt;
	}

	public boolean[] getKilledBrothers() {
		return killedBrothers;
	}

	public void setKilledBrothers(boolean[] killedBrothers) {
		this.killedBrothers = killedBrothers;
	}

	public void setKilledBrother(int index, boolean state) {
		this.killedBrothers[index] = state;
	}

	public int getBarrowsChestsLooted() {
		return barrowsChestsLooted;
	}

	public void setBarrowsChestsLooted(int chestsLooted) {
		this.barrowsChestsLooted = chestsLooted;
	}

	public boolean isPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(boolean placeholders) {
		this.placeholders = placeholders;
	}
	
	public boolean hasEnteredPin() {
		return enteredPin;
	}

	public void setEnteredPin(boolean enteredPin) {
		this.enteredPin = enteredPin;
	}

	public String bankpin = "";
	
	public String getPin() {
		return bankpin;
	}

	public void setPin(String bankpin) {
		this.bankpin = bankpin;
	}

	public Presetable[] getPresets() {
		return presets;
	}

	public void setPresets(Presetable[] sets) {
		this.presets = sets;
	}

	public boolean isOpenPresetsOnDeath() {
		return openPresetsOnDeath;
	}

	public void setOpenPresetsOnDeath(boolean openPresetsOnDeath) {
		this.openPresetsOnDeath = openPresetsOnDeath;
	}

	public Presetable getCurrentPreset() {
		return currentPreset;
	}

	public void setCurrentPreset(Presetable currentPreset) {
		this.currentPreset = currentPreset;
	}

	public Queue<ChatMessage> getChatMessageQueue() {
		return chatMessageQueue;
	}

	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}

	public void setCurrentChatMessage(ChatMessage currentChatMessage) {
		this.currentChatMessage = currentChatMessage;
	}

	public Action getDialogueContinueAction() {
		return dialogueContinueAction;
	}

	public void setDialogueContinueAction(Action dialogueContinueAction) {
		this.dialogueContinueAction = dialogueContinueAction;
	}

	public Map<TeleportButton, Position> getPreviousTeleports() {
		return previousTeleports;
	}

	public boolean isTeleportInterfaceOpen() {
		return teleportInterfaceOpen;
	}

	public void setTeleportInterfaceOpen(boolean teleportInterfaceOpen) {
		this.teleportInterfaceOpen = teleportInterfaceOpen;
	}

	@Override
	public PendingHit manipulateHit(PendingHit hit) {
		Character attacker = hit.getAttacker();
		
		if (attacker.isNpc()) {
			NPC npc = attacker.getAsNpc();
			if (npc.getId() == NpcIdentifiers.TZTOK_JAD) {
				if (PrayerHandler.isActivated(this, PrayerHandler.getProtectingPrayer(hit.getCombatType()))) {
					hit.setTotalDamage(0);
				}
			}
		}
		
		return hit;
	}

	public Position getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(Position oldPosition) {
		this.oldPosition = oldPosition;
	}

	private boolean debug = false;
	public boolean isDebug() {
		return debug;
	}
	
	public void toggleDebug() {
		debug = !debug;
	}

	private Godwars gwd = null;
	public Godwars getGodwars() {
		if (gwd == null)
			gwd = new Godwars(this);
		return gwd;
	}

    private int npcTalking = -1;
    private int dialogueState = -1;
    private int dialogueClickType = -1;
    private int dialogueClickId = -1;
    private int[] dialogueAction = new int[2];

    public void setNpcTalking(int id) {
        this.npcTalking = id;
    }
    public int getNpcTalking() {
        return npcTalking;
    }

    public void setDialogueState(int id) {
        this.dialogueState = id;
    }
    public int getDialogueState() {
        return dialogueState;
    }
    
    public void setDialogueClickType(int id) {
        this.dialogueClickType = id;
    }
    public int getDialogueClickType() {
        return dialogueClickType;
    }
    
    public void setDialogueClickId(int id) {
        this.dialogueClickId = id;
    }
    public int getDialogueClickId() {
        return dialogueClickId;
    }

    public void setDialogueAction(int type, int id) {
        this.dialogueAction[0] = type;
        this.dialogueAction[1] = id;
    }
    public int[] getDialogueAction() {
        return dialogueAction;
    }
    public int getDialogueActionType() {
        return dialogueAction[0];
    }
    public int getDialogueActionId() {
        return dialogueAction[1];
    }
    
    private Slayer slayer;
    private int slayerTasksCompleted;
    public Slayer getSlayer() {
    	if (slayer == null)
    		slayer = new Slayer(this);
    	return slayer;
    }

	public void setSlayerTasksCompleted(int i) {
		this.slayerTasksCompleted = i;
	}
	public int getSlayerTasksCompleted() {
		return slayerTasksCompleted;
	}

	public Player sendMessage(String str) {
		getPacketSender().sendMessage(str);
		return this;
	}
	
	Farming farming;
	public Farming getFarming() {
		if (farming == null)
			farming = new Farming(this);
		return farming;
	}
	
	Map<String, AllotmentFieldsData> farmingPatches = new HashMap<>();
	public Map<String, AllotmentFieldsData> getPatches() {
		if (farmingPatches.isEmpty()) {
			for (AllotmentFieldsData allotment : this.getFarming().getAllotment().getAllotmentFieldData()) {
				farmingPatches.put(allotment.name(), allotment);
			}
		}
		return farmingPatches;
	}
	public void setPatches(Map<String, AllotmentFieldsData> patches) {
		this.farmingPatches = patches;
	}
	
	private Compost compost = null;
	public Compost getCompost() {
		if (compost == null)
			compost = new Compost(this);
		return compost;
	}

	
	NightmareZone nightmareZone = null;
	public NightmareZone getNightmareZone() {
		if (nightmareZone == null)
			nightmareZone = new NightmareZone(this);
		return nightmareZone;
	}

	private int fightCaveWaveID = 0;
	private long fightCaveLeaveTimer = 0;
	private int fightCaveWaveInfo = 0;
	private FightCaves fightcave = null;

	public FightCaves getFightCave() {
		if (fightcave == null)
			fightcave = new FightCaves(this);
		return fightcave;
	}
	
	public int getWaveID() {
		return fightCaveWaveID;
	}
	
	public void setWaveID(int value) {
		this.fightCaveWaveID = value;
	}

	public long getJoinFightCavesTime() {
		return fightCaveLeaveTimer;
	}

	public void setJoinFightCavesTime(Long value) {
		this.fightCaveLeaveTimer = value;
	}

	public int getWaveInfo() {
		return fightCaveWaveInfo;
	}

	public void setWaveInfo(int i) {
		this.fightCaveWaveInfo = i;
	}

	private boolean animatedArmour = false;
	public boolean hasAnimatedArmour() {
		return animatedArmour;
	}

	public void setAnimatedArmour(boolean i) {
		this.animatedArmour = i;
	}
	
	ZulrahLostItems zulrahLostItems = null;
	public ZulrahLostItems getZulrahLostItems() {
		if (zulrahLostItems == null) {
			zulrahLostItems = new ZulrahLostItems(this);
		}
		return zulrahLostItems;
	}

    VorkathLostItems vorkathLostItems = null;
    public VorkathLostItems getVorkathLostItems() {
        if (vorkathLostItems == null) {
            vorkathLostItems = new VorkathLostItems(this);
        }
        return vorkathLostItems;
    }
	
	private DiaryManager diaries = null;
	
	public DiaryManager getDiaryManager() {
		if (diaries == null)
			diaries = new DiaryManager(this);
		return diaries;
	}
	
	private DiaryType lastClickDiaryType;
	private OptionType lastClickOptionType;
	private int[] lastClickActionId;
	
	public DiaryType getLastClickDiaryType() {
		return lastClickDiaryType;
	}
	
	public OptionType getLastClickOptionType() {
		return lastClickOptionType;
	}
	
	public int[] getLastClickActionId() {
		return lastClickActionId;
	}
	
	public void setLastClickData(DiaryType diaryType, OptionType optionType, int... id) {
		lastClickDiaryType = diaryType;
		lastClickOptionType = optionType;
		lastClickActionId = id;
	}

	public void clearLastClickData() {
		lastClickDiaryType = null;
		lastClickOptionType = null;
		lastClickActionId = null;
	}
	
	Map<String, Integer> diaryProgression = new HashMap<>();
	public Map<String, Integer> getDiaries() {
		validateDiaries();
		return this.diaryProgression;
	}
	
	public Map<String, Integer> getDiaries(String regionName) {
		validateDiaries();
		return this.diaryProgression;
	}
	
	public int getDiaryAmount(String name) {
		if (this.diaryProgression.containsKey(name)) {
			return this.diaryProgression.get(name);
		}
		return 0;
	}
	
	private void validateDiaries() {
		for (Tasks task : Tasks.values())
			if (!diaryProgression.containsKey(task.name()))
				this.diaryProgression.put(task.name(), 0);
	}
	
	public void setDiaries(Map<String, Integer> diaryProgression) {
		this.diaryProgression = diaryProgression;
	}
	
	public void addDiaryTask(String s, int i) {
		if (this.diaryProgression.containsKey(s)) {
			i += this.diaryProgression.get(s);
		}
		this.diaryProgression.put(s, i);
	}

	private AchievementManager achievementManager;
	public AchievementManager getAchievements() {
		if (achievementManager == null)
			achievementManager = new AchievementManager(this);
		return achievementManager;
	}
	
	private House house;
	public House getHouse() {
		if (house == null)
			house = new House(this);
		return house;
	}

	private GameObject ladder = null;
	public void setLadderClicked(GameObject ladder) {
		this.ladder = ladder;
	}
	
	public GameObject getLadderClicked() {
		return ladder;
	}
}
