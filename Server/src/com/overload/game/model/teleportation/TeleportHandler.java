package com.overload.game.model.teleportation;

import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.EffectTimer;
import com.overload.game.model.Position;
import com.overload.game.model.areas.Area;
import com.overload.game.model.areas.AreaManager;
import com.overload.game.model.areas.impl.WildernessArea;
import com.overload.game.model.dialogue.DialogueManager;
import com.overload.game.model.dialogue.DialogueOptions;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

public class TeleportHandler {

	/**
	 * Teleports a player to the target location.
	 *
	 * @param player
	 *            The player teleporting.
	 * @param targetLocation
	 *            The location to teleport to.
	 * @param teleportType
	 *            The type of teleport.
	 */
	public static void teleport(Player player, Position targetLocation, TeleportType teleportType,
			boolean wildernessWarning) {
		if (wildernessWarning) {
			StringBuilder warning = new StringBuilder();
			Area area = AreaManager.get(targetLocation);
			boolean wilderness = (area instanceof WildernessArea);
			int wildernessLevel = WildernessArea.getLevel(targetLocation.getY());
			if (wilderness) {
				warning.append("Are you sure you want to teleport there? ");
				if (wildernessLevel > 0) {
					warning.append("It's in level @red@" + wildernessLevel + "@bla@ wilderness! ");
					if (WildernessArea.multi(targetLocation.getX(), targetLocation.getY())) {
						warning.append(
								"Additionally, @red@it's a multi zone@bla@. Other players may attack you simultaneously.");
					} else {
						warning.append("Other players will be able to attack you.");
					}
				} else {
					warning.append("Other players will be able to attack you.");
				}
				player.setDialogueContinueAction(new Action() {
					@Override
					public void execute() {

						DialogueManager.start(player, 7);
						player.setDialogueOptions(new DialogueOptions() {
							@Override
							public void handleOption(Player player, int option) {
								player.getPacketSender().sendInterfaceRemoval();
								if (option == 1) {
									teleport(player, targetLocation, teleportType, false);
								}
							}
						});
					}
                    @Override
                    public void execute(Entity entity) {}

				});
				DialogueManager.sendStatement(player, warning.toString());
				return;
			}
		}
		player.getMovementQueue().setBlockMovement(true).reset();
		onTeleporting(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		player.setUntargetable(true);
		player.setTeleporting(true);
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;

			@Override
			public void execute() {
				if (tick == teleportType.getStartTick() - 2) {
					if (teleportType.getMiddleAnim() != null) {
						player.performAnimation(teleportType.getMiddleAnim());
					}
					if (teleportType.getMiddleGraphic() != null) {
						player.performGraphic(teleportType.getMiddleGraphic());
					}
				} else if (tick == teleportType.getStartTick()) {
					onTeleporting(player);
					player.performAnimation(teleportType.getEndAnimation());
					player.performGraphic(teleportType.getEndGraphic());
					player.moveTo(targetLocation);
				} else if (tick >= teleportType.getStartTick() + 2) {
					stop();
				}
				tick++;
			}

			@Override
			public void stop() {
				player.getClickDelay().reset(0);
				player.getMovementQueue().setBlockMovement(false).reset();
				player.setUntargetable(false);
				player.setTeleporting(false);
				super.stop();
			}
		});
		player.getClickDelay().reset();
	}

	private static void onTeleporting(Player player) {
		player.getSkillManager().stopSkillable();
		player.getPacketSender().sendInterfaceRemoval();
		player.setWalkToTask(null);
		player.setEntityInteraction(null);
		player.getMovementQueue().resetFollowing();
		player.getCombat().reset();
	}

	public static boolean checkReqs(Player player, Position targetLocation) {
		if (player.busy()) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return false;
		}

		if (!player.getCombat().getTeleBlockTimer().finished()) {
			if (player.getArea() instanceof WildernessArea) {
				player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
				return false;
			} else {
				player.getCombat().getTeleBlockTimer().stop();
				player.getPacketSender().sendEffectTimer(0, EffectTimer.TELE_BLOCK);
			}
		}

		if (player.getMovementQueue().isMovementBlocked()) {
			return false;
		}

		if (player.getArea() != null) {
			if (!player.getArea().canTeleport(player)) {
				return false;
			}
		}

		return true;
	}

	public static boolean handleButton(Player player, int buttonId, int menuId) {
		TeleportButton teleportButton = TeleportButton.get(buttonId);
		if (teleportButton != null) {
			switch (menuId) {
			case 0: // Click to teleport
				if (TeleportHandler.checkReqs(player, teleportButton.getDestination())) {
					TeleportHandler.teleport(player, teleportButton.getDestination(),
							player.getSpellbook().getTeleportType(), teleportButton.isDangerous());
					player.getPreviousTeleports().put(teleportButton, teleportButton.getDestination());
				}
				return true;
			case 1: // Previous option on teleport
				if (player.getPreviousTeleports().containsKey(teleportButton)) {
					Position tele = player.getPreviousTeleports().get(teleportButton);
					if (TeleportHandler.checkReqs(player, tele)) {
						TeleportHandler.teleport(player, tele, player.getSpellbook().getTeleportType(), true);
					}
				} else {
					player.getPacketSender().sendMessage("Unable to find a previous teleport.");
				}
				player.getPacketSender().sendInterfaceRemoval();
				return true;
			}
		}
		return false;
	}
}
