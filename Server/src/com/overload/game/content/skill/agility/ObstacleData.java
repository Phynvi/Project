package com.overload.game.content.skill.agility;

import java.util.Optional;

import com.overload.game.content.combat.hit.HitDamage;
import com.overload.game.content.combat.hit.HitMask;
import com.overload.game.entity.Entity;
import com.overload.game.entity.impl.object.GameObject;
import com.overload.game.entity.impl.object.MapObjects;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Action;
import com.overload.game.model.Animation;
import com.overload.game.model.Flag;
import com.overload.game.model.ForceMovement;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.game.task.impl.ForceMovementTask;
import com.overload.util.Misc;

public enum ObstacleData {

	/* GNOME COURSE */
	LOG(23145, true, 0) {
		@Override
		public void cross(final Player player) {
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.moveTo(new Position(2474, 3436));
			player.getPacketSender().sendMessage("You attempt to walk across the log..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 8;

				@Override
				public void execute() {
					tick--;
					player.getMovementQueue().walkStep(0, -1);
					if (tick <= 0)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 60);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely make your way across the log.");
				}
			});
		}
	},
	NET(23134, false, 0) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the net..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 2) {
						player.moveTo(new Position(2473, 3423, 1));
						Agility.addExperience(player, 40);
					} else if (tick == 3) {
						player.setCrossedObstacle(1, true).setCrossingObstacle(false);
						stop();
					}
					tick++;
				}
			});
		}
	},
	BRANCH(23559, false, 0) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the branch..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2473, 3420, 2));
					Agility.addExperience(player, 42);
					player.setCrossedObstacle(2, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	ROPE(23557, true, 0) {
		@Override
		public void cross(final Player player) {
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to walk across the rope..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(1, 0);
					if (tick >= 6)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(3, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 25);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely walk across the rope.");
				}
			});
		}
	},
	BRANCH_2(23560, false, 0) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the branch..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
					Agility.addExperience(player, 42);
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
    NETS_2(23135, false, 0) {
        @Override
        public void cross(final Player player) {
            if (player.getPosition().getY() != 3425) {
                player.setCrossingObstacle(false);
                return;
            }
            player.getPacketSender().sendMessage("You climb the net..");
            player.performAnimation(new Animation(828));
            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {
                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() + 2, 0));
                    Agility.addExperience(player, 15);
                    player.setCrossedObstacle(5, true).setCrossingObstacle(false);
                    stop();
                }
            });
        }
    },
	PIPE_1(23139, true, 0) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2487, 3430));
			player.getPacketSender().sendMessage("You attempt to go through the pipe..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 3 || tick >= 4) {
						if (player.getSkillAnimation() != 844) {
							player.setSkillAnimation(844);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					} else {
						if (player.getSkillAnimation() != -1) {
							player.setSkillAnimation(-1);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					}

					tick++;
					player.getMovementQueue().walkStep(0, 1);
					if (tick >= 4)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.moveTo(new Position(2487, 3437));
					player.setCrossedObstacle(6, true).setCrossingObstacle(false).setSkillAnimation(-1);
					player.getClickDelay().reset();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					if (Agility.passedAllObstacles(player)) {
						player.getInventory().add(2996, 2);
						Agility.addExperience(player, 10200 + Misc.getRandom(1000));
					}
					player.getPacketSender().sendMessage("You manage to make your way through the pipe.");
				}
			});
		}
	},
	PIPE_2(23138, true, 0) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2484, 3430));
			player.getPacketSender().sendMessage("You attempt to go through the pipe..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 3 || tick >= 4) {
						if (player.getSkillAnimation() != 844) {
							player.setSkillAnimation(844);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					} else {
						if (player.getSkillAnimation() != -1) {
							player.setSkillAnimation(-1);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					}

					tick++;
					player.getMovementQueue().walkStep(0, 1);
					if (tick >= 4)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.moveTo(new Position(2483, 3437));
					player.setCrossedObstacle(6, true).setCrossingObstacle(false).setSkillAnimation(-1);
					player.getClickDelay().reset();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					if (Agility.passedAllObstacles(player)) {
						player.getInventory().add(2996, 2);
						Agility.addExperience(player, 10200 + Misc.getRandom(1000));
					}
					player.getPacketSender().sendMessage("You manage to make your way through the pipe.");
				}
			});
		}

	},

	/* BARBARIAN OUTPOST COURSE */
	ROPE_SWING(23131, true, 10) {
		@Override
		public void cross(final Player player) {
//			if (player.getCrossedObstacle(0)) {
//                player.setCrossingObstacle(false);
//				player.getPacketSender().sendMessage("You must complete the course to do this again.");
//				return;
//			}
			player.getPacketSender().sendMessage("You attempt to swing on the ropeswing..");
			player.moveTo(new Position(2551, 3554));
			final boolean success = Agility.isSuccessive(player);

			final Position swing = new Position(0, success ? -5 : -2);

			Optional<GameObject> obj = MapObjects.get(23131, new Position(2551, 3550));

			if (obj.isPresent()) {
				obj.get().performAnimation(new Animation(497));
			}
			if (success)
            	super.cross(player);

			TaskManager.submit(
				new ForceMovementTask(
					player,
					2,
					new ForceMovement(
						player.getPosition().clone(),
						swing,
						0,
						70,
						2,
						751
					),
					Optional.of(new Action() {
	                    @Override
	                    public void execute() {
	                        player.setCrossingObstacle(false);
	                        player.setCrossedObstacle(0, true).setCrossingObstacle(false).setSkillAnimation(-1);
	                        if (!success) {
		                        player.forceChat("OUCH!");
		                        player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(10), HitMask.RED));
		                        player.moveTo(new Position(2551, 3549));
	                        }
	                    }
	                    @Override
	                    public void execute(Entity entity) {}
					})
				)
			);
		}
	},
	LOG_2(23144, true, 5) {
		@Override
		public void cross(final Player player) {
			final boolean fail = !Agility.isSuccessive(player);
			player.getPacketSender().sendMessage("You attempt to walk-over the log..");
			player.setSkillAnimation(762);
			player.moveTo(new Position(2550, 3546, 0));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(-1, 0);
					if (tick >= 9 || player == null)
						stop();
					if (tick == 5 && fail) {
						stop();
						tick = 0;
						player.getMovementQueue().reset();
						player.performAnimation(new Animation(764));
						TaskManager.submit(new Task(1, player, true) {
							int tick2 = 0;

							public void execute() {
								if (tick2 == 0) {
									player.moveTo(new Position(2546, 3547));
									player.decrementHealth(new HitDamage(Misc.getRandom(50), HitMask.RED));
								}
								tick2++;
								player.setSkillAnimation(772);
								player.getUpdateFlag().flag(Flag.APPEARANCE);
								player.getMovementQueue().walkStep(0, 1);
								if (tick2 >= 4) {
									player.getPacketSender()
											.sendMessage("You are unable to make your way across the log.");
									player.setCrossedObstacle(1, false).setCrossingObstacle(false)
											.setSkillAnimation(-1);
									player.getUpdateFlag().flag(Flag.APPEARANCE);
									stop();
									return;
								}
							}
						});
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					if (!fail) {
						player.setCrossedObstacle(1, true).setCrossingObstacle(false).setSkillAnimation(-1);
						Agility.addExperience(player, fail ? 5 * 3 : 60 * 3);
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						player.moveTo(new Position(2541, 3546));
						player.getPacketSender().sendMessage("You safely make your way across the log.");
					}
				}
			});
		}
	},
	NET_3(20211, false, 0) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the net..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2537 + Misc.getRandom(1), 3546 + Misc.getRandom(1), 1));
					Agility.addExperience(player, 30 * 3);
					player.setCrossedObstacle(2, true).setSkillAnimation(-1).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	BALANCE_LEDGE(23547, true, 0) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getX() != 2536) {
				player.setCrossingObstacle(false);
				return;
			}
			player.getPacketSender().sendMessage("You attempt to make your way across the ledge..");

			final boolean fallDown = !Agility.isSuccessive(player);
			player.setCrossingObstacle(true);
			player.moveTo(new Position(2536, 3547, 1));
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.setSkillAnimation(756);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getMovementQueue().walkStep(-1, 0);
					if (tick == 3 && fallDown) {
						player.performAnimation(new Animation(761));
						stop();
						TaskManager.submit(new Task(1) {
							@Override
							public void execute() {
								player.moveTo(new Position(2535, 3546, 0));
								player.decrementHealth(new HitDamage(Misc.getRandom(50), HitMask.RED));
								player.getMovementQueue().walkStep(0, -1);
								player.setCrossedObstacle(3, false).setSkillAnimation(-1);
								player.getUpdateFlag().flag(Flag.APPEARANCE);
								Agility.addExperience(player, 6 * 3);
								player.getPacketSender().sendMessage("You accidently slip and fall down!");
								TaskManager.submit(new Task(1) {
									@Override
									public void execute() {
										player.setCrossingObstacle(false);
										stop();
									}
								});
								stop();
							}
						});
					} else if (tick == 4) {
						player.setCrossedObstacle(3, true).setSkillAnimation(-1).setCrossingObstacle(false);
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						Agility.addExperience(player, 40 * 3);
						player.getPacketSender().sendMessage("You safely move across the ledge.");
						stop();
					}
				}
			});
		}
	},
	LADDER(16682, false, 0) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(827));
			player.getPacketSender().sendMessage("You climb down the ladder...");
			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2532, 3546, 0));
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	RAMP(1948, false, 0) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getX() != 2535 && player.getPosition().getX() != 2538
					&& player.getPosition().getX() != 2542 && player.getPosition().getX() != 2541) {
				player.getPacketSender().sendMessage("You cannot jump over the wall from this side!");
				player.setCrossingObstacle(false);
				return;
			}
			final boolean first = player.getPosition().getX() == 2535;
			final boolean oneStep = player.getPosition().getX() == 2537 || player.getPosition().getX() == 2542;
			player.getPacketSender().sendMessage("You attempt to jump over the wall...");
			player.performAnimation(new Animation(1115));
			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(player.getPosition().getX() + (oneStep ? 1 : 2), 3553));
					player.setCrossingObstacle(false).setCrossedObstacle(first ? 5 : 6, true);
					if (player.getPosition().getX() == 2543 && player.getPosition().getY() == 3553) {
						if (Agility.passedAllObstacles(player)) {
							//DialogueManager.start(player, 57);
							player.getInventory().add(2996, 4);
							Agility.addExperience(player, 14500 + Misc.getRandom(1000));
						} else {
							//DialogueManager.start(player, 56);
						}
						player.getPacketSender().sendMessage("You manage to jump over the wall.");
					}
					stop();
				}
			});
		}
	},

	/* WILD COURSE */
	GATE_1(23555, true, 0) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2998, 3917));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(0, 1);
					if (player.getPosition().getY() == 3930 || tick >= 15) {
						player.moveTo(new Position(2998, 3931, 0));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	GATE_2(2308, true, 0) {
		@Override
		public void cross(final Player player) {
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			player.moveTo(new Position(2998, 3930));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(0, -1);
					if (player.getPosition().getY() == 3917 || tick >= 15) {
						player.moveTo(new Position(2998, 3916));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	GATE_3(2308, true, 0) {
		@Override
		public void cross(final Player player) {
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			player.moveTo(new Position(2998, 3930));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(0, -1);
					if (player.getPosition().getY() == 3917 || tick >= 15) {
						player.moveTo(new Position(2998, 3916));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	PIPE_3(23137, true, 0) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(3004, 3937));
			player.setSkillAnimation(844);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to squeeze through the pipe..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(0, 1);
					if (tick == 4)
						player.moveTo(new Position(3004, 3947));
					else if (tick == 7)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossedObstacle(1, true).setCrossedObstacle(2, true)
							.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 175);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to squeeze through the pipe.");
				}
			});
		}
	},
	ROPE_SWING_2(23132, true, 0) {
		@Override
        public void cross(final Player player) {
//            if (player.getCrossedObstacle(0)) {
//                player.setCrossingObstacle(false);
//                player.getPacketSender().sendMessage("You must complete the course to do this again.");
//                return;
//            }
            player.getPacketSender().sendMessage("You attempt to swing on the ropeswing..");
            player.moveTo(new Position(3005, 3953));
            final boolean success = Agility.isSuccessive(player);

            final Position swing = new Position(0, success ? +5 : +2);

            Optional<GameObject> obj = MapObjects.get(23132, new Position(3005, 3952));

            if (obj.isPresent()) {
                obj.get().performAnimation(new Animation(497));
            }
            if (success)
                super.cross(player);

            TaskManager.submit(
                    new ForceMovementTask(
                            player,
                            2,
                            new ForceMovement(
                                    player.getPosition().clone(),
                                    swing,
                                    0,
                                    70,
                                    0,
                                    751
                            ),
                            Optional.of(new Action() {
                                @Override
                                public void execute() {
                                    player.setCrossingObstacle(false);
                                    player.setCrossedObstacle(0, true);
                                    if (!success) {
                                        player.forceChat("OUCH!");
                                        player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(10), HitMask.RED));
                                        player.moveTo(new Position(3005, 3958));
                                    }
                                }
                                @Override
                                public void execute(Entity entity) {}
                            })
                    )
            );
        }
    },
	STEPPING_STONES(23556, true, 0) {
		@Override
		public void cross(final Player player) {
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to pass the stones..");
			if (player.getWildernessLevel() == 0) {
				if (player.getPosition().getY() != 10002) {
					if (player.getPosition().getX() <= 2769) {
						player.getPosition().setY(10002);
					}
				} else if (player.getPosition().getY() != 10003) {
					if (player.getPosition().getX() >= 2774) {
						player.getPosition().setY(10003);
					}
				}
			}
			TaskManager.submit(new Task(1, player, false) {
				int tick = 1;

				@Override
				public void execute() {
					tick++;
					player.performAnimation(new Animation(769));
					if (tick == 1 || tick == 2 || tick == 3 || tick == 4 || tick == 5) {
						if (player.getPosition().getY() == 10002) {
							player.moveTo(new Position(player.getPosition().getX() + 1, player.getPosition().getY()));
						} else
							player.moveTo(new Position(player.getPosition().getX() - 1, player.getPosition().getY()));
					} else if (tick >= 6) {
						if (player.getWildernessLevel() != 0) {
							player.moveTo(new Position(2996, 3960, 0));
						} else {
							if (player.getPosition().getX() <= 2770) {
								player.moveTo(new Position(2769, 10002, 0));
							} else if (player.getPosition().getX() >= 2773) {
								player.moveTo(new Position(2775, 10003, 0));
							}
						}
						Agility.addExperience(player, 250);
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					Agility.addExperience(player, 100);
					player.getPacketSender().sendMessage("You manage to pass the stones.");
				}
			});
		}
	},
	BALANCE_LEDGE_2(23542, true, 0) {

		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(3001, 3945, 0));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			final boolean fail = !Agility.isSuccessive(player);
			player.getPacketSender().sendMessage("You attempt to make your way over the log..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;
				@Override
				public void execute() {
					tick++;
					player.getMovementQueue().walkStep(-1, 0);
					if (tick >= 10)
						stop();
					else if (fail && tick >= 3) {
						player.moveTo(new Position(3000, 10346));
						player.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(10), HitMask.RED));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(5, fail ? false : true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, fail ? 10 : 250);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely make your way over the log.");
				}
			});
		}

	},
	CLIMB_WALL(23640, false, 0) {

		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You attempt to climb up the wall..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2996, 3933, 0));
					stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(6, true).setCrossingObstacle(false);
					Agility.addExperience(player, 100);
					if (Agility.passedAllObstacles(player)) {
						//DialogueManager.start(player, 57);
						player.getInventory().add(2996, 6);
						Agility.addExperience(player, 21544 + Misc.getRandom(2000));
					} else {
						//DialogueManager.start(player, 56);
					}
					player.getPacketSender().sendMessage("You manage to climb up the wall.");
				}
			});
		}

	},
	LADDER_2(14758, false, 0) {

		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(827));
			player.setEntityInteraction(null);
			player.setCrossingObstacle(false);
			TaskManager.submit(new Task(1) {
				@Override
				public void execute() {
					player.moveTo(new Position(3005, 10362, 0));
					stop();
				}
			});
		}

	},

    /**
     * Ardounge Roof
     */

    CLIMB_WALL_ARDOUNGE(11405, false, 0) {
        @Override
        public void cross(final Player player) {
            player.performAnimation(new Animation(737));
            player.getPacketSender().sendMessage("You climb the wall..");
            TaskManager.submit(new Task(1, player, false) {
                int tick = 0;

                @Override
                public void execute() {
                    tick++;
                    if (tick == 1) {
                        player.performAnimation(new Animation(737));
                        player.moveTo(new Position(2673, 3298, 1));
                        if (tick == 2) {
                            player.performAnimation(new Animation(737));
                            player.moveTo(new Position(2673, 3298, 2));
                        } else if (tick == 3) {
                            player.performAnimation(new Animation(737));
                            player.moveTo(new Position(2671, 3299,  3));
                            Agility.addExperience(player, 40);
                            player.setCrossedObstacle(1, true).setCrossingObstacle(false);
                            stop();
                        }
                    }
                }
            });
        }
    },

    /** MISC **/

    PYRAMID_CLIMB(11948, false, 0) {

        @Override// check the postion of the player when hes down, i am doing but we have to remember this....
        public void cross(final Player player) {
            int off = 4;
            off *= (player.getX() > 3336) ? -1 : 1;

            player.setPositionToFace(new Position(player.getX() + (off > 0 ? -1: 1), player.getY(), player.getZ()));
            player.getUpdateFlag().flag(Flag.FORCED_MOVEMENT);

            player.setEntityInteraction(null);
            player.setCrossingObstacle(false);

            TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.getPosition(), new Position(off, 0, 0), 0, 100, 1, 737)));
        }

    },
    PYRAMID_CLIMB_2(11949, false, 0) {

        @Override// check the postion of the player when hes down, i am doing but we have to remember this....
        public void cross(final Player player) {
            int off = 4;
            off *= (player.getX() > 3350) ? -1 : 1;

            player.setPositionToFace(new Position(player.getX() + (off > 0 ? -1: 1), player.getY(), player.getZ()));
            player.getUpdateFlag().flag(Flag.FORCED_MOVEMENT);

            player.setEntityInteraction(null);
            player.setCrossingObstacle(false);

            TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.getPosition(), new Position(off, 0, 0), 0, 100, 1, 737)));
        }

    },
	RED_DRAGON_LOG_BALANCE(5088, false, 0) {

		@Override
		public void cross(final Player player) {
			player.setCrossingObstacle(true).setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			final int moveX = player.getPosition().getX() > 2683 ? 2686 : 2683;
			player.moveTo(new Position(moveX, 9506));
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 4)
						player.getMovementQueue().walkStep(moveX == 2683 ? +1 : -1, 0);
					else if (tick == 4) {
						player.setSkillAnimation(-1).setCrossingObstacle(false);
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						Agility.addExperience(player, 32);
						stop();
					}
					tick++;
				}
			});
		}

	},;

	ObstacleData(int object, boolean mustWalk, int exp) {
		this.object = object;
		this.mustWalk = mustWalk;
		this.exp = exp;
	}

	private int object;
	private boolean mustWalk;
	private int exp;

	public int getObject() {
		return object;
	}

	public boolean mustWalk() {
		return mustWalk;
	}

	public int getExp() {
		return exp;
	}

	public void cross(final Player player) {
		Agility.addExperience(player, getExp());
	}

	public static ObstacleData forId(int object) {
		if (object == 2993 || object == 2328 || object == 2995 || object == 2994)
			return CLIMB_WALL;
		else if (object == 2307)
			return GATE_2;
		else if (object == 5088 || object == 5090)
			return RED_DRAGON_LOG_BALANCE;
		for (ObstacleData obstacleData : ObstacleData.values()) {
			if (obstacleData.getObject() == object)
				return obstacleData;
		}
		return null;
	}
}