package com.overload.net.packet.impl;

import com.overload.game.content.combat.magic.EffectSpells.EffectSpell;
import com.overload.game.content.combat.magic.spells.SuperheatData;
import com.overload.game.content.skill.construction.Planks;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.Misc;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Handles the packet for using magic spells on items ingame.
 *
 * @author Professor Oak
 */
public class MagicOnItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            case PacketConstants.MAGIC_ON_ITEM_OPCODE:
                int slot = packet.readShort();
                int itemId = packet.readShortA();
                @SuppressWarnings("unused")
				int childId = packet.readShort();
                int spellId = packet.readShortA();
                if (player.isDebug())
                    player.sendMessage("Item: " + itemId + " Spell: " + spellId);
                if (!player.getClickDelay().elapsed(1300))
                    return;
                if (slot < 0 || slot >= player.getInventory().capacity())
                    return;
                if (player.getInventory().getItems()[slot].getId() != itemId)
                    return;
                Optional<EffectSpell> spell = EffectSpell.forSpellId(spellId);
                if (!spell.isPresent()) {
                    return;
                }


                Item item = player.getInventory().getItems()[slot];
                switch (spell.get()) {
                    case LOW_ALCHEMY:
                    case HIGH_ALCHEMY:
                        if (!item.getDefinition().isTradeable() || !item.getDefinition().isSellable() || item.getId() == 995
                                || item.getDefinition().getHighAlchValue() <= 0 || item.getDefinition().getLowAlchValue() <= 0) {
                            player.getPacketSender().sendMessage("This spell can not be cast on this item.");
                            return;
                        }
                        if (!spell.get().getSpell().canCast(player, true)) {
                            return;
                        }
                        player.getInventory().delete(itemId, 1);
                        player.performAnimation(new Animation(712));
                        if (spell.get() == EffectSpell.LOW_ALCHEMY) {
                            player.getInventory().add(995, item.getDefinition().getLowAlchValue());
                        } else {
                            player.getInventory().add(995, item.getDefinition().getHighAlchValue());
                        }
                        player.performGraphic(new Graphic(112, GraphicHeight.HIGH));
                        player.getSkillManager().addExperience(Skill.MAGIC, spell.get().getSpell().baseExperience());
                        player.getPacketSender().sendTab(6);
                        break;

                    case SUPERHEAT_ITEM:
                        if(player.busy()) {
                            return;
                        }
                        if(!player.getClickDelay().elapsed(500)){
                            return;
                        }
                        SuperheatData data = SuperheatData.getItem(player, itemId);

                        if (data == null) {
                            return;
                        }
                        ArrayList<String> required = new ArrayList<String>();

                        for (int i = 0; i < data.getRequired().length; i++) {
                            if (!player.getInventory().contains(data.getRequired()[i][0], data.getRequired()[i][1])) {
                                required.add(data.getRequired()[i][1] + "x " + ItemDefinition.forId(data.getRequired()[i][0]).getName() + " ");
                                continue;
                            }
                        }
                        if (!required.isEmpty()) {
                            String name = ItemDefinition.forId(data.getProduct()).getName();
                            player.sendMessage("You require " + required + " to super heat " + Misc.anOrA(name) + " " + name + ".");
                            return;
                        }
                        if (player.getSkillManager().getCurrentLevel(Skill.SMITHING) < data.getLevel()) {
                            player.sendMessage("You need a smithing level of " + data.getLevel() + " to do this!");
                            return;
                        }
                        for (int i = 0; i < data.getRequired().length; i++) {
                            player.getInventory().delete(data.getRequired()[i][0], data.getRequired()[i][1]);
                        }
                        player.performAnimation(new Animation(722));
                        player.performGraphic(new Graphic(148, 3));
                        player.sendMessage("You have successfully super heated " + ItemDefinition.forId(data.getProduct()).getName() + ".");
                        player.getSkillManager().addExperience(Skill.SMITHING, data.getExperience());
                        player.getInventory().add(data.getProduct(), 1);
                        break;

                    case PLANK_MAKE:
                        if (!player.getClickDelay().elapsed(500)) {
                            return;
                        }
                        if (ItemDefinition.forId(itemId) != null &&
                            ItemDefinition.forId(itemId).getName() != null &&
                            ItemDefinition.forId(itemId).getName().toLowerCase().contains("logs")) {
                                if (!player.getInventory().contains(itemId)) {
                                    player.getPacketSender().sendMessage("You do not have any logs in your inventory.");
                                    return;
                                }
                                player.getInventory().deleteItemSet(spell.get().getSpell().itemsRequired(player));
                                int i2 = 0;

                                Planks plank = Planks.getPlank(itemId);

                                if (plank == null)
                                    return;

                                int amount = player.getInventory().getAmount(plank.getLogId());

                                player.getInventory().delete(plank.getLogId(), amount).add(plank.getPlankId(), amount);

                                player.performGraphic(new Graphic(725, 2, GraphicHeight.HIGH));
                                player.performAnimation(new Animation(4411));
                                player.getSkillManager().addExperience(Skill.MAGIC, spell.get().getSpell().baseExperience() * i2);
                                player.getClickDelay().reset();
                        }
                        break;
                    default:
                        break;
                }
        }
    }
}