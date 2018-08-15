package com.overload.game.content.achievement;

import java.util.EnumSet;
import java.util.Set;

import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Item;
import com.overload.util.Misc;

public class Achievement {
	
	public enum AchievementType {
		PVM
	};
	
	public enum Achievements {
		Crab_Killer(0, AchievementTier.EASY, AchievementType.PVM, "Kill 100 Rock Crabs", 100, 1, new Item(995, 50000), new Item(4153)),
		;

		private AchievementTier tier;
		private AchievementType type;
		private String description;
		private int amount, identification, points;
		private final Item[] rewards;

		Achievements(int identification, AchievementTier tier, AchievementType type, String description, int amount, int points, Item... rewards) {
			this.identification = identification;
			this.tier = tier;
			this.type = type;
			this.description = description;
			this.amount = amount;
			this.points = points;
			this.rewards = rewards;

			//format the items
			for (Item b : rewards) if (b.getAmount() == 0) b.setAmount(1);

		}

		public int getId() {
			return identification;
		}

		public AchievementTier getTier() {
			return tier;
		}

		public AchievementType getType() {
			return type;
		}

		public String getDescription() {
			return description;
		}

		public int getAmount() {
			return amount;
		}

		public int getPoints() {
			return points;
		}

		public Item[] getRewards() {
			return rewards;
		}

		public static final Set<Achievements> ACHIEVEMENTS = EnumSet.allOf(Achievements.class);

		public static Achievements getAchievement(AchievementTier tier, int ordinal) {
			for (Achievements achievement : ACHIEVEMENTS)
				if (achievement.getTier() == tier && achievement.ordinal() == ordinal)
					return achievement;
			return null;
		}

		public static boolean hasRequirement(Player player, AchievementTier tier, int ordinal) {
			for (Achievements achievement : ACHIEVEMENTS) {
				if (achievement.getTier() == tier && achievement.ordinal() == ordinal) {
					return true;
				}
			}
			return false;
		}
	}

	public static void increase(Player player, int amount) {
		for (Achievements achievement : Achievements.ACHIEVEMENTS) {
			int currentAmount = player.getAchievements().getAmountRemaining(achievement.getTier().ordinal(), achievement.getId());
			int tier = achievement.getTier().ordinal();
			if (currentAmount < achievement.getAmount() && !player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
				player.getAchievements().setAmountRemaining(tier, achievement.getId(), currentAmount + amount);
				if ((currentAmount + amount) >= achievement.getAmount()) {
					String name = achievement.name().replaceAll("_", " ");
					player.getAchievements().setComplete(tier, achievement.getId(), true);
					player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
					player.sendMessage("Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).");
					if (achievement.getTier().ordinal() > 0) {
						for (Player p : World.getPlayers()) {
							if (p == null)
								continue;
							Player c = p;
							c.sendMessage("@red@[ACHIEVEMENT]@blu@ " + Misc.capitalizeWords(player.getUsername()) + " @bla@completed the achievement @blu@" + name + " @bla@on tier @blu@" + (tier + 1) + ".");
						}
					}
					//add reward inventory if spots free
					if (achievement.getRewards() != null) {
						player.sendMessage("Your achievement reward(s) has been added to your account.");
						for (Item b : achievement.getRewards()) {
							if (player.getInventory().isFull()) {
								player.getBank(0).forceAdd(player, b);
							} else {
								player.getInventory().add(b);
							}
						}
					}
				}
			}
		}
	}

	public static void reset(Player player) {
		for (Achievements achievement : Achievements.ACHIEVEMENTS) {
			if (!player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
				player.getAchievements().setAmountRemaining(achievement.getTier().ordinal(), achievement.getId(), 0);
			}
		}
	}

	public static void complete(Player player) {
		for (Achievements achievement : Achievements.ACHIEVEMENTS) {
			if (!player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
				int tier = achievement.getTier().ordinal();
				//String name = achievement.name().replaceAll("_", " ");
				player.getAchievements().setAmountRemaining(tier, achievement.getId(), achievement.getAmount());
				player.getAchievements().setComplete(tier, achievement.getId(), true);
				player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
				player.sendMessage("Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).");
			}
		}
	}

	public static void checkIfFinished(Player player) {
		
	}

	public static int getMaximumAchievements() {
		return Achievements.ACHIEVEMENTS.size();
	}
}
