package com.overload.game.content.skill.slayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;

import com.overload.game.content.dialogues.DialogueHandler;
import com.overload.game.definition.ItemDefinition;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Skill;
import com.overload.util.Misc;

public class Slayer {
	
	private Optional<SlayerTask> task = Optional.empty();

	private Set<Unlocks> unlockSet = new HashSet<Unlocks>();
	private Set<Extends> extendSet = new HashSet<Extends>();
	
	private int master;

	private int consecutiveTasks;

	private int points;

	private int taskAmount;

	private final Player player;

	private String[] removed = Misc.nullToEmpty(4);

	public Slayer(Player player) {
		this.player = player;
	}

	public boolean isOnTuraelTask() {
		Optional<SlayerMaster> m = SlayerMaster.get(401);
		if (m.isPresent()) {
			SlayerTask[] available = m.get().getAvailable();
			for (int i = 0; i < available.length; i++) {
				if (available[i].getPrimaryName() == task.get().getPrimaryName())
					return true;
			}
		}
		return false;
	}

	public void createNewTask(int masterId) {
		SlayerMaster.get(masterId).ifPresent(m -> {	
			if (player.getSkillManager().getCombatLevel() < m.getLevel()) {
				DialogueHandler.npcChat(player, m.getId(), "You need a combat level of " + m.getLevel() + " to get a task from me.");
				return;
			}
			if (masterId == 401 && master != 401 && consecutiveTasks > 0 && taskAmount > 0) {
				consecutiveTasks = 0;
				player.getPacketSender().sendMessage("Your consecutive tasks have been reset as you have switched to an easy task.");
			}

			SlayerTask[] available = retainObtainable(m.getAvailable());
			task = Optional.of(Misc.randomSearch(available, 0, available.length - 1));
			
			taskAmount = Misc.random(Range.between(task.get().getMinimum(), task.get().getMaximum()));
			
			setTask(task);
			setTaskAmount(taskAmount);
			
			String taskName = task.get().getPrimaryName();
			
			player.setNpcTalking(m.getId());
			DialogueHandler.npcChat(player, m.getId(), "You have been given a task to kill " + taskAmount + " " + taskName + (player.getSlayer().getTaskAmount() > 1 && (taskName.substring(0, taskName.length() - 1) == "s") ? "" : "s"));
			player.setDialogueState(-1);
			master = m.getId();
		});
	}
	
	public void killTaskMonster(NPC npc) {
		if (npc == null) {
			return;
		}
		if (player == null) {
			return;
		}
		if (!task.isPresent()) {
			return;
		}
		if (taskAmount == 0) {
			return;
		}
		
		task.ifPresent(task -> {
			String name = npc.getDefinition().getName().toLowerCase().replaceAll("_", " ");
			
			if (name.equals(task.getPrimaryName()) || ArrayUtils.contains(task.getNames(), name)) {
				Optional<SlayerMaster> master = SlayerMaster.get(this.master);
				
				master.ifPresent(m -> {
					switch (m.getId()) {
					case 401:
					case 402:
					case 405:
					case 6797:
						taskAmount--;
						player.getSkillManager().addExperience(Skill.SLAYER, task.getExperience(), true);
						break;
					case 7663:
						if (npc.getArea().isWilderness()) {
							taskAmount--;
							player.getSkillManager().addExperience(Skill.SLAYER, task.getExperience(), true);
							break;
						}
					}
					if (taskAmount == 0) {
						int consecutive = consecutiveTasks + 1;
						this.consecutiveTasks++;
						this.points += m.getPointReward(0);
						this.task = Optional.empty();
						player.getPacketSender().sendMessage("You have completed your slayer task, talk to a slayer master to receive another.");
						
						int bonusPoints = 
							this.consecutiveTasks == 10 ? m.getPointReward(0) : 
							this.consecutiveTasks == 30 ? m.getPointReward(1) : 
							this.consecutiveTasks == 50 ? m.getPointReward(2) : 
							this.consecutiveTasks == 70 ? m.getPointReward(3) : 
							this.consecutiveTasks == 100 ? m.getPointReward(4) : 
							this.consecutiveTasks == 150 ? m.getPointReward(5) : 0;
											
						if (bonusPoints > 0) {
							points += bonusPoints;
							player.getPacketSender().sendMessage("<col=255>You have completed " + consecutive + " tasks in a row and receive " + bonusPoints + " additional points.</col>");
						}
					}
				});
			}
		});
	}

	private SlayerTask[] retainObtainable(SlayerTask[] tasks) {
		List<SlayerTask> retainable = new ArrayList<>();
		List<String> blocked = new ArrayList<>(Arrays.asList(removed));
		for (SlayerTask task1 : tasks) {
			if (task1 == null)
				continue;
			if (task1.getLevel() <= player.getSkillManager().getCurrentLevel(Skill.SLAYER) && !blocked.contains(task1.getPrimaryName())) {
				retainable.add(task1);
			}
		}

		return retainable.toArray(new SlayerTask[retainable.size()]);
	}

	public Set<Unlocks> getUnlockSet() {
		return unlockSet;
	}
	
	public Set<Extends> getExtendSet() {
		return extendSet;
	}
	
	public void addUnlock(Unlocks unlock) {
		unlockSet.add(unlock);
	}
	
	public void addExtend(Extends extend) {
		extendSet.add(extend);
	}
	
	public void openInterface(int tab) {
		int interfaceId = 47500 + tab;
		updatePoints();
		if (tab == 0) {
			if (player.getSlayer().getUnlockSet().size() > 0) {
				player.getSlayer().getUnlockSet().stream().forEach(
					u -> {
						player.getPacketSender().sendConfigInt(1550 + Unlocks.valueOf(u.toString()).ordinal(), 1);
					}
				);
			}
		} else if (tab == 1) {
			if (player.getSlayer().getExtendSet().size() > 0) {
				player.getSlayer().getExtendSet().stream().forEach(
					e -> {
						player.getPacketSender().sendConfigInt(1700 + Extends.valueOf(e.toString()).ordinal(), 1);
					}
				);
			}
		}
		
		player.getPacketSender().sendInterface(interfaceId);
	}
	
	public boolean handleInterface(int btn) {
		updatePoints();
		switch (btn) {
			case 47513://Unlock Button
				openInterface(0);
				return true;
			case 47514://Extend Button
				openInterface(1);
				return true;
			case 47515://Buy Button
				//openInterface(2);
				player.sendMessage("This tab is currently under construction.");
				return true;
			case 47516://Task Button
				//openInterface(3);
				player.sendMessage("This tab is currently under construction.");
				return true;
		}
		if (btn - 47550 >= 0 && btn < 47700) {
			return handleUnlockInterface(btn);
		} else if (btn - 47700 >= 0 && btn < 47860) {
			return handleExtendInterface(btn);
		}
		return false;
	}

	public boolean handleUnlockInterface(int btn) {
		int id = btn - 47550;
		if (id > 0)
			id = (id / 7); 
		
		if (!unlockSet.contains(Unlocks.values()[id])) {
			if (getPoints() >= Unlocks.values()[id].getPoints()) {
				takePoints(Unlocks.values()[id].getPoints());
				unlockSet.add(Unlocks.values()[id]);
				player.getPacketSender().sendConfigInt(1550 + id, 1);
			} else {
				player.sendMessage("You need @dre@" + Unlocks.values()[id].getPoints() + " Slayer Points @bla@to purchase this.");
			}
			return true;
		} else {
			player.sendMessage("You already own this Unlock");
			return true;
		}
	}

	public boolean handleExtendInterface(int btn) {
		int id = btn - 47700;
		if (id > 0)
			id = (id / 7); 
		
		if (!extendSet.contains(Extends.values()[id])) {
			if (getPoints() >= Extends.values()[id].getPoints()) {
				takePoints(Extends.values()[id].getPoints());
				extendSet.add(Extends.values()[id]);
				player.getPacketSender().sendConfigInt(1700 + id, 1);
			} else {
				player.sendMessage("You need @dre@" + Extends.values()[id].getPoints() + " Slayer Points @bla@to purchase this.");
			}
			return true;
		} else {
			player.sendMessage("You already own this Extend");
			return true;
		}
	}
	
	public void cancelTask() {
		int rankPoints = 30;
		
		if (!task.isPresent()) {
			player.getPacketSender().sendMessage("You must have a task to cancel first.");
			return;
		}
		if (points < rankPoints) {
			player.getPacketSender().sendMessage("This requires atleast " + rankPoints + " slayer points, which you don't have.");
			return;
		}
		player.getPacketSender().sendMessage("You have cancelled your current task of " + taskAmount + " " + task.get().getPrimaryName() + ".");
		task = Optional.empty();
		taskAmount = 0;
		points -= rankPoints;
	}

	public void removeTask() {
		int rankPoints = 100;
		if (!task.isPresent()) {
			player.getPacketSender().sendMessage("You must have a task to remove first.");
			return;
		}
		if (points < rankPoints) {
			player.getPacketSender().sendMessage("This requires atleast " + rankPoints + " slayer points, which you don't have.");
			return;
		}
		for (int index = 0; index < removed.length; index++) {
			if (!removed[index].isEmpty()) {
				continue;
			}
			if (index == 4) {
				player.getPacketSender().sendMessage("You don't have any open slots left to remove tasks.");
				return;
			}
			if (removed[index].isEmpty()) {
				removed[index] = task.get().getPrimaryName();
				points -= rankPoints;
				task = Optional.empty();
				taskAmount = 0;
				player.getPacketSender().sendMessage("Your current slayer task has been removed, you can't obtain this task again.");
				updateCurrentlyRemoved();
				updatePoints();
				return;
			}
		}
	}

	public void updatePoints() {
		player.getPacketSender().sendString(47508, "Reward Points: " + player.getSlayer().getPoints());
	}

	public void updateCurrentlyRemoved() {
		for (int index = 0; index < removed.length; index++) {
			if (removed[index].isEmpty()) {
				//If removed is empty send blank string
			} else {
				//If removed contains show it
			}
		}
	}

	public boolean onActionButton(int actionId) {
		return false;
	}
	
	public void revertHelmet(int helmet) {
		String helmName = ItemDefinition.forId(helmet).getName();
		System.out.println("Name: " + helmName);
		if (helmName.contains("(i)")) {
			player.getInventory().delete(helmet, 1);
			player.getInventory().add(SLAYER_HELMETS.REVERT.getImbued(), 1);
		} else {
			player.getInventory().delete(helmet, 1);
			player.getInventory().add(SLAYER_HELMETS.REVERT.getRegular(), 1);
		}
		player.getPacketSender().sendMessage("You successfully reverted your slayer helmet to normal.");
	}
	
	private enum SLAYER_HELMETS {
		BLACK(19639, 19641, 7980),
		GREEN(19643, 19645, 7981),
		RED(19647, 19649, 7979),
		PURPLE(21264, 21266, 21275),
		REVERT(11864, 11865, -1);
		
		private int regular, imbued, head;
		public int getRegular() {
			return regular;
		}
		public int getImbued() {
			return imbued;
		}
		//WILL BE USED FOR RECOLOURING :)
		@SuppressWarnings("unused")
		public int getHead() {
			return head;
		}
		SLAYER_HELMETS(int regular, int imbued, int head) {
			this.regular = regular;
			this.imbued = imbued;
			this.head = head;
		}
	}

	public void setPoints(int points) {
		this.points = points;
		updatePoints();
	}
	
	public void takePoints(int points) {
		this.points -= points;
		updatePoints();
	}
	
	public void givePoints(int points) {
		this.points += points;
		updatePoints();
	}

	public int getPoints() {
		return points;
	}

	public int getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(int taskAmount) {
		this.taskAmount = taskAmount;
	}

	public int getMaster() {
		return master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public int getConsecutiveTasks() {
		return consecutiveTasks;
	}

	public void setConsecutiveTasks(int consecutiveTasks) {
		this.consecutiveTasks = consecutiveTasks;
	}

	public void setRemoved(String[] removed) {
		this.removed = removed;
	}

	public String[] getRemoved() {
		return removed;
	}

	public Optional<SlayerTask> getTask() {
		return task;
	}

	public void setTask(Optional<SlayerTask> task) {
		this.task = task;
	}
}
