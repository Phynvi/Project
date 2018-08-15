package com.overload.net.packet.impl;

import com.overload.game.World;
import com.overload.game.collision.RegionManager;
import com.overload.game.content.PetHandler;
import com.overload.game.content.achievement.Diary.DiaryType;
import com.overload.game.content.achievement.Diary.OptionType;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.game.content.combat.magic.CombatSpell;
import com.overload.game.content.combat.magic.CombatSpells;
import com.overload.game.content.combat.method.impl.npcs.VorkathCombatMethod;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.skill.hunter.PuroPuro;
import com.overload.game.content.skill.skillable.impl.Fishing;
import com.overload.game.content.skill.thieving.ThievingNpcTask;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.container.shop.ShopManager;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.dialogue.DialogueOptions;
import com.overload.game.model.movement.WalkToAction;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.NpcIdentifiers;
import com.overload.util.ShopIdentifiers;

public class NPCOptionPacketListener extends NpcIdentifiers implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (player.busy()) {
			return;
		}

		switch (packet.getOpcode()) {
		case PacketConstants.ATTACK_NPC_OPCODE:
			attackNPC(player, packet);
			break;
		case PacketConstants.FIRST_CLICK_NPC_OPCODE:
			firstClick(player, packet);
			break;
		case PacketConstants.SECOND_CLICK_NPC_OPCODE:
			handleSecondClick(player, packet);
			break;
		case PacketConstants.THIRD_CLICK_NPC_OPCODE:
			handleThirdClick(player, packet);
			break;
		case PacketConstants.FOURTH_CLICK_NPC_OPCODE:
			handleFourthClick(player, packet);
			break;
		case PacketConstants.MAGE_NPC_OPCODE:
			mageNpc(player, packet);
			break;
		}
	}

	private static void firstClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity()) {
			return;
		}
		final NPC npc = World.getNpcs().get(index);
		if (npc == null) {
			return;
		}
		if (player.isDebug()) {
			player.getPacketSender().sendMessage("First click NPC: " + Integer.toString(npc.getId()) + ". " + npc.getPosition().toString());
		}

		player.setEntityInteraction(npc);
		player.setWalkToTask(new WalkToAction(player, npc.getPosition(), npc.getSize(), new Action() {
			@Override
			public void execute() {
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
				
				player.setLastClickData(DiaryType.NPC, OptionType.FIRST, npc.getId());

				// Check if we're interacting with our pet..
				if (PetHandler.interact(player, npc)) {
					return;
				}

				if (npc.getDefinition().getName() != null && !npc.getDefinition().getName().toLowerCase().contains("fishing")) {
					if (DialogueHandler.startDialogue(player, npc)) {
				    	return;
                	}
				} else if (npc.getDefinition().getName() != null && npc.getDefinition().getName().toLowerCase().contains("fishing")) {
					player.getSkillManager().startSkillable(new Fishing(npc, Fishing.getSpot(npc.getId(), 1)));
				}

                player.setDialogueAction(-1, -1);
                player.setDialogueState(-1);
                player.setNpcTalking(-1);

				switch (npc.getId()) {
				case SHOP_KEEPER_4:
					ShopManager.open(player, ShopIdentifiers.GENERAL_STORE);
					break;
                case VORKATH_ASLEEP:
                    ((VorkathCombatMethod) npc.getCombatMethod()).awake(player, npc);
                    break;
				case THESSALIA:
					ShopManager.open(player, ShopIdentifiers.THESSALIAS_FINE_CLOTHES);
					break;
				case BANKER_2:
				case TZHAAR_KET_ZUH:
					player.getBank(player.getCurrentBankTab()).open();
					break;
				case MAKE_OVER_MAGE:
					player.getPacketSender().sendInterfaceRemoval().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
					break;
				case SECURITY_GUARD:
					DialogueManager.start(player, 2500);
					break;
				case BABY_IMPLING:
                case YOUNG_IMPLING:
                case GOURMET_IMPLING:
                case EARTH_IMPLING:
                case ESSENCE_IMPLING:
                case ECLECTIC_IMPLING:
                case NATURE_IMPLING:
                case MAGPIE_IMPLING:
                case NINJA_IMPLING:
                case DRAGON_IMPLING:
                case DRAGON_IMPLING_2:
                    PuroPuro.catchImpling(player, npc);
                    break;
//				case EMBLEM_TRADER:
////				case EMBLEM_TRADER_2:
////				case EMBLEM_TRADER_3:
////					player.setDialogueOptions(new DialogueOptions() {
////						@Override
////						public void handleOption(Player player, int option) {
////							switch (option) {
////							case 1:
////								ShopManager.open(player, ShopIdentifiers.BOUNTY_HUNTER_STORE);
////								break;
////							case 2:
////								// Sell emblems option
////								player.setDialogueOptions(new DialogueOptions() {
////									@Override
////									public void handleOption(Player player, int option) {
////										if (option == 1) {
////											int cost = BountyHunter.getValueForEmblems(player, true);
////											player.getPacketSender().sendMessage("@red@You have received " + cost
////													+ " blood money for your emblem(s).");
////											DialogueManager.start(player, 4);
////										} else {
////											player.getPacketSender().sendInterfaceRemoval();
////										}
////									}
////								});
////								int value = BountyHunter.getValueForEmblems(player, false);
////								if (value > 0) {
////									player.setDialogue(DialogueManager.getDialogues().get(10)); // Yes / no option
////									DialogueManager.sendStatement(player,
////											"I will give you " + value + " blood money for those emblems. Agree?");
////								} else {
////									DialogueManager.start(player, 5);
////								}
////								break;
////							case 3:
////								// Skull me option
////								if (player.isSkulled()) {
////									DialogueManager.start(player, 3);
////								} else {
////									DialogueManager.start(player, 22);
////									player.setDialogueOptions(new DialogueOptions() {
////										@Override
////										public void handleOption(Player player, int option) {
////											if (option == 1) {
////												CombatFactory.skull(player, SkullType.WHITE_SKULL, 300);
////											} else if (option == 2) {
////												CombatFactory.skull(player, SkullType.RED_SKULL, 300);
////											}
////											player.getPacketSender().sendInterfaceRemoval();
////										}
////									});
////								}
////								break;
////							case 4:
////								// Cancel option
////								player.getPacketSender().sendInterfaceRemoval();
////								break;
////							}
////						}
////					});
////					break;

				case PERDU:
					// Set dialogue options
					player.setDialogueOptions(new DialogueOptions() {
						@Override
						public void handleOption(Player player, int option) {
							if (option == 1) {

								int cost = BrokenItem.getRepairCost(player);

								player.setDialogueOptions(new DialogueOptions() {
									@Override
									public void handleOption(Player player, int option) {
										if (option == 1) {
											BrokenItem.repair(player);
										} else {
											player.getPacketSender().sendInterfaceRemoval();
										}
									}
								});

								if (cost > 0) {
									player.setDialogue(DialogueManager.getDialogues().get(10)); // Yes / no option
									DialogueManager.sendStatement(player, "It will cost you " + cost
											+ " blood money to fix your broken items. Agree?");
								} else {
									DialogueManager.start(player, 20);
								}

							} else {
								player.getPacketSender().sendInterfaceRemoval();
							}
						}
					});

					// Start main dialogue
					DialogueManager.start(player, 19);
					break;

				case FINANCIAL_ADVISOR:
					DialogueManager.start(player, 15);
					// Removed
					break;

				}
			}
            @Override
            public void execute(Entity entity) {}
		}));
	}

    private void handleSecondClick(Player player, Packet packet) {
		int index = packet.readLEShortA();
		if (index < 0 || index > World.getNpcs().capacity()) {
			return;
		}
		final NPC npc = World.getNpcs().get(index);
		if (npc == null) {
			return;
		}


		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Second click NPC: " + Integer.toString(npc.getId()) + ". " + npc.getPosition().toString());
		}

		player.setEntityInteraction(npc);
		player.setWalkToTask(new WalkToAction(player, npc.getPosition(), npc.getSize(), new Action() {
			@Override
			public void execute() {
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
                player.setLastClickData(DiaryType.NPC, OptionType.SECOND, npc.getId());

                // Check if we're picking up our pet..
                if (PetHandler.pickup(player, npc)) {
                    return;
                }
                
                if (player.getClickDelay().elapsed(1000)) {
                	if (ThievingNpcTask.attemptThieving(player, npc)) {
                		player.getClickDelay().setTime(1000);
                    	return;
                	}
                }
				
				if (npc.getDefinition().getName() != null && npc.getDefinition().getName().toLowerCase().contains("fishing"))
					player.getSkillManager().startSkillable(new Fishing(npc, Fishing.getSpot(npc.getId(), 2)));
				switch (npc.getId()) {
				case BANKER_2:
				case BANKER_3:
				case BANKER_4:
				case BANKER_5:
				case BANKER_6:
				case BANKER_7:
				case TZHAAR_KET_ZUH:
					player.getBank(player.getCurrentBankTab()).open();
					break;
				case RICHARD_2:
					ShopManager.open(player, ShopIdentifiers.TEAM_CAPE_SHOP);
					break;
				case EMBLEM_TRADER:
				case EMBLEM_TRADER_2:
				case EMBLEM_TRADER_3:
					ShopManager.open(player, ShopIdentifiers.PVP_SHOP);
					break;
				case MAGIC_INSTRUCTOR:
					//ShopManager.open(player, ShopIdentifiers.MAGE_ARMOR_SHOP);
					break;

				}
			}
            @Override
            public void execute(Entity entity) {}
		}));
	}

    private void handleThirdClick(Player player, Packet packet) {
		int index = packet.readShort();
		if (index < 0 || index > World.getNpcs().capacity()) {
			return;
		}
		final NPC npc = World.getNpcs().get(index);
		if (npc == null) {
			return;
		}


		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Third click NPC: " + Integer.toString(npc.getId()) + ". " + npc.getPosition().toString());
		}

		player.setEntityInteraction(npc);
		npc.setPositionToFace(player.getPosition());
		player.setWalkToTask(new WalkToAction(player, npc.getPosition(), npc.getSize(), new Action() {
			@Override
			public void execute() {
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
				player.setLastClickData(DiaryType.NPC, OptionType.THIRD, npc.getId());

				if (PetHandler.morph(player, npc)) {
					return;
				}
//                int cost = BountyHunter.getValueForEmblems(player, true);
//                player.getPacketSender().sendMessage("@red@You have received " + cost + " blood money for your emblem(s).");
//            } else {
//                player.getClickDelay().elapsed(500);
//                npc.forceChat("Dear"  + player.getUsername().toLowerCase() +  "u do not have any emblems.");
//            }
				switch (npc.getId()) {
				    case EMBLEM_TRADER:
                        int cost = BountyHunter.getValueForEmblems(player, true);
                        player.getPacketSender().sendMessage("@red@You have received " + cost + " blood money for your emblem(s).");
                        break;
				case MAGIC_INSTRUCTOR:
					ShopManager.open(player, ShopIdentifiers.MAGE_RUNES_SHOP);
					break;
				}
			}
            @Override
            public void execute(Entity entity) {}
		}));
	}

	private void handleFourthClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity()) {
			return;
		}
		final NPC npc = World.getNpcs().get(index);
		if (npc == null) {
			return;
		}

		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Fourth click NPC: " + Integer.toString(npc.getId()) + ". " + npc.getPosition().toString());
		}

		player.setEntityInteraction(npc);
		player.setWalkToTask(new WalkToAction(player, npc.getPosition(), npc.getSize(), new Action() {
			@Override
			public void execute() {
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
				player.setLastClickData(DiaryType.NPC, OptionType.FOURTH, npc.getId());

				switch (npc.getId()) {
				case EMBLEM_TRADER:
				    CombatFactory.skull(player, SkullType.WHITE_SKULL, 300);
					break;
				}
			}
            @Override
            public void execute(Entity entity) {}
		}));
	}

	private static void attackNPC(Player player, Packet packet) {
		int index = packet.readShortA();
		if (index < 0 || index > World.getNpcs().capacity()) {
			return;
		}
		final NPC npc = World.getNpcs().get(index);

		if (npc == null || npc.getDefinition() == null) {
			return;
		}

		player.setLastClickData(DiaryType.NPC, OptionType.ATTACK, npc.getId());

		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Attack NPC: " + npc.getId() + ". " + npc.getPosition().toString());
		}

		if (!npc.getDefinition().isAttackable()) {
			return;
		}

		if (npc == null || npc.getHitpoints() <= 0) {
			player.getMovementQueue().reset();
			return;
		}

		// Make sure there's nothing blocking us
		if (!RegionManager.canMove(npc.getPosition(), Direction.fromDeltas(npc.getX()-player.getX(), npc.getY()-player.getY()).getId())) {
			return;
		}

		player.getCombat().attack(npc);
	}

	private static void mageNpc(Player player, Packet packet) {
		int npcIndex = packet.readLEShortA();
		int spellId = packet.readShortA();

		if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
			return;
		}

		final NPC npc = World.getNpcs().get(npcIndex);

		if (npc == null || npc.getDefinition() == null) {
			return;
		}

		player.setLastClickData(DiaryType.NPC, OptionType.MAGE, npc.getId());

		if (player.isDebug()) {
			player.getPacketSender().sendMessage("Magic on NPC: " + Integer.toString(npc.getId()) + ". " + npc.getPosition().toString());
		}

		if (!npc.getDefinition().isAttackable()) {
			return;
		}

		if (npc == null || npc.getHitpoints() <= 0) {
			player.getMovementQueue().reset();
			return;
		}

		CombatSpell spell = CombatSpells.getCombatSpell(spellId);

		if (spell == null) {
			player.getMovementQueue().reset();
			return;
		}

        if (player.isDebug())
            player.sendMessage("Spell Id: " + spell.spellId());

		player.setPositionToFace(npc.getPosition());
		player.getCombat().setCastSpell(spell);

		if (spell.spellId() == 62052) {
            return;
        }

		player.getCombat().attack(npc);
	}


}
