package com.overload.net.packet.impl;

import java.util.Optional;

import com.overload.Server;
import com.overload.game.collision.RegionManager;
import com.overload.game.content.CrystalChest;
import com.overload.game.content.achievement.Diary.DiaryType;
import com.overload.game.content.achievement.Diary.OptionType;
import com.overload.game.content.combat.CombatSpecial;
import com.overload.game.content.combat.ranged.DwarfCannonEvent;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.interactable.DoorHandler;
import com.overload.game.content.interactable.LadderHandler;
import com.overload.game.content.interactable.LeverHandler;
import com.overload.game.content.interactable.Shortcut;
import com.overload.game.content.interactable.StairHandler;
import com.overload.game.content.minigames.pestcontrol.PestControl;
import com.overload.game.content.skill.agility.Agility;
import com.overload.game.content.skill.farming.Compost.CompostBinLocations;
import com.overload.game.content.skill.farming.Farming;
import com.overload.game.content.skill.hunter.Hunter;
import com.overload.game.content.skill.hunter.PuroPuro;
import com.overload.game.content.skill.smithing.Smithing.EquipmentMaking;
import com.overload.game.content.skill.smithing.SmithingBarData;
import com.overload.game.content.skill.thieving.HomeStalls;
import com.overload.game.content.skill.thieving.ThievingStallTask;
import com.overload.game.content.skill.thieving.WallSafes;
import com.overload.game.definition.ObjectDefinition;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Animation;
import com.overload.game.model.DwarfCannon;
import com.overload.game.model.ForceMovement;
import com.overload.game.model.Graphic;
import com.overload.game.model.MagicSpellbook;
import com.overload.game.model.Position;
import com.overload.game.model.Skill;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.movement.WalkToAction;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.game.model.teleportation.TeleportType;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.ForceMovementTask;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.ObjectIdentifiers;

/**
 * This packet listener is called when a player clicked on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener extends ObjectIdentifiers implements PacketListener {

	/**
	 * Handles the first click option on an object.
	 *
	 * @param player
	 *            The player that clicked on the object.
	 * @param packet
	 *            The packet containing the object's information.
	 */
	private static void firstClick(final Player player, Packet packet) {
		final int x = packet.readLEShortA();
		final int id = packet.readUnsignedShort();
		final int y = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject object;
		Optional<GameObject> obj = MapObjects.get(id, position);
		if (!obj.isPresent()) {
			obj = ObjectManager.get(id, position);
		}
		
		if (player.isDebug()) {
			player.getPacketSender().sendMessage("First click object: " + Integer.toString(id) + ". " + position.toString());
		}
		
		// Make sure the object actually exists in the region...
		if (obj == null || !obj.isPresent()) {
			Server.getLogger().info("Object with id " + id + " does not exist!");
			return;
		}
		object = obj.get();
		
		// Get object definition
		final ObjectDefinition def = ObjectDefinition.forId(id);
		if (def == null) {
			Server.getLogger().info("ObjectDefinition for object " + id + " is null.");
			return;
		}
		
		// Face object..
		player.setPositionToFace(position);

		player.setWalkToTask(
				new WalkToAction(player, position, def.getSize(), def.getSizeX(), def.getSizeY(), new Action() {
					@Override
					public void execute() {
						
						player.setLastClickData(DiaryType.OBJECT, OptionType.FIRST, id);
						
						if (!RegionManager.canMove(object.getPosition(), object.getPosition(), object.getDefinition().getSizeX(), object.getDefinition().getSizeY())) {
							player.sendMessage("You can't reach that.");
							return;
						}
						
						if (Shortcut.handle(player, object)) {
							return;
						}

						if (def.getName() != null) {
							if ((def.getName().toLowerCase().contains("door") || def.getName().toLowerCase().contains("gate")) && !def.getName().toLowerCase().contains("trap")) {
								if (DoorHandler.clickDoor(player, object))
									return;
							}
							if (def.getName().toLowerCase().contains("ladder") || def.getName().toLowerCase().contains("trap")) {
								if (LadderHandler.climb(player, object, Optional.empty()))
									return;
							}
							if (def.getName().toLowerCase().contains("stair")) {
								if (StairHandler.climb(player, object, Optional.empty()))
									return;
							}
						}
						
						if (LeverHandler.handle(player, object)) {
							return;
						}
						
						// Skills..
						if (player.getSkillManager().startSkillable(object)) {
							return;
						}

                        if(Agility.handleObject(player, object)) {
                            return;
                        }

                        if (PestControl.handleObject(player, object.getId())) {
						    return;
                        }
                        
                        if (Farming.handleClickOption("first", player, x, y, null)) {
                        	return;
                        }
                        
                        CompostBinLocations cbl = CompostBinLocations.forPosition(x, y);
                        if (cbl != null) {
	                        player.getCompost().openClose(cbl.getCompostIndex(), x, y);
                        	return;
                        }

						// Areas
						if (player.getArea() != null) {
							if (player.getArea().handleObjectClick(player, object.getId(), 1)) {
								return;
							}
						}

						switch (id) {
                            case 7236:
                                WallSafes.crack(player);
                                break;
						case 27853:
						case 27854://Home Stairs Climb Up
							player.moveTo(new Position(1635, 3737, 1));
							break;
						case 27855:
						case 27856://Home Stairs Climb Down
							player.moveTo(new Position(1635, 3740, 0));
							break;
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfCannonEvent.startFiringCannon(player, cannon);
                                }
                                break;
						case 172:
							CrystalChest.searchChest(player);
							break;
						case KBD_LADDER_DOWN:
							TeleportHandler.teleport(player, new Position(3069, 10255), TeleportType.LADDER_DOWN, false);
							break;
						case KBD_LADDER_UP:
							TeleportHandler.teleport(player, new Position(3017, 3850), TeleportType.LADDER_UP, false);
							break;
						case KBD_ENTRANCE_LEVER:
							if (!player.getCombat().getTeleBlockTimer().finished()) {
								player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
								return;
							}
							TeleportHandler.teleport(player, new Position(2271, 4680), TeleportType.LEVER, false);
							break;
						case KBD_EXIT_LEVER:
							TeleportHandler.teleport(player, new Position(3067, 10253), TeleportType.LEVER, false);
							break;
						case FIGHT_CAVES_ENTRANCE:
							player.getFightCave().create();
							break;
                        case 9380:
                        case 9345:
                            Hunter.dismantle(player, object);
                            break;
                        case 25016:
                        case 25017:
                        case 25018:
                        case 25029:
                            PuroPuro.goThroughWheat(player, object);
                            break;
                        case 9373:
                        case 9377:
                        case 9379:
                        case 9375:
                        case 9348:
                        case 9384:
                        case 9382:
                            Hunter.lootTrap(player, object);
                            break;
						case FIGHT_CAVES_EXIT:
							player.getFightCave().leaveGame();
							break;
						case PORTAL_51:
							DialogueManager.sendStatement(player, "Construction will be avaliable in the future.");
							break;
						case ANVIL:
							EquipmentMaking.openInterface(player);
							break;
						case ALTAR:
						case CHAOS_ALTAR_2:
							player.performAnimation(new Animation(645));
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
									.getMaxLevel(Skill.PRAYER)) {
								player.getSkillManager().setCurrentLevel(Skill.PRAYER,
										player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
								player.getPacketSender().sendMessage("You recharge your Prayer points.");
							}
							break;

						case BANK_CHEST:
							player.getBank(player.getCurrentBankTab()).open();
							break;

						case DITCH_PORTAL:
							player.getPacketSender().sendMessage("You are teleported to the Wilderness ditch.");
							player.moveTo(new Position(3087, 3520));
							break;

						case WILDERNESS_DITCH:
							player.getMovementQueue().reset();
							if (player.getForceMovement() == null && player.getClickDelay().elapsed(2000)) {
								final Position crossDitch = new Position(0,
										player.getPosition().getY() < 3522 ? 3 : -3);
								TaskManager.submit(
										new ForceMovementTask(player, 3, new ForceMovement(player.getPosition().clone(),
												crossDitch, 0, 70, crossDitch.getY() == 3 ? 0 : 2, 6132)));
								player.getClickDelay().reset();
							}
							break;

						case MAGICAL_ALTAR:
							DialogueHandler.sendOptions(player, "Please select a new Spellbook", new String[] {
									"Normal",
									"Ancient",
									"Lunar",
									"Arceuus",
									"Cancel"
							});
                            player.setDialogueState(1);
                            player.setNpcTalking(-10);
                            player.setDialogueClickType(DialogueHandler.GLOBAL_OBJECT_CLICK);
                            player.setDialogueClickId(MAGICAL_ALTAR);
							break;
							
						case SACRIFICIAL_BOAT:
							DialogueHandler.sendOptions(player, "Return to Zulrah's shrine?", new String[] {
									"Yes",
									"No"
							});
							player.setDialogueState(1);
							player.setNpcTalking(-10);
							player.setDialogueClickType(DialogueHandler.GLOBAL_OBJECT_CLICK);
							player.setDialogueClickId(SACRIFICIAL_BOAT);
							break;

						case ORNATE_REJUVENATION_POOL:
							player.getPacketSender().sendMessage("You feel slightly renewed.");
							player.performGraphic(new Graphic(683));
							player.setSpecialPercentage(100);
				            CombatSpecial.updateBar(player);
							player.resetAttributes();
							break;

						}
					}

                    @Override
                    public void execute(Entity entity) {}
				}));
	}

	/**
	 * Handles the second click option on an object.
	 *
	 * @param player
	 *            The player that clicked on the object.
	 * @param packet
	 *            The packet containing the object's information.
	 */
	private static void secondClick(final Player player, Packet packet) {
		final int id = packet.readLEShortA();
		final int y = packet.readLEShort();
		final int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final Optional<GameObject> object = MapObjects.get(id, position);


		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Second click object: " + Integer.toString(id) + ". " + position.toString());
		}

		// Make sure the object actually exists in the region...
		if (!object.isPresent()) {
			Server.getLogger().info("Object with id " + id + " does not exist!");
			return;
		}

		// Get object definition
		final ObjectDefinition def = ObjectDefinition.forId(id);
		if (def == null) {
			Server.getLogger().info("ObjectDefinition for object " + id + " is null.");
			return;
		}

		// Face object..
		player.setPositionToFace(position);

		player.setWalkToTask(
				new WalkToAction(player, position, def.getSize(), def.getSizeX(), def.getSizeY(), new Action() {
					public void execute() {
						
						if (def.getName() != null) {
							if (def.getName().toLowerCase().contains("ladder") || def.getName().toLowerCase().contains("trapdoor"))
								if (LadderHandler.climb(player, object.get(), Optional.of("up")))
									return;

							if (def.getName().toLowerCase().contains("stair")) {
								if (StairHandler.climb(player, object.get(), Optional.of("up")))
									return;
							}
						}
						
						player.setLastClickData(DiaryType.OBJECT, OptionType.SECOND, id);
						
						// Check thieving..
//						if (StallThieving.init(player, object.get())) {
//							return;
//						}

						// Areas
						if (player.getArea() != null) {
							if (player.getArea().handleObjectClick(player, object.get().getId(), 2)) {
								return;
							}
						}
                        
//                        if (Farming.inspectObject(player, x, y)) {
//                        	return;
//                        }

                        if (Farming.handleClickOption("second", player, x, y, null)) {
                        	return;
                    	}
                        ThievingStallTask.attemptStealFromStall(player, id, new Position(x, y, player.getZ()));
                        HomeStalls.attempt(player, id, position);

						switch (id) {
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfCannonEvent.pickupCannon(player, cannon, false);
                                }
                                break;
						case PORTAL_51:
							DialogueManager.sendStatement(player, "Construction will be avaliable in the future.");
							break;
						case SPINNING_WHEEL:
						case SPINNING_WHEEL_2:
						case SPINNING_WHEEL_3:
						case SPINNING_WHEEL_4:
						case SPINNING_WHEEL_5:
						case SPINNING_WHEEL_6:
						case SPINNING_WHEEL_7:
							player.sendMessage("@dre@We are aware this doesn't work... Crafting is being reworked!");
							break;
						case FURNACE:
						case FURNACE_2:
						case FURNACE_3:
						case FURNACE_4:
						case FURNACE_5:
						case FURNACE_6:
						case FURNACE_7:
						case FURNACE_8:
						case FURNACE_9:
						case FURNACE_10:
						case FURNACE_11:
						case FURNACE_12:
						case FURNACE_13:
						case FURNACE_14:
						case FURNACE_15:
						case FURNACE_16:
						case FURNACE_17:
						case FURNACE_18:
						case FURNACE_19:
						case FURNACE_20:
						case FURNACE_21:
						case FURNACE_22:
							for (SmithingBarData.Bar bar : SmithingBarData.Bar.values()) {
								player.getPacketSender().sendInterfaceModel(bar.getFrame(), bar.getBar(), 150);
							}
							player.getPacketSender().sendChatboxInterface(2400);
							break;
						case BANK_CHEST:
						case BANK:
						case BANK_BOOTH:
						case BANK_BOOTH_2:
						case BANK_BOOTH_3:
						case BANK_BOOTH_4:
							player.getBank(player.getCurrentBankTab()).open();
							break;
						case MAGICAL_ALTAR:
							player.getPacketSender().sendInterfaceRemoval();
							MagicSpellbook.changeSpellbook(player, MagicSpellbook.ANCIENT);
							break;
						}
					}

                    @Override
                    public void execute(Entity entity) {}
				}));
	}

	/**
	 * Handles the third click option on an object.
	 *
	 * @param player
	 *            The player that clicked on the object.
	 * @param packet
	 *            The packet containing the object's information.
	 */
	private static void thirdClick(Player player, Packet packet) {
		final int x = packet.readLEShort();
		final int y = packet.readShort();
		final int id = packet.readLEShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final Optional<GameObject> object = MapObjects.get(id, position);


		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Third click object: " + Integer.toString(id) + ". " + position.toString());
		}

		// Make sure the object actually exists in the region...
		if (!object.isPresent()) {
			Server.getLogger().info("Object with id " + id + " does not exist!");
			return;
		}

		// Get object definition
		final ObjectDefinition def = ObjectDefinition.forId(id);
		if (def == null) {
			Server.getLogger().info("ObjectDefinition for object " + id + " is null.");
			return;
		}

		// Face object..
		player.setPositionToFace(position);

		player.setWalkToTask(
				new WalkToAction(player, position, def.getSize(), def.getSizeX(), def.getSizeY(), new Action() {
					public void execute() {
						
						if (def.getName() != null) {
							if (def.getName().toLowerCase().contains("ladder") || def.getName().toLowerCase().contains("trapdoor"))
								if (LadderHandler.climb(player, object.get(), Optional.of("down")))
									return;

							if (def.getName().toLowerCase().contains("stair")) {
								if (StairHandler.climb(player, object.get(), Optional.of("down")))
									return;
							}
						}
						
						player.setLastClickData(DiaryType.OBJECT, OptionType.THIRD, id);

						// Areas
						if (player.getArea() != null) {
							if (player.getArea().handleObjectClick(player, object.get().getId(), 3)) {
								return;
							}
						}

						switch (id) {
						case PORTAL_51:
							DialogueManager.sendStatement(player, "Construction will be avaliable in the future.");
							break;
						case MAGICAL_ALTAR:
							player.getPacketSender().sendInterfaceRemoval();
							MagicSpellbook.changeSpellbook(player, MagicSpellbook.LUNAR);
							break;
						}
					}
                    @Override
                    public void execute(Entity entity) {}
				}));
	}

	/**
	 * Handles the fourth click option on an object.
	 *
	 * @param player
	 *            The player that clicked on the object.
	 * @param packet
	 *            The packet containing the object's information.
	 */
	private static void fourthClick(Player player, Packet packet) {
		final int x = packet.readLEShortA();
		final int id = packet.readUnsignedShortA();
		final int y = packet.readLEShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final Optional<GameObject> object = MapObjects.get(id, position);


		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Fourth click object: " + Integer.toString(id) + ". " + position.toString());
		}

		// Make sure the object actually exists in the region...
		if (!object.isPresent()) {
			Server.getLogger().info("Object with id " + id + " does not exist!");
			return;
		}

		// Get object definition
		final ObjectDefinition def = ObjectDefinition.forId(id);
		if (def == null) {
			Server.getLogger().info("ObjectDefinition for object " + id + " is null.");
			return;
		}

		// Face object..
		player.setPositionToFace(position);

		player.setWalkToTask(
				new WalkToAction(player, position, def.getSize(), def.getSizeX(), def.getSizeY(), new Action() {
					public void execute() {
						player.setLastClickData(DiaryType.OBJECT, OptionType.FOURTH, id);

						// Areas
						if (player.getArea() != null) {
							if (player.getArea().handleObjectClick(player, object.get().getId(), 4)) {
								return;
							}
						}

						switch (id) {
						case PORTAL_51:
							DialogueManager.sendStatement(player, "Construction will be avaliable in the future.");
							break;
						case MAGICAL_ALTAR:
							player.getPacketSender().sendInterfaceRemoval();
							MagicSpellbook.changeSpellbook(player, MagicSpellbook.ARCEUUS);
							break;
						}
					}
                    @Override
                    public void execute(Entity entity) {}
				}));
	}

	/**
	 * Handles the fifth click option on an object.
	 *
	 * @param player
	 *            The player that clicked on the object.
	 * @param packet
	 *            The packet containing the object's information.
	 */
	private static void fifthClick(final Player player, Packet packet) {

	}

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (player == null || player.getHitpoints() <= 0) {
			return;
		}

		// Make sure we aren't doing something else..
		if (player.busy()) {
			return;
		}

		switch (packet.getOpcode()) {
		case PacketConstants.OBJECT_FIRST_CLICK_OPCODE:
			firstClick(player, packet);
			break;
		case PacketConstants.OBJECT_SECOND_CLICK_OPCODE:
			secondClick(player, packet);
			break;
		case PacketConstants.OBJECT_THIRD_CLICK_OPCODE:
			thirdClick(player, packet);
			break;
		case PacketConstants.OBJECT_FOURTH_CLICK_OPCODE:
			fourthClick(player, packet);
			break;
		case PacketConstants.OBJECT_FIFTH_CLICK_OPCODE:
			fifthClick(player, packet);
			break;
		}
	}
}
