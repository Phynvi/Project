package com.overload.game.content.skill.crafting;

import com.overload.game.content.skill.skillable.impl.ItemCreationSkillable;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.*;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.menu.impl.QuardrupleItemCreationMenu;
import com.overload.game.model.menu.impl.TripleItemCreationMenu;
import com.overload.util.ItemIdentifiers;

import java.util.Arrays;
import java.util.Optional;

public class LeatherMaking extends ItemIdentifiers{

    public static boolean makeHide(Player player, int itemUsed, int itemUsedWith) {
        if (itemUsed == NEEDLE || itemUsedWith == NEEDLE || itemUsed == THREAD || itemUsedWith == THREAD) {
            int hideId = itemUsed == ItemIdentifiers.NEEDLE ||  itemUsed == THREAD ? itemUsedWith : itemUsed;
            LeatherData.HideData list = LeatherData.HideData.hides.get(hideId);
            if (list != null) {
                int menuSize = list.getResult().length;
                Optional<CreationMenu> menu = Optional.empty();
                CreationMenu.CreationMenuAction action = new  CreationMenu.CreationMenuAction() {
                    @Override
                    public void execute(int item, int amount) {
                        for (LeatherMaking lm : list.getResult()) {
                            if (lm.getProduct().getId() == item) {
                                player.getSkillManager().startSkillable(new ItemCreationSkillable(Arrays.asList(new RequiredItem(new Item(THREAD), true), (new RequiredItem(new Item(NEEDLE), false)), new RequiredItem(new Item(list.getHideId(), 2), true)), lm.getProduct(), amount, Optional.of(new AnimationLoop(lm.getAnimation(), 3)), lm.getLevelRequired(), lm.getExperience(), Skill.CRAFTING));
                            }
                        }
                    }
                };

                switch (menuSize) {
                    case 4:
                        menu = Optional.of(new QuardrupleItemCreationMenu(player, list.getResult()[0].getProduct().getId(), list.getResult()[1].getProduct().getId(), list.getResult()[2].getProduct().getId(), list.getResult()[3].getProduct().getId(), "What would you like to make?", action));
                        break;
                    case 3:
                        menu = Optional.of(new TripleItemCreationMenu(player, list.getResult()[0].getProduct().getId(), list.getResult()[1].getProduct().getId(), list.getResult()[2].getProduct().getId(), "What would you like to make?", action));
                        break;
                }

                if (menu.isPresent()) {
                    player.setCreationMenu(menu);
                    menu.get().open();
                }
                return true;
            }
        }
        return false;
    }

        private final Item product;
        private final int levelRequired;
        private final int experience;
        private final Animation animation;

        public LeatherMaking(Item product, int levelRequired, int experience, Animation animation) {
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
