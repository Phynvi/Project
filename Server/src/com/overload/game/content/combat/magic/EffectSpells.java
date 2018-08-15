package com.overload.game.content.combat.magic;

import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.game.content.skill.cooking.Cookable.Pie;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.teleportation.TeleportHandler;
import com.overload.game.model.teleportation.TeleportType;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;
import com.overload.util.timers.TimerKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Handles spells with special effects.
 */
public class EffectSpells extends ItemIdentifiers {

	public static int getRawPie(Player player) {
		return  player.getInventory().contains(UNCOOKED_BERRY_PIE) 	  ? UNCOOKED_BERRY_PIE 	:
				player.getInventory().contains(UNCOOKED_MEAT_PIE) 	  ? UNCOOKED_MEAT_PIE 	:
				player.getInventory().contains(RAW_MUD_PIE) 	 	  ? RAW_MUD_PIE 	 	:
				player.getInventory().contains(UNCOOKED_APPLE_PIE) 	  ? UNCOOKED_APPLE_PIE 	:
				player.getInventory().contains(RAW_GARDEN_PIE) 	 	  ? RAW_GARDEN_PIE 	 	:
				player.getInventory().contains(RAW_FISH_PIE) 	 	  ? RAW_FISH_PIE	 	:
				player.getInventory().contains(RAW_ADMIRAL_PIE)  	  ? RAW_ADMIRAL_PIE	 	:
				player.getInventory().contains(RAW_WILD_PIE) 	 	  ? RAW_WILD_PIE	 	:
				player.getInventory().contains(RAW_SUMMER_PIE) 	 	  ? RAW_SUMMER_PIE	 	: -1;
	}

	public static int getCookedPie(int raw) {
		return  raw == UNCOOKED_BERRY_PIE ? REDBERRY_PIE :
				raw == UNCOOKED_MEAT_PIE  ? MEAT_PIE 	 :
				raw == RAW_MUD_PIE 	 	  ? MUD_PIE 	 :
				raw == UNCOOKED_APPLE_PIE ? APPLE_PIE 	 :
				raw == RAW_GARDEN_PIE 	  ? GARDEN_PIE 	 :
				raw == RAW_FISH_PIE 	  ? FISH_PIE	 :
				raw == RAW_ADMIRAL_PIE    ? ADMIRAL_PIE	 :
				raw == RAW_WILD_PIE 	  ? WILD_PIE	 :
				raw == RAW_SUMMER_PIE 	  ? SUMMER_PIE	 : -1;
	}
	
    public static boolean handleSpell(Player player, int button) {
        Optional<EffectSpell> spell = EffectSpell.forSpellId(button);
        if (!spell.isPresent()) {
            return false;
        }
        if (!spell.get().getSpell().canCast(player, false)) {
            return true;
        }
        switch (spell.get()) {
            case BAKE_PIES:
                if (!player.getClickDelay().elapsed(500)) {
                    return true;
                }
                int raw = getRawPie(player);
                int cooked = getCookedPie(raw);
                if (raw == -1) {
                    player.getPacketSender().sendMessage("You do not have any pies in your inventory.");
                    return true;
                }
                player.getInventory().deleteItemSet(spell.get().getSpell().itemsRequired(player));
                for (Item invItem : player.getInventory().getValidItems()) {
                    if (invItem.getId() == raw) {
                        player.getInventory().delete(raw, 1).add(cooked, 1);
                        break;
                    }
                }
                player.performGraphic(new Graphic(746, GraphicHeight.MIDDLE));
                player.performAnimation(new Animation(4413));
                player.getSkillManager().addExperience(Skill.MAGIC,   spell.get().getSpell().baseExperience());
                player.getSkillManager().addExperience(Skill.COOKING, Pie.getExperienceFromPrimary(raw));
                player.getClickDelay().reset();
                break;
            case GEOMANCY:
                player.getFarming().getAllotment().inspectSpell();
                break;
            case BOUNTYTELE:
                Optional<Player> player2 = BountyHunter.getTargetFor(player);
                if(!player.getClickDelay().elapsed(500))
                    return true;
                if(player2.isPresent()) {
                    Position tmp = player2.get().getPosition().clone();
                    tmp.addY(Misc.random(6));
                    tmp.addX(Misc.random(6));
                    if (player.getWildernessLevel() > 1 && player2.get().getWildernessLevel() > 1)
                        TeleportHandler.teleport(player, tmp, TeleportType.valueOf(player.getSpellbook().name().toUpperCase()), false);
                }
                break;
            case BONES_TO_PEACHES:
            case BONES_TO_BANANAS:
                if (!player.getClickDelay().elapsed(500)) {
                    return true;
                }
                if (!player.getInventory().contains(526)) {
                    player.getPacketSender().sendMessage("You do not have any bones in your inventory.");
                    return true;
                }
                player.getInventory().deleteItemSet(spell.get().getSpell().itemsRequired(player));
                int i = 0;
                for (Item invItem : player.getInventory().getValidItems()) {
                    if (invItem.getId() == 526) {
                        player.getInventory().delete(526, 1).add(spell.get() == EffectSpell.BONES_TO_PEACHES ? 6883 : 1963, 1);
                        i++;
                    }
                }
                player.performGraphic(new Graphic(141, GraphicHeight.MIDDLE));
                player.performAnimation(new Animation(722));
                player.getSkillManager().addExperience(Skill.MAGIC, spell.get().getSpell().baseExperience() * i);
                player.getClickDelay().reset();
                break;
            case CHARGE:
                if (player.getDueling().inDuel()) {
                    player.getPacketSender().sendMessage("You cannot cast Charge during a duel!");
                    return true;
                }
                if (player.getSkillManager().getMaxLevel(Skill.MAGIC) < 80) {
                    player.getPacketSender().sendMessage("You need at least level 80 Magic to cast this spell.");
                    return true;
                }

                if (player.hasCharge()) {
                    player.getPacketSender().sendMessage("You already boosted your magic.");
                    return true;
                }


                if (!player.getChargeTimer().finished()) {
                    player.getPacketSender().sendMessage("You must wait another " + player.getChargeTimer().secondsRemaining() + " seconds before you can cast that again.");
                    return true;
                }

                //Send message and effect timer to client

                player.setHasCharge(true);
                player.getChargeTimer().start(350);
                player.getPacketSender().sendEffectTimer(30, EffectTimer.CHARGE).sendMessage("You feel charged with magic power.");
                player.getInventory().deleteItemSet(EffectSpell.CHARGE.getSpell().itemsRequired(player));
                player.performAnimation(new Animation(811));
                player.performGraphic(new Graphic(308, 3 , GraphicHeight.HIGH));
                player.getSkillManager().increaseCurrentLevelMax(Skill.MAGIC, (int) Math.floor(2 + (0.12 * player.getSkillManager().getMaxLevel(Skill.MAGIC))));
                break;
            case VENGEANCE:
                if (player.getDueling().inDuel()) {
                    player.getPacketSender().sendMessage("You cannot cast Vengeance during a duel!");
                    return true;
                }
                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                    player.getPacketSender().sendMessage("You need at least level 40 Defence to cast this spell.");
                    return true;
                }

                if (player.hasVengeance()) {
                    player.getPacketSender().sendMessage("You already have Vengeance's effect.");
                    return true;
                }


                if (!player.getVengeanceTimer().finished()) {
                    player.getPacketSender().sendMessage("You must wait another " + player.getVengeanceTimer().secondsRemaining() + " seconds before you can cast that again.");
                    return true;
                }

                //Send message and effect timer to client

                player.setHasVengeance(true);
                player.getVengeanceTimer().start(30);
                player.getPacketSender().sendEffectTimer(30, EffectTimer.VENGEANCE)
                        .sendMessage("You now have Vengeance's effect.");
                player.getInventory().deleteItemSet(EffectSpell.VENGEANCE.getSpell().itemsRequired(player));
                player.performAnimation(new Animation(4410));
                player.performGraphic(new Graphic(726, GraphicHeight.HIGH));
                break;
		default:
			break;
        }
        return true;
    }

    public static enum EffectSpell {
        BONES_TO_BANANAS(new Spell() {

            @Override
            public int spellId() {
                return 1159;
            }

            @Override
            public int levelRequired() {
                return 15;
            }

            @Override
            public int baseExperience() {
                return 650;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(561), new Item(555, 2), new Item(557, 2)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

        }),
        MONSTER_EXAMINE(new Spell() {

            @Override
            public int spellId() {
                return 62052;
            }

            @Override
            public int levelRequired() {
                return 15;
            }

            @Override
            public int baseExperience() {
                return 650;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(ASTRAL_RUNE), new Item(COSMIC_RUNE), new Item(MIND_RUNE)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {
                Player player = cast.getAsPlayer();
                NPC npc = castOn.getAsNpc();
                npc.performGraphic(new Graphic(736, 5));
                npc.getTimers().register(TimerKey.FREEZE, 5);
                TaskManager.submit(new Task(5, player, false) {
                    @Override
                    protected void execute() {
                        player.sendMessage("Name: "         + npc.getDefinition().getName());
                        player.sendMessage("Hitpoints: "    + npc.getDefinition().getHitpoints());
                        player.sendMessage("Stats: Attack/Defence");
                        player.sendMessage("Attack: "       + npc.getDefinition().getStats()[0] + "/" + npc.getDefinition().getStats()[5]);
                        player.sendMessage("Defence: "      + npc.getDefinition().getStats()[1] + "/" + npc.getDefinition().getStats()[6]);
                        player.sendMessage("Stregnth: "     + npc.getDefinition().getStats()[2] + "/" + npc.getDefinition().getStats()[7]);
                        player.sendMessage("Magic: "        + npc.getDefinition().getStats()[3] + "/" + npc.getDefinition().getStats()[8]);
                        player.sendMessage("Ranged: "       + npc.getDefinition().getStats()[4] + "/" + npc.getDefinition().getStats()[9]);
                        player.sendMessage("Max Hit: "      + npc.getDefinition().getMaxHit());
                        player.sendMessage("Attack Style: " + npc.getCombat().getFightType().name());
                        stop();
                    }
                });
            }

        }),
        LOW_ALCHEMY(new Spell() {

            @Override
            public int spellId() {
                return 1162;
            }

            @Override
            public int levelRequired() {
                return 21;
            }

            @Override
            public int baseExperience() {
                return 4000;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(554, 3), new Item(561)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

        }),
        TELEKINETIC_GRAB(new Spell() {

            @Override
            public int spellId() {
                return 1168;
            }

            @Override
            public int levelRequired() {
                return 33;
            }

            @Override
            public int baseExperience() {
                return 3988;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(563), new Item(556)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

        }),
        SUPERHEAT_ITEM(new Spell() {

            @Override
            public int spellId() {
                return 1173;
            }

            @Override
            public int levelRequired() {
                return 43;
            }

            @Override
            public int baseExperience() {
                return 6544;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(554, 4), new Item(561)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {
            }

        }),
        HIGH_ALCHEMY(new Spell() {

            @Override
            public int spellId() {
                return 1178;
            }

            @Override
            public int levelRequired() {
                return 55;
            }

            @Override
            public int baseExperience() {
                return 20000;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(554, 5), new Item(561)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

        }),
        BONES_TO_PEACHES(new Spell() {

            @Override
            public int spellId() {
                return 15877;
            }

            @Override
            public int levelRequired() {
                return 60;
            }

            @Override
            public int baseExperience() {
                return 4121;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(561, 2), new Item(555, 4), new Item(557, 4)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {

            }

        }),
        BAKE_PIES(new Spell() {

            @Override
            public int spellId() {
                return 62013;
            }

            @Override
            public int levelRequired() {
                return 65;
            }

            @Override
            public int baseExperience() {
                return 200;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),
        PLANK_MAKE(new Spell() {

            @Override
            public int spellId() {
                return 62429;
            }

            @Override
            public int levelRequired() {
                return 86;
            }

            @Override
            public int baseExperience() {
                return 200;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(ASTRAL_RUNE), new Item(WATER_RUNE, 15), new Item(NATURE_RUNE, 1)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),
        BOUNTYTELE(new Spell() {

            @Override
            public int spellId() {
                return 12465;
            }

            @Override
            public int levelRequired() {
                return 86;
            }

            @Override
            public int baseExperience() {
                return 200;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(LAW_RUNE), new Item(DEATH_RUNE), new Item(CHAOS_RUNE)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return null;
            }
        }),
        GEOMANCY(new Spell() {

            @Override
            public int spellId() {
                return 62026;
            }

            @Override
            public int levelRequired() {
                return 65;
            }

            @Override
            public int baseExperience() {
                return 300;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(NATURE_RUNE, 3), new Item(ASTRAL_RUNE, 3), new Item(EARTH_RUNE, 8)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),
        VENGEANCE_OTHER(new Spell() {

            @Override
            public int spellId() {
                return 62533;
            }

            @Override
            public int levelRequired() {
                return 93;
            }

            @Override
            public int baseExperience() {
                return 10000;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(9075, 3), new Item(557, 10), new Item(560, 2)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),
        HEAL_OTHER(new Spell() {

            @Override
            public int spellId() {
                return 62520;
            }

            @Override
            public int levelRequired() {
                return 92;
            }

            @Override
            public int baseExperience() {
                return 10000;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(ASTRAL_RUNE, 3), new Item(LAW_RUNE, 3), new Item(BLOOD_RUNE, 1)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {
                Player player = cast.getAsPlayer();
                Player player2 = castOn.getAsPlayer();
                player.performAnimation(new Animation(4411));
                player2.performGraphic(new Graphic(738, 3));
                TaskManager.submit(new Task(3, player, false) {
                    @Override
                    protected void execute() {
                        player2.getSkillManager().setCurrentLevel(Skill.HITPOINTS, player2.getSkillManager().getCurrentLevel(Skill.HITPOINTS));
                        player.sendMessage("Work");
                        stop();
                    }
                });
            }


            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),
        VENGEANCE(new Spell() {

            @Override
            public int spellId() {
                return 62546;// 30306;
            }

            @Override
            public int levelRequired() {
                return 94;
            }

            @Override
            public int baseExperience() {
                return 14000;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{new Item(9075, 4), new Item(557, 10), new Item(560, 2)});
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.LUNAR;
            }
        }),

        CHARGE(new Spell() {

            @Override
            public int spellId() {
                return 12435;// 30306;
            }

            @Override
            public int levelRequired() {
                return 80;
            }

            @Override
            public int baseExperience() {
                return 180;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.of(new Item[]{ new Item(565, 3), new Item(554, 3), new Item(556, 3) });
            }

            @Override
            public Optional<Item[]> equipmentRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public void startCast(Character cast, Character castOn) {


            }

            @Override
            public MagicSpellbook getSpellbook() {
                return MagicSpellbook.NORMAL;
            }
        });

        private static final Map<Integer, EffectSpell> map = new HashMap<Integer, EffectSpell>();

        static {
            for (EffectSpell spell : EffectSpell.values()) {
                map.put(spell.getSpell().spellId(), spell);
            }
        }

        private Spell spell;

        EffectSpell(Spell spell) {
            this.spell = spell;
        }

        public static Optional<EffectSpell> forSpellId(int spellId) {
            EffectSpell spell = map.get(spellId);
            if (spell != null) {
                return Optional.of(spell);
            }
            return Optional.empty();
        }

        public Spell getSpell() {
            return spell;
        }
    }
}
