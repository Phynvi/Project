package com.overload.game.entity;

import java.util.Optional;

import com.overload.game.GameConstants;
import com.overload.game.World;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Graphic;
import com.overload.game.model.NodeType;
import com.overload.game.model.Position;
import com.overload.game.model.Projectile;
import com.overload.game.model.region.InstancedRegion;
import com.overload.util.timers.TimerRepository;

public abstract class Entity {

    /**
     * The entity's type.
     */
    private NodeType type;
    /**
     * The entity's unique index.
     */
    private int index;
    /**
     * The entity's tile size.
     */
    private int size = 1;
    /**
     * The default position the entity is in.
     */
    private Position position = GameConstants.DEFAULT_POSITION.clone();
    /**
     * The entity's first position in current map region.
     */
    private Position lastKnownRegion;
    
    /**
     * The {@link TimerRepository} which manages all of the
     * timers/delays for this {@link Entity}.
     */
    private final TimerRepository timers = new TimerRepository();

    /**
     * The Entity constructor.
     *
     * @param position The position the entity is currently in.
     */
    public Entity(NodeType type, Position position) {
        this.position = position;
        this.type = type;
        this.lastKnownRegion = position;
    }

    /**
     * Gets this type.
     *
     * @return
     */
    public NodeType getNodeType() {
        return type;
    }

    /**
     * Gets the entity's unique index.
     *
     * @return The entity's index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the entity's index.
     *
     * @param index The value the entity's index will contain.
     * @return The Entity instance.
     */
    public Entity setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets this entity's first position upon entering their
     * current map region.
     *
     * @return The lastKnownRegion instance.
     */
    public Position getLastKnownRegion() {
        return lastKnownRegion;
    }

    /**
     * Sets the entity's current region's position.
     *
     * @param lastKnownRegion The position in which the player first entered the current region.
     * @return The Entity instance.
     */
    public Entity setLastKnownRegion(Position lastKnownRegion) {
        this.lastKnownRegion = lastKnownRegion;
        return this;
    }

    /**
     * Gets the entity position.
     *
     * @return the entity's world position
     */
    public Position getPosition() {
        return position;
    }
    
    public int getX() {
    	return getPosition().getX();
    }
    
    public int getY() {
    	return getPosition().getY();
    }
    
    public int getZ() {
    	return getPosition().getZ();
    }

    /**
     * Sets the entity position
     *
     * @param position the world position
     */
    public Entity setPosition(Position position) {
        this.position = position;
        return this;
    }

    /**
     * Performs an animation.
     *
     * @param animation The animation to perform.
     */
    public void performAnimation(Animation animation) {

    }
    public void performAnimation(int animation) {

    }

    /**
     * Performs a graphic.
     *
     * @param graphic The graphic to perform.
     */
    public void performGraphic(Graphic graphic) {

    }

    /**
     * gets the entity's tile size.
     *
     * @return The size the entity occupies in the world.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the entity's tile size
     *
     * @return The Entity instance.
     */
    public Entity setSize(int size) {
        this.size = size;
        return this;
    }
    
    /**
	 * The character's current {@link InstancedRegion}.
	 */
	private Optional<InstancedRegion> instancedRegion = Optional.empty();
	
	public Optional<InstancedRegion> getInstancedRegion() {
		return instancedRegion;
	}
	
	public void setInstancedRegion(Optional<InstancedRegion> instancedRegion) {
		this.instancedRegion = instancedRegion;
	}

    public boolean isNpc() {
        return this instanceof NPC;
    }

    public boolean isPlayer() {
        return this instanceof Player;
    }

    public boolean isItemOnGround() {
        return this instanceof ItemOnGround;
    }

    public boolean isGameObject() {
        return this instanceof GameObject;
    }

    public Player getAsPlayer() {
        return ((Player) this);
    }

    public NPC getAsNpc() {
        return ((NPC) this);
    }

    public ItemOnGround getAsItem() {
        return ((ItemOnGround) this);
    }

    public GameObject getAsObject() {
        return ((GameObject) this);
    }

	public TimerRepository getTimers() {
		return timers;
	}
	
	public int executeProjectile(Projectile projectile) {
		if (projectile == null) {
			return 0;
		}
		Position source = projectile.getStart();
		Position target = projectile.getTarget();
		if (source == null || target == null) {
			return 0;
		}
		Position delta = projectile.getTarget().getDelta(source);

		int distance = source.getDistance(target);
		int duration = projectile.getDuration(distance);

		if (distance <= 60) {
			int creatorSize = projectile.getCreatorSize() == -1 ? getSize() : projectile.getCreatorSize();

			for (Player player : World.getPlayers()) {
				if (player == null) {
					continue;
				}

				if (source.isViewableFrom(player.getPosition())) {
					player.getPacketSender().sendProjectile(source, delta, projectile.getCurve(), duration, projectile.getProjectileID(), projectile.getStartHeight(), projectile.getEndHeight(), projectile.getLockon(), projectile.getDelay(), creatorSize, projectile.getStartDistanceOffset());
				}
			}
		}
		return projectile.getHitDelay(distance);
	}
}
