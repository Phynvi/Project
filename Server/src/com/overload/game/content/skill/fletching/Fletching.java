package com.overload.game.content.skill.fletching;

import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.menu.CreationMenu.CreationMenuAction;
import com.overload.game.model.menu.impl.QuardrupleItemCreationMenu;
import com.overload.game.model.menu.impl.SingleItemCreationMenu;
import com.overload.game.model.menu.impl.TripleItemCreationMenu;
import com.overload.util.ItemIdentifiers;

import java.util.Arrays;
import java.util.Optional;

public class Fletching extends ItemIdentifiers {

    public static boolean fletchAmmo(Player player, int itemUsed, int itemUsedWith) {
        for (FletchAmmo.FletchableAmmo ammo : FletchAmmo.FletchableAmmo.values()) {
            if ((ammo.getItem1() == itemUsed || ammo.getItem1() == itemUsedWith)
                    && (ammo.getItem2() == itemUsed || ammo.getItem2() == itemUsedWith)) {
                if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= ammo.getLevelReq()) {
                    if (player.getInventory().getAmount(ammo.getItem1()) >= 10 && player.getInventory().getAmount(ammo.getItem2()) >= 10) {
                        player.getInventory().delete(ammo.getItem1(), 10);
                        player.getInventory().delete(ammo.getItem2(), 10);
                        player.getInventory().add(ammo.getOutcome(), 10);
                        player.getSkillManager().addExperience(Skill.FLETCHING, ammo.getXp());
                        String name = ItemDefinition.forId(ammo.getOutcome()).getName();
                        if (!name.endsWith("s"))
                            name += "s";
                        player.getPacketSender().sendMessage("You make some " + name + ".");
                    } else {
                        player.getPacketSender().sendMessage("You must have at least 10 of each supply when fletching a set.");
                    }
                } else {
                    player.getPacketSender().sendMessage("You need a Fletching level of at least " + ammo.getLevelReq() + " to fletch this.");
                }
                return true;
            }
        }
        return false;
    }

    public static boolean fletchCrossbow(Player player, int itemUsed, int itemUsedWith) {
        for (FletchCrossBow.FletchableCrossbow c : FletchCrossBow.FletchableCrossbow.values()) {
            if ((c.getStock() == itemUsed || c.getStock() == itemUsedWith)
                    && (c.getLimbs() == itemUsed || c.getLimbs() == itemUsedWith)) {
                player.setCreationMenu(Optional.of(new SingleItemCreationMenu(player, c.getUnstrung(), "How many would you like to make?", new CreationMenuAction() {
                    @Override
                    public void execute(int item, int amount) {
                        player.getSkillManager().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(c.getStock()), true), new RequiredItem(new Item(c.getLimbs()), true)), new Item(c.getUnstrung()), amount, Optional.empty(), c.getLevel(), c.getLimbsExp(), Skill.FLETCHING));
                    }
                }).open()));
                return true;
            }
        }
        return false;
    }

    public static boolean stringBow(Player player, int itemUsed, int itemUsedWith) {
        if (itemUsed == BOW_STRING || itemUsedWith == BOW_STRING || itemUsed == CROSSBOW_STRING || itemUsedWith == CROSSBOW_STRING) {
            int string = itemUsed == BOW_STRING || itemUsed == CROSSBOW_STRING ? itemUsed : itemUsedWith;
            int unstrung = itemUsed == BOW_STRING || itemUsed == CROSSBOW_STRING ? itemUsedWith : itemUsed;
            StringBow.StringableBow bow = StringBow.StringableBow.unstrungBows.get(unstrung);
            if (bow != null) {
                if (bow.getBowStringId() == string) {
                    player.setCreationMenu(Optional.of(new SingleItemCreationMenu(player, bow.getResult(), "How many would you like to make?", new CreationMenuAction() {
                        @Override
                        public void execute(int item, int amount) {
                            player.getSkillManager().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(bow.getItemId()), true), new RequiredItem(new Item(bow.getBowStringId()), true)), new Item(bow.getResult()), amount, Optional.of(new AnimationLoop(bow.getAnimation(), 3)), bow.getLevelReq(), bow.getExp(), Skill.FLETCHING));
                        }
                    }).open()));
                    return true;
                } else {
                    player.getPacketSender().sendMessage("This bow cannot be strung with that.");
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean fletchLog(Player player, int itemUsed, int itemUsedWith) {
        if (itemUsed == ItemIdentifiers.KNIFE || itemUsedWith == ItemIdentifiers.KNIFE) {
            int logId = itemUsed == ItemIdentifiers.KNIFE ? itemUsedWith : itemUsed;
            FletchLog.FletchableLog list = FletchLog.FletchableLog.logs.get(logId);
            if (list != null) {
                int menuSize = list.getFletchable().length;
                Optional<CreationMenu> menu = Optional.empty();

                //The action that will take place when a player
                //selects an amount to create on the interface.
                CreationMenuAction action = new CreationMenuAction() {
                    @Override
                    public void execute(int item, int amount) {
                        for (FletchableItem fl : list.getFletchable()) {
                            if (fl.getProduct().getId() == item) {
                                player.getSkillManager().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(KNIFE), false), new RequiredItem(new Item(list.getLogId()), true)), fl.getProduct(), amount, Optional.of(new AnimationLoop(fl.getAnimation(), 3)), fl.getLevelRequired(), fl.getExperience(), Skill.FLETCHING));
                            }
                        }
                    }
                };

                //Create the item creation menu interface..
                switch (menuSize) {
                    case 4:
                        menu = Optional.of(new QuardrupleItemCreationMenu(player, list.getFletchable()[0].getProduct().getId(), list.getFletchable()[1].getProduct().getId(), list.getFletchable()[2].getProduct().getId(), list.getFletchable()[3].getProduct().getId(), "What would you like to make?", action));
                        break;
                    case 3:
                        menu = Optional.of(new TripleItemCreationMenu(player, list.getFletchable()[0].getProduct().getId(), list.getFletchable()[1].getProduct().getId(), list.getFletchable()[2].getProduct().getId(), "What would you like to make?", action));
                        break;
                }

                //Send the interface if present..
                if (menu.isPresent()) {
                    player.setCreationMenu(menu);
                    menu.get().open();
                }
                return true;
            }
        }
        return false;
    }


    public static final class FletchableItem {
        private final Item product;
        private final int levelRequired;
        private final int experience;
        private final Animation animation;

        public FletchableItem(Item product, int levelRequired, int experience, Animation animation) {
            this.product = product;
            this.levelRequired = levelRequired;
            this.experience = experience;
            this.animation = animation;
        }

        public Item getProduct() {
            return product;
        }

        public int getLevelRequired() {
            return levelRequired;
        }

        public int getExperience() {
            return experience;
        }

        public Animation getAnimation() {
            return animation;
        }
    }
}
