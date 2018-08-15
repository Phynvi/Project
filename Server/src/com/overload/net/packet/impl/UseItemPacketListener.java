package com.overload.net.packet.impl;

import com.overload.Server;
import com.overload.game.World;
import com.overload.game.content.achievement.Diary.DiaryType;
import com.overload.game.content.achievement.Diary.OptionType;
import com.overload.game.content.combat.CombatFactory;
import com.overload.game.content.interactable.ZulrahItems;
import com.overload.game.content.interactable.ZulrahItems.ZulrahItemType;
import com.overload.game.content.minigames.warriors_guild.AnimatedArmour;
import com.overload.game.content.skill.cooking.Cooking;
import com.overload.game.content.skill.cooking.Pie;
import com.overload.game.content.skill.cooking.Cookable;
import com.overload.game.content.skill.cooking.Cookable.Fish;
import com.overload.game.content.skill.cooking.Cookable.Pizza;
import com.overload.game.content.skill.crafting.LeatherMaking;
import com.overload.game.content.skill.farming.Compost;
import com.overload.game.content.skill.farming.Farming;
import com.overload.game.content.skill.herblore.Herblore;
import com.overload.game.content.skill.herblore.UnfCreator;
import com.overload.game.content.skill.crafting.Crafting;
import com.overload.game.content.skill.prayer.BoneData;
import com.overload.game.content.skill.skillable.impl.Firemaking;
import com.overload.game.content.skill.skillable.impl.Firemaking.LightableLog;
import com.overload.game.content.skill.fletching.Fletching;
import com.overload.game.content.skill.prayer.Prayer.AltarOffering;
import com.overload.game.content.skill.smithing.Smithing;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.grounditem.ItemOnGroundManager;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.object.ObjectManager;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Item;
import com.overload.game.model.Position;
import com.overload.game.model.menu.CreationMenu;
import com.overload.game.model.menu.CreationMenu.CreationMenuAction;
import com.overload.game.model.menu.impl.SingleItemCreationMenu;
import com.overload.game.model.movement.WalkToAction;
import com.overload.game.model.rights.Right;
import com.overload.net.packet.Packet;
import com.overload.net.packet.PacketConstants;
import com.overload.net.packet.PacketListener;
import com.overload.util.ItemIdentifiers;
import com.overload.util.ObjectIdentifiers;

import java.util.Optional;


public class UseItemPacketListener extends ItemIdentifiers implements PacketListener {

	private static void itemOnItem(Player player, Packet packet) {
        int usedWithSlot = packet.readUnsignedShort();
        int itemUsedSlot = packet.readUnsignedShortA();
        if (usedWithSlot < 0 || itemUsedSlot < 0
                || itemUsedSlot >= player.getInventory().capacity()
                || usedWithSlot >= player.getInventory().capacity())
            return;
        Item used = player.getInventory().getItems()[itemUsedSlot];
        Item usedWith = player.getInventory().getItems()[usedWithSlot];

        player.getPacketSender().sendInterfaceRemoval();
        player.getSkillManager().stopSkillable();

		player.setLastClickData(DiaryType.ITEM, OptionType.USE_ON_ITEM, used.getId(), usedWith.getId());

        //Herblore
        Item primary = used.getDefinition().getName().toLowerCase().contains("unf") ? used : usedWith;
        if (UnfCreator.make(player, used.getId(), usedWith.getId())
                || Herblore.make(player, primary.getId())) {
            return;
        }

        //Crafting
        if(Crafting.craftGem(player, used.getId(), usedWith.getId())) {
            return;
        }

        //BoltTip
        if(Crafting.craftBolt(player, used.getId(), usedWith.getId())) {
            return;
        }

        //leathermaking
        if(LeatherMaking.makeHide(player, used.getId(), usedWith.getId())) {
            return;
        }

        //Fletching
        if (Fletching.fletchLog(player, used.getId(), usedWith.getId())
                || Fletching.stringBow(player, used.getId(), usedWith.getId())
                || Fletching.fletchAmmo(player, used.getId(), usedWith.getId())
                || Fletching.fletchCrossbow(player, used.getId(), usedWith.getId())) {
            return;
        }
        
        //Pie Making
        if (Pie.makePie(player, used.getId(), usedWith.getId())) {
        	return;
        }
        
        //Handle cooking on objects..
		final Cookable itemUsed = new Cookable(Cookable.getItemById(used.getId()));
		final Cookable itemUsedWith = new Cookable(Cookable.getItemById(usedWith.getId()));
		if (itemUsed != null || itemUsedWith != null) {
			final Cookable cookable = itemUsed == null ? itemUsedWith : itemUsed;
			if (cookable.isPizza()) {
				Pizza pizza = cookable.getAsPizza();
				if (pizza != null) {
		            CreationMenu cookMenu = new SingleItemCreationMenu(player, ((Pizza) cookable.get()).getResult().getId(), "How many would you like to make?", new CreationMenuAction() {
		                @Override
		                public void execute(int item, int amount) {
		                    player.getSkillManager().startSkillable(new Cooking(null, cookable, amount));
		                }
		            }).open();
		            player.setCreationMenu(Optional.of(cookMenu));
				}
		        return;
			}
		}


        //Firemaking
        if (Firemaking.init(player, used.getId(), usedWith.getId())) {
            return;
        }

        //Granite clamp on Granite maul
        if ((used.getId() == GRANITE_CLAMP || usedWith.getId() == GRANITE_CLAMP)
                && (used.getId() == GRANITE_MAUL || usedWith.getId() == GRANITE_MAUL)) {
            if (player.busy() || CombatFactory.inCombat(player)) {
                player.getPacketSender().sendMessage("You cannot do that right now.");
                return;
            }
            if (player.getInventory().contains(GRANITE_MAUL)) {
                player.getInventory().delete(GRANITE_MAUL, 1).delete(GRANITE_CLAMP, 1).add(GRANITE_MAUL_3, 1);
                player.getPacketSender().sendMessage("You attach your Granite clamp onto the maul..");
            }
            return;
        }

        //Blowpipe reload
        else if (used.getId() == TOXIC_BLOWPIPE || usedWith.getId() == TOXIC_BLOWPIPE || used.getId() == TOXIC_BLOWPIPE_EMPTY || usedWith.getId() == TOXIC_BLOWPIPE_EMPTY) {
            ZulrahItems.charge(player, used, usedWith, ZulrahItemType.BLOWPIPE);
        }
    }

    private static void itemOnNpc(final Player player, Packet packet) {
        final int id = packet.readShortA();
        final int index = packet.readShortA();
        final int slot = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity()) {
            return;
        }
        if (slot < 0 || slot > player.getInventory().getItems().length) {
            return;
        }
        NPC npc = World.getNpcs().get(index);
        if (npc == null) {
            return;
        }
        if (player.getInventory().getItems()[slot].getId() != id) {
            return;
        }

        if (player.isDebug() && player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
            player.sendMessage("Item: " + id + " Used on NPC: " + npc.getId() + " at " + npc.getPosition().toString());
        }
        
		player.setLastClickData(DiaryType.ITEM, OptionType.USE_ON_NPC, new int[] {id, npc.getId()});
        
        if (player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
        	if (id == 6714) {
    			World.getNpcs().remove(npc);
    			return;
        	}
        }
        switch (id) {

        }
    }

    @SuppressWarnings("unused")
    private static void itemOnObject(Player player, Packet packet) {
        int interfaceType = packet.readShort();
        final int objectId = packet.readShort();
        final int objectY = packet.readLEShortA();
        final int itemSlot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readShort();

        if (itemSlot < 0 || itemSlot >= player.getInventory().capacity())
            return;
        final Item item = player.getInventory().getItems()[itemSlot];
        if (item == null || item.getId() != itemId)
            return;
        final Position position = new Position(objectX, objectY, player.getPosition().getZ());
        final Optional<GameObject> object = MapObjects.get(objectId, position);

        //Make sure the object actually exists in the region...
        if (!object.isPresent()) {
            Server.getLogger().info("Object with id " + objectId + " (" + objectX + ", " + objectY + ") does not exist!");
            if (player.getRights().isOrInherits(Right.GAME_DEVELOPER) && player.isDebug()) {
                player.sendMessage("Object with id " + objectId + " (" + objectX + ", " + objectY + ") does not exist!");
            }
            return;
        }
		player.setLastClickData(DiaryType.ITEM, OptionType.USE_ON_OBJECT, itemId, objectId);

        //Update facing..
        player.setPositionToFace(position);

        if (player.getRights().isOrInherits(Right.GAME_DEVELOPER) && player.isDebug()) {
        	player.getPacketSender().sendMessage("Use item: " + itemId + " on Object: " + objectId);
        }
        
        if (player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
        	if (itemId == 6714) {
        		if (object.isPresent()) {
        			ObjectManager.deregister(object.get(), true);
        			return;
        		}
        	}
        }
        
        String usageText = "itemPatch";
        for (int normalCompost : Compost.COMPOST_ORGANIC) {
        	if (normalCompost == item.getId())
        		usageText = "itemCompost";
        }
        for (int superCompost : Compost.SUPER_COMPOST_ORGANIC) {
        	if (superCompost == item.getId())
        		usageText = "itemCompost";
        }
        if (item.getId() == Compost.TOMATO) {
    		usageText = "itemCompost";
        }
        
        if (Farming.handleClickOption(usageText, player, objectX, objectY, item)) {
        	return;
        }

//        FarmingService.onGameObjectActionEvent(player,object, "item", item);
        
        //Handle object..
        switch (object.get().getId()) {
			case 23955:
				AnimatedArmour.itemOnAnimator(player, itemId);
				break;
            case 2030:
                Smithing.EquipmentMaking.smithcannonballs(player);
                break;
			case ObjectIdentifiers.COOKING_RANGE:
			case ObjectIdentifiers.COOKING_RANGE_2:
			case ObjectIdentifiers.COOKING_RANGE_3:
			case ObjectIdentifiers.COOKING_RANGE_4:
			case ObjectIdentifiers.COOKING_RANGE_5:
			case ObjectIdentifiers.COOKING_RANGE_6:
                Cookable cookable = new Cookable(Cookable.getItemById(itemId));
                if (cookable != null) {
					if (cookable.isPie()) {
	            		com.overload.game.content.skill.cooking.Cookable.Pie pie = cookable.getAsPie();
	            		if (pie != null) {
		                    CreationMenu cookMenu = new SingleItemCreationMenu(player, ((com.overload.game.content.skill.cooking.Cookable.Pie) cookable.get()).getResult().getId(), "How many would you like to cook?", new CreationMenuAction() {
		                        @Override
		                        public void execute(int item, int amount) {
		                            player.getSkillManager().startSkillable(new Cooking(object.get(), cookable, amount));
		                        }
		                    }).open();
		                    player.setCreationMenu(Optional.of(cookMenu));
	            		}
	                    return;
	            	}
                }
				break;
            case ObjectIdentifiers.STOVE_4: //Edgeville Stove
            case ObjectIdentifiers.FIRE_5: //Player-made Fire
            case ObjectIdentifiers.FIRE_23: //Barb village fire
                //Handle cooking on objects..
                cookable = new Cookable(Cookable.getItemById(itemId));
                if (cookable != null) {
                	if (cookable.isFish()) {
                		Fish fish = cookable.getAsFish();
                		if (fish != null) {
		                    CreationMenu cookMenu = new SingleItemCreationMenu(player, ((Fish) cookable.get()).getCookedItem(), "How many would you like to cook?", new CreationMenuAction() {
		                        @Override
		                        public void execute(int item, int amount) {
		                            player.getSkillManager().startSkillable(new Cooking(object.get(), cookable, amount));
		                        }
		                    }).open();
		                    player.setCreationMenu(Optional.of(cookMenu));
                		}
	                    return;
                	} else if (cookable.isPizza()) {
                		Pizza pizza = cookable.getAsPizza();
                		if (pizza != null) {
		                    CreationMenu cookMenu = new SingleItemCreationMenu(player, ((Pizza) cookable.get()).getResult().getId(), "How many would you like to cook?", new CreationMenuAction() {
		                        @Override
		                        public void execute(int item, int amount) {
		                            player.getSkillManager().startSkillable(new Cooking(object.get(), cookable, amount));
		                        }
		                    }).open();
		                    player.setCreationMenu(Optional.of(cookMenu));
                		}
	                    return;
                	}
        		}
                //Handle bonfires..
                if (object.get().getId() == ObjectIdentifiers.FIRE_5) {
                    Optional<LightableLog> log = LightableLog.getForItem(item.getId());
                    if (log.isPresent()) {
                        CreationMenu fmMenu = new SingleItemCreationMenu(player, log.get().getLogId(), "How many would you like to burn?", new CreationMenuAction() {
                            @Override
                            public void execute(int item, int amount) {
                                player.getSkillManager().startSkillable(new Firemaking(log.get(), object.get(), amount));
                            }
                        }).open();
                        player.setCreationMenu(Optional.of(fmMenu));
                        return;
                    }
                }
                break;
            case 409: //Bone on Altar
                Optional<BoneData.BuriableBone> b = BoneData.BuriableBone.forId(item.getId());
                if (b.isPresent()) {
                    CreationMenu menu = new SingleItemCreationMenu(player, itemId, "How many would you like to offer?", new CreationMenuAction() {
                        @Override
                        public void execute(int item, int amount) {
                            player.getSkillManager().startSkillable(new AltarOffering(b.get(), object.get(), amount));
                        }
                    }).open();
                    player.setCreationMenu(Optional.of(menu));
                }
                break;
        }
    }

    @SuppressWarnings("unused")
    private static void itemOnPlayer(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int targetIndex = packet.readUnsignedShort();
        int itemId = packet.readUnsignedShort();
        int slot = packet.readLEShort();
        if (slot < 0 || slot >= player.getInventory().capacity() || targetIndex >= World.getPlayers().capacity())
            return;
        Player target = World.getPlayers().get(targetIndex);
        if (target == null) {
            return;
        }
    }

    @SuppressWarnings("unused")
    private static void itemOnGroundItem(Player player, Packet packet) {
        int interfaceType = packet.readLEShort();
        int usedItemId = packet.readShortA();
        int groundItemId = packet.readShort();
        int y = packet.readShortA();
        int unknown = packet.readLEShortA();
        int x = packet.readShort();

        //Verify item..
        if (!player.getInventory().contains(usedItemId)) {
            return;
        }

        //Verify ground item..
        Optional<ItemOnGround> groundItem = ItemOnGroundManager.getGroundItem(Optional.of(player.getUsername()), groundItemId, new Position(x, y));
        if (!groundItem.isPresent()) {
            return;
        }
        
        if (player.getRights().isOrInherits(Right.GAME_DEVELOPER)) {
        	if (usedItemId == 6714) {
        		if (groundItem.isPresent()) {
        			ItemOnGroundManager.deregister(groundItem.get());
        			return;
        		}
        	}
        }

        player.setWalkToTask(new WalkToAction(player, groundItem.get().getPosition(), 1, new Action() {
            @Override
            public void execute() {
                //Face...
                player.setPositionToFace(groundItem.get().getPosition());

                player.setLastClickData(DiaryType.ITEM, OptionType.USE_ON_GROUND, usedItemId, groundItem.get().getItem().getId());
        		
                //Handle used item..
                switch (usedItemId) {
                    case TINDERBOX: //Lighting a fire..
                        Optional<LightableLog> log = LightableLog.getForItem(groundItemId);
                        if (log.isPresent()) {
                            player.getSkillManager().startSkillable(new Firemaking(log.get(), groundItem.get()));
                            return;
                        }
                        break;
                }
            }
            @Override
            public void execute(Entity entity) {}
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHitpoints() <= 0)
            return;
        switch (packet.getOpcode()) {
            case PacketConstants.ITEM_ON_ITEM:
                itemOnItem(player, packet);
                break;
            case PacketConstants.ITEM_ON_OBJECT:
                itemOnObject(player, packet);
                break;
            case PacketConstants.ITEM_ON_GROUND_ITEM:
                itemOnGroundItem(player, packet);
                break;
            case PacketConstants.ITEM_ON_NPC:
                itemOnNpc(player, packet);
                break;
            case PacketConstants.ITEM_ON_PLAYER:
                itemOnPlayer(player, packet);
                break;
        }
    }
}
