package com.overload.game.content;

import com.overload.game.World;
import com.overload.game.content.combat.WeaponInterfaces;
import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.content.skill.SkillManager;
import com.overload.game.entity.impl.grounditem.ItemOnGround;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Flag;
import com.overload.game.model.Item;
import com.overload.game.model.Skill;
import com.overload.game.model.syntax.impl.SpawnAggressiveNPC;
import com.overload.game.model.syntax.impl.TeleportToPlayer;
import com.overload.game.model.syntax.impl.TransformIntoNPC;

public class RottenPotato {
	public static void eat(Player player) {
		switch (player.getDialogueActionId()) {
			case 1:
				player.getPacketSender().sendInterfaceRemoval();
				for(Skill skill : Skill.values()) {
					int level = SkillManager.getMaxAchievingLevel(skill);
					player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
				}
				WeaponInterfaces.assign(player);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				break;
			case 2:
				player.getPacketSender().sendInterfaceRemoval();
				player.getInventory().resetItems().refreshItems();
				break;
			case 3:
				player.sendMessage("Currently Unavailable");
				break;
			case 4:
				player.setEnterSyntax(new TeleportToPlayer());
				player.getPacketSender().sendEnterInputPrompt("Enter a players name you want to teleport to.");
				break;
			case 5:
				player.setEnterSyntax(new SpawnAggressiveNPC());
				player.getPacketSender().sendEnterInputPrompt("Enter the npc ID you wish to spawn aggressive.");
				break;
		}
		DialogueHandler.close(player, false);
	}
	public static void slice(Player player) {
		switch (player.getDialogueActionId()) {
		}
		DialogueHandler.close(player, false);
	}
	public static void peel(Player player) {
		switch (player.getDialogueActionId()) {
		case 1:
			player.getBank(0).open();
			break;
		case 2:
			player.sendMessage("Currently Unavailable");
			break;
		case 3:
			player.getPacketSender().sendInterfaceRemoval();
			for (ItemOnGround groundItem : World.getItems()) {
				Item item = groundItem.getItem();
				if (item.rarity.name() == "RARE" || item.rarity.name() == "VERY_RARE") {
					player.moveTo(groundItem.getPosition());
					break;
				}
			}
			break;
		case 4:
			player.getPacketSender().sendInterfaceRemoval();
			player.sendMessage("Currently Unavailable");
			break;
		}
		DialogueHandler.close(player, false);
	}
	public static void mash(Player player) {
		switch (player.getDialogueActionId()) {
			case 1:
				player.getPacketSender().sendInterfaceRemoval();
				player.setKeepLoggedIn(true);
				break;
			case 2:
				player.getPacketSender().sendInterfaceRemoval();
				player.setKeepLoggedIn(false);
				player.requestLogout();
				break;
			case 3:
				player.getPacketSender().sendInterfaceRemoval();
				player.setHitpoints(0);
				break;
			case 4:
				player.getPacketSender().sendInterfaceRemoval();
				player.setEnterSyntax(new TransformIntoNPC());
				player.getPacketSender().sendEnterInputPrompt("Enter the ID of the NPC you want to turn into.");
				break;
		}
		DialogueHandler.close(player, false);
	}
}
