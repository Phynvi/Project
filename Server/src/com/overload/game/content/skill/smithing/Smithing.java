package com.overload.game.content.skill.smithing;

import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.syntax.impl.SmeltBarX;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.ItemIdentifiers;
import com.overload.util.Misc;

import java.util.Arrays;
import java.util.Optional;

public class Smithing extends ItemIdentifiers {

    public static boolean handleButton(Player player, int button) {
        for (SmithingBarData.Bar bar : SmithingBarData.Bar.values()) {
            for (int[] b : bar.getButtons()) {
                if (b[0] == button) {
                    int amount = b[1];
                    if (amount == -1) {
                        player.setEnterSyntax(new SmeltBarX(bar));
                        player.getPacketSender().sendEnterAmountPrompt("Enter amount of bars to smelt:");
                    } else {
                        player.getSkillManager().startSkillable(new Smelting(bar, amount));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static final class EquipmentMaking {

        public static final int EQUIPMENT_CREATION_INTERFACE_ID = 994;

        public static final int EQUIPMENT_CREATION_COLUMN_1 = 1119;
        public static final int EQUIPMENT_CREATION_COLUMN_2 = 1120;
        public static final int EQUIPMENT_CREATION_COLUMN_3 = 1121;
        public static final int EQUIPMENT_CREATION_COLUMN_4 = 1122;
        public static final int EQUIPMENT_CREATION_COLUMN_5 = 1123;

        public static void openInterface(Player player) {
            //Search for bar..
            Optional<SmithingBarData.Bar> bar = Optional.empty();
            for (SmithingBarData.Bar b : SmithingBarData.Bar.values()) {
                if (!b.getItems().isPresent()) {
                    continue;
                }
                if (player.getInventory().contains(b.getBar())) {
                    if (player.getSkillManager().getCurrentLevel(Skill.SMITHING) >= b.getLevelReq()) {
                        bar = Optional.of(b);
                    }
                }
            }

            //Did we find a bar in the player's inventory?
            if (bar.isPresent()) {
                //First, clear the interface from items..
                for (int i = 1119; i <= 1123; i++) {
                    player.getPacketSender().clearItemOnInterface(i);
                }

                //Clear slots that aren't always used..
                player.getPacketSender()
                        .sendString(1132, "")
                        .sendString(1096, "")
                        .sendString(1135, "")
                        .sendString(1134, "");

                //Go through the bar's items..
                for (SmithEquipmentData.SmithableEquipment b : bar.get().getItems().get()) {
                    player.getPacketSender().sendSmithingData(b.getItemId(), b.getItemSlot(), b.getItemFrame(), b.getAmount());
                    String barColor = "@red@";
                    String itemColor = "@bla@";
                    if (player.getInventory().getAmount(b.getBarId()) >= b.getBarsRequired()) {
                        barColor = "@gre@";
                    }
                    if (player.getSkillManager().getCurrentLevel(Skill.SMITHING) >= b.getRequiredLevel()) {
                        itemColor = "@whi@";
                    }
                    player.getPacketSender().sendString(b.getBarFrame(), barColor + Integer.toString(b.getBarsRequired()) + " " + (b.getBarsRequired() > 1 ? "bars" : "bar"));
                    player.getPacketSender().sendString(b.getNameFrame(), itemColor + b.getName());
                }

                //Send interface..
                player.getPacketSender().sendInterface(EQUIPMENT_CREATION_INTERFACE_ID);
            } else {
                player.getPacketSender().sendMessage("You don't have any bars in your inventory which can be used with your Smithing level.");
            }
        }

        public static void smithcannonballs (Player player) {
            if (!player.getInventory().contains(AMMO_MOULD)) {
                player.sendMessage("You need an ammo mould to smith cannonballs." + player.getClickDelay().elapsed(1000));
                player.getClickDelay().reset();
                return;
            }
            TaskManager.submit(new Task(3, player, true) {
                @Override
                protected void execute() {

                    if(!player.getInventory().contains(STEEL_BAR)) {
                        player.sendMessage("You dont have Steel bars.");
                        stop();
                        return;
                    }
                    if(!player.getInventory().contains(AMMO_MOULD)) {
                        player.sendMessage("You need a ammo mould to smith cannonballs.");
                        stop();
                        return;
                    }
                    if(player.getInventory().getFreeSlots() == 0) {
                        player.sendMessage("You dont have enough space to hold cannonballs.");
                        return;
                    }
                    player.getInventory().delete(STEEL_BAR, 1);
                    player.getInventory().add(CANNONBALL, 4);
                    player.getSkillManager().addExperience(Skill.SMITHING, 180);
                    player.performAnimation(new Animation(896, 3));
                }
            });
        }


        public static void initialize(Player player, int itemId, int interfaceId, int slot, int amount) {
            //First verify the item we're trying to make..
            for (SmithEquipmentData.SmithableEquipment smithable : SmithEquipmentData.SmithableEquipment.values()) {
                if (smithable.getItemId() == itemId && smithable.getItemFrame() == interfaceId
                        && smithable.getItemSlot() == slot) {
                    //Start making items..
                    player.getSkillManager().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(HAMMER)), new RequiredItem(new Item(smithable.getBarId(), smithable.getBarsRequired()), true)),
                            new Item(smithable.getItemId(), smithable.getAmount()), amount, Optional.of(new AnimationLoop(new Animation(898), 3)), smithable.getRequiredLevel(), 10, Skill.SMITHING));
                    break;
                }
            }
        }
    }


    public static final class Smelting extends ItemCreationSkillable {

        private static final Animation ANIMATION = new Animation(896);

        private final SmithingBarData.Bar bar;

        public Smelting(SmithingBarData.Bar bar, int amount) {
            super(Arrays.asList(bar.getOres()), new Item(bar.getBar()), amount,
                    Optional.of(new AnimationLoop(ANIMATION, 4)), bar.getLevelReq(), bar.getXpReward(), Skill.SMITHING);
            this.bar = bar;
        }

        //Override finishedCycle because we need to handle special cases
        //such as Iron ore 50% chance of failing to smelt.
        @Override
        public void finishedCycle(Player player) {
            //Handle iron bar. It has a 50% chance of failing.
            if (bar == SmithingBarData.Bar.IRON_BAR) {
                if (Misc.getRandom(2) == 1) {
                    player.getPacketSender().sendMessage("The Iron ore was too impure and you were unable to make an Iron bar.");
                    //We still need to delete the ore and decrement amount.
                    filterRequiredItems(r -> r.isDelete()).forEach(r -> player.getInventory().delete(r.getItem()));
                    decrementAmount();
                    return;
                }
            }

            super.finishedCycle(player);
        }
    }
}
