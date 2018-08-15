package com.overload.game.entity.impl;

import com.overload.game.content.combat.Combat;
import com.overload.game.content.combat.CombatType;
import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.PendingHit;
import com.overload.game.entity.Attributes;
import com.overload.game.entity.Entity;
import com.overload.game.model.Animation;
import com.overload.game.model.Boundary;
import com.overload.game.model.Direction;
import com.overload.game.model.Flag;
import com.overload.game.model.Graphic;
import com.overload.game.model.NodeType;
import com.overload.game.model.Position;
import com.overload.game.model.UpdateFlag;
import com.overload.game.model.areas.Area;
import com.overload.game.model.movement.MovementQueue;
import com.overload.util.Stopwatch;

/**
 * A player or NPC
 *
 * @author Swiffy
 */

public abstract class Character extends Entity {
	
	private final Combat combat = new Combat(this);
	private final MovementQueue movementQueue = new MovementQueue(this);
	private String forcedChat;
	private Direction walkingDirection = Direction.NONE, runningDirection = Direction.NONE;
	private Stopwatch lastCombat = new Stopwatch();
	private UpdateFlag updateFlag = new UpdateFlag();
	private Position positionToFace;
	private Animation animation;
	private Graphic graphic;

	/*
	 * Fields
	 */
    private final Attributes attributes = new Attributes();
	private Entity interactingEntity;
	private int npcTransformationId = -1;
	private int poisonDamage;
	private boolean[] prayerActive = new boolean[30], curseActive = new boolean[20];
	private boolean resetMovementQueue;
	private boolean needsPlacement;
	private boolean untargetable;
	private boolean hasVengeance;
    private boolean hasCharge;
	private int specialPercentage = 100;
	private boolean specialActivated;
	private boolean recoveringSpecialAttack;
	private boolean isTeleporting = false;
	// Area
	private Area area;
	private HitDamage primaryHit;
	private HitDamage secondaryHit;
	/**
	 * Is this entity registered.
	 */
	private boolean registered;

	/**
	 * Constructs this character/entity
	 *
	 * @param position
	 */
	public Character(NodeType type, Position position) {
		super(type, position);
	}

	/**
	 * An abstract method used for handling actions once this entity has been added
	 * to the world.
	 */
	public abstract void onAdd();

	/**
	 * An abstract method used for handling actions once this entity has been
	 * removed from the world.
	 */
	public abstract void onRemove();

	public abstract PendingHit manipulateHit(PendingHit hit);

	/**
	 * Teleports the character to a target location
	 *
	 * @param teleportTarget
	 * @return
	 */
	public Character moveTo(Position teleportTarget) {
		getMovementQueue().reset();
		setPosition(teleportTarget.clone());
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setEntityInteraction(null);
		if (isPlayer()) {
			getMovementQueue().handleRegionChange();
		}
		return this;
	}
	
	public Character getNewRegionPosition(Position regionTopRight) {
		getMovementQueue().reset();
		Position pos = regionTopRight.clone();
		pos.setX(pos.getX()-24);
		pos.setY(pos.getY()-24);
		setPosition(pos);
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setEntityInteraction(null);
		if (isPlayer()) {
			getMovementQueue().handleRegionChange();
		}
		return this;
	}

	/**
	 * Resets all flags related to updating.
	 */
	public void resetUpdating() {
		getUpdateFlag().reset();
		walkingDirection = Direction.NONE;
		runningDirection = Direction.NONE;
		needsPlacement = false;
		resetMovementQueue = false;
		forcedChat = null;
		interactingEntity = null;
		positionToFace = null;
		animation = null;
		graphic = null;
	}

	public Character forceChat(String message) {
		setForcedChat(message);
		getUpdateFlag().flag(Flag.FORCED_CHAT);
		return this;
	}

	public Character setEntityInteraction(Entity entity) {
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}

    @Override
    public void performAnimation(Animation animation) {
        if (this.animation != null && animation != null) {
            if (this.animation.getPriority().ordinal() > animation.getPriority().ordinal()) {
                return;
            }
        }

        this.animation = animation;
        getUpdateFlag().flag(Flag.ANIMATION);
    }
    @Override
    public void performAnimation(int animation) {
        performAnimation(new Animation(animation));
    }

    public Attributes getAttributes() {
        return attributes;
    }

	@Override
	public void performGraphic(Graphic graphic) {
		if (this.graphic != null && graphic != null) {
			if (this.graphic.getPriority().ordinal() > graphic.getPriority().ordinal()) {
				return;
			}
		}

		this.graphic = graphic;
		getUpdateFlag().flag(Flag.GRAPHIC);
	}

	public abstract void appendDeath();

	public abstract void heal(int damage);

	public abstract int getHitpoints();

	public abstract Character setHitpoints(int hitpoints);

	public abstract int getBaseAttack(CombatType type);

	public abstract int getBaseDefence(CombatType type);

	public abstract int getBaseAttackSpeed();

	public abstract int getAttackAnim();

	public abstract int getBlockAnim();

	/*
	 * Getters and setters Also contains methods.
	 */

	public boolean isTeleporting() {
		return isTeleporting;
	}

	public void setTeleporting(boolean isTeleporting) {
		this.isTeleporting = isTeleporting;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public Animation getAnimation() {
		return animation;
	}

	/**
	 * @return the lastCombat
	 */
	public Stopwatch getLastCombat() {
		return lastCombat;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}

	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public boolean isPoisoned() {
		return poisonDamage > 0;
	}

	public Position getPositionToFace() {
		return positionToFace;
	}

	public Character setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}

	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}

	public MovementQueue getMovementQueue() {
		return movementQueue;
	}

	public Combat getCombat() {
		return combat;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	public void setDirection(Direction direction) {
		setPositionToFace(getPosition().clone().add(direction.getX(), direction.getY()));
	}

	public String getForcedChat() {
		return forcedChat;
	}

	public Character setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public Character setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}

	public boolean[] getCurseActive() {
		return curseActive;
	}

	public Character setCurseActive(boolean[] curseActive) {
		this.curseActive = curseActive;
		return this;
	}

	public Character setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}

	public Character setCurseActive(int id, boolean curseActive) {
		this.curseActive[id] = curseActive;
		return this;
	}

	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public Character setNpcTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		getUpdateFlag().flag(Flag.APPEARANCE);
		return this;
	}

	public HitDamage decrementHealth(HitDamage hit) {
		if (getHitpoints() <= 0)
			return hit;
		if (hit.getDamage() > getHitpoints())
			hit.setDamage(getHitpoints());
		if (hit.getDamage() < 0)
			hit.setDamage(0);
		int outcome = getHitpoints() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		setHitpoints(outcome);
		return hit;
	}
	
	public Boundary getBoundary() {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int size = (getSize() - 1);
		return new Boundary(x, x + size, y, y + size);
	}

	/**
	 * Get the primary hit for this entity.
	 *
	 * @return the primaryHit.
	 */
	public HitDamage getPrimaryHit() {
		return primaryHit;
	}

	public void setPrimaryHit(HitDamage hit) {
		this.primaryHit = hit;
	}

	/**
	 * Get the secondary hit for this entity.
	 *
	 * @return the secondaryHit.
	 */
	public HitDamage getSecondaryHit() {
		return secondaryHit;
	}

	public void setSecondaryHit(HitDamage hit) {
		this.secondaryHit = hit;
	}

	/*
	 * Movement queue
	 */

	public Direction getWalkingDirection() {
		return walkingDirection;
	}

	public void setWalkingDirection(Direction walkDirection) {
		this.walkingDirection = walkDirection;
	}

	public Direction getRunningDirection() {
		return runningDirection;
	}

	public void setRunningDirection(Direction runDirection) {
		this.runningDirection = runDirection;
	}

	/**
	 * Determines if this character needs to reset their movement queue.
	 *
	 * @return {@code true} if this character needs to reset their movement queue,
	 *         {@code false} otherwise.
	 */
	public final boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	/**
	 * Sets the value for {@link CharacterNode#resetMovementQueue}.
	 *
	 * @param resetMovementQueue
	 *            the new value to set.
	 */
	public final void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	/**
	 * Gets if this entity is registered.
	 *
	 * @return the unregistered.
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Sets if this entity is registered,
	 *
	 * @param unregistered
	 *            the unregistered to set.
	 */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isNeedsPlacement() {
		return needsPlacement;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean hasVengeance() {
		return hasVengeance;
	}

	public void setHasVengeance(boolean hasVengeance) {
		this.hasVengeance = hasVengeance;
	}

    public boolean hasCharge() {
        return hasCharge;
    }

    public void setHasCharge(boolean hasCharge) {
        this.hasCharge = hasCharge;
    }

	public boolean isSpecialActivated() {
		return specialActivated;
	}

	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}

	public int getSpecialPercentage() {
		return specialPercentage;
	}

	public void setSpecialPercentage(int specialPercentage) {
		this.specialPercentage = specialPercentage;
	}

	public void decrementSpecialPercentage(int drainAmount) {
		this.specialPercentage -= drainAmount;

		if (specialPercentage < 0) {
			specialPercentage = 0;
		}
	}

	public void incrementSpecialPercentage(int gainAmount) {
		this.specialPercentage += gainAmount;

		if (specialPercentage > 100) {
			specialPercentage = 100;
		}
	}

	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
	}

	public boolean isUntargetable() {
		return untargetable;
	}

	public void setUntargetable(boolean untargetable) {
		this.untargetable = untargetable;
	}

	public boolean inDungeon() {
		return false;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}