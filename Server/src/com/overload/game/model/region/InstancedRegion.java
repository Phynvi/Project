package com.overload.game.model.region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.overload.game.entity.impl.Character;
import com.overload.game.Palette;
import com.overload.game.entity.Entity;
import com.overload.game.model.Action;
import com.overload.game.model.areas.Area;
import com.overload.game.model.areas.AreaManager;

public class InstancedRegion {

	/**
	 * Creates this {@link InstancedRegion}.
	 * @param onAdd
	 * @param onRemove
	 * @param bounds
	 * @param palette
	 */
	public InstancedRegion(Optional<Character> owner, Optional<Action> onAdd, Optional<Action> onRemove, Optional<List<Area>> bounds, Optional<Palette> palette) {
		this.owner = owner;
		this.onAdd = onAdd;
		this.onRemove = onRemove;
		this.areas = bounds;
		this.palette = palette;
	}

	/**
	 * The {@link Character} which is the owner of this
	 * instanced region.
	 */
	private Optional<Character> owner = Optional.empty();

	/**
	 * The {@link Entity}s currently in this instanced region.
	 */
	private List<Entity> entities = new ArrayList<Entity>();

	/**
	 * Represents the {@link Action} which will be executed
	 * when a {@link Character} is added to this {@link InstancedRegion}.
	 */
	private Optional<Action> onAdd = Optional.empty();

	/**
	 * Represents the {@link Action} which will be executed
	 * when a {@link Character} is removed from this {@link InstancedRegion}.
	 */
	private Optional<Action> onRemove = Optional.empty();

	/**
	 * The {@link Area} this region is located in. If 
	 * an entity from our list of {@code entities} was to
	 * leave any of the areas, they would be removed from
	 * this {@link InstancedRegion}.
	 */
	private Optional<List<Area>> areas = Optional.empty();

	/**
	 * The {@link Palette} represents the different tiles 
	 * this instanced region has. This can be used for
	 * constructing private map regions.
	 */
	private Optional<Palette> palette = Optional.empty();

	/**
	 * Adds the given entity to this instanced region.
	 * @param entity		The character to add.
	 * @return				Returns this instance.
	 */
	public InstancedRegion addEntity(Entity entity) {
		if (entity.isPlayer()) {
			if(areas.isPresent()) {
				if (owner.isPresent() && entity.isPlayer()) {
					if (entity.getAsPlayer() != owner.get().getAsPlayer()) {
						entity.getAsPlayer().moveTo(owner.get().getAsPlayer().getPosition());
					}
				}
				for(Area area : areas.get()) {
					if(AreaManager.inside(entity.getAsPlayer().getPosition(), area)) {
						if(!entities.contains(entity)) {
							entities.add(entity);
							area.enter(entity.getAsPlayer());
						}
						entity.setInstancedRegion(Optional.of(this));
						onAdd.ifPresent(e -> e.execute(entity));
						if (owner.isPresent() && palette.isPresent() && entity.getAsPlayer() != owner.get().getAsPlayer())
							entity.getAsPlayer().getPacketSender().sendConstructMapRegion(palette.get());
					}
				}
			}
		}
		if (entity.isNpc()) {
			if(areas.isPresent()) {
				for(Area area : areas.get()) {
					if(AreaManager.inside(entity.getAsNpc().getPosition(), area)) {
						if(!entities.contains(entity)) {
							entities.add(entity);
							area.enter(entity.getAsNpc());
						}
						entity.setInstancedRegion(Optional.of(this));
						onAdd.ifPresent(e -> e.execute(entity));
					}
				}
			}
		}
		return this;
	}

	/**
	 * Removes the given character to this instanced region
	 * if it was found in the {@code characters} list.
	 * 
	 * @param entity		The character to remove.
	 * @return				Returns this instance.
	 */
	public InstancedRegion removeEntity(Entity entity) {
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			if(e.equals(entity)) {
				iterator.remove();
			}
		}
		entity.setInstancedRegion(Optional.empty());
		onRemove.ifPresent(e -> e.execute(entity));
		return this;
	}

	/**
	 * Processes this {@link InstancedRegion}
	 * for the given character.
	 */
	public void sequence(Character character) {
		if(areas.isPresent()) {
			for(Area area : areas.get()) {
				//entities.stream().filter(e -> !area.inBounds(e.getPosition())).forEach(e -> removeEntity(e));
				//if(!area.inside(character.getPosition())) {
				if(!AreaManager.inside(character.getPosition(), area)) {
					removeEntity(character);
					area.leave(character);
				} else {
					area.process(character);
				}
			}
		}
	}

	/**
	 * Gets this instanced region's owner.
	 * @return		The owner.
	 */
	public Optional<Character> getOwner() {
		return owner;
	}

	/**
	 * Gets this instanced region's palette
	 * @return 		The palette.
	 */
	public Optional<Palette> getPalette() {
		return palette;
	}
	
	public Optional<List<Area>> getAreas() {
		return areas;
	}
}
