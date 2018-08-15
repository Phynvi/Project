package com.overload.game.model.teleportation.chatbox;

import com.overload.game.model.Position;

public enum SkillingTeleport {
	
	AGILITY(new Teleport[] {
			new Teleport() {
	
				@Override
				public String[] getWarning() {
					return null;
				}
	
				@Override
				public Position getPosition() {
					//gnome course
					return new Position(2480, 3430);
				}
				
			}, new Teleport() {
	
				@Override
				public String[] getWarning() {
					return null;
				}
	
				@Override
				public Position getPosition() {
					//penguin course
					return new Position(2640, 4042, 1);
				}
				
			}, new Teleport() {
	
				@Override
				public String[] getWarning() {
					return null;
				}
	
				@Override
				public Position getPosition() {
					//barbarian course
					return new Position(2553, 3564);
				}
				
			}, new Teleport() {
				
				@Override
				public String[] getWarning() {
					return null;
				}
	
				@Override
				public Position getPosition() {
					//agility pyramid
					return new Position(3334, 2828);
				}
				
			}, new Teleport() {
				
				@Override
				public String[] getWarning() {
					return null;
				}
	
				@Override
				public Position getPosition() {
					//wildy course
					return new Position(2998, 3915);
				}
				
			},
		
	}),
	
	COOKING(new Teleport[] { 
			new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//rogue's den
					return new Position(3060, 4981, 1);
				}
				
			}, new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//cook's guild
					return new Position(3143, 3442);
				}
				
			}
			
	}),
	
	CRAFTING(new Teleport[] { 
			new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//al-kharid
					return new Position(3277, 3187);
				}
				
			}, new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//seers' village
					return new Position(2711, 3471, 1);
				}
				
			},
			
	}),
	
	FISHING(new Teleport[] {
			new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//fishing guild
					return new Position(2600, 3406);
				}
				
			}, new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//karamja port
					return new Position(2924, 3176);
				}
				
			}, new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//catherby shore
					return new Position(2851, 3431);
				}
				
			}, new Teleport() {

				@Override
				public String[] getWarning() {
					return null;
				}

				@Override
				public Position getPosition() {
					//fremmenik dock
					return new Position(2630, 3681);
				}
				
			},
			
	}),
	
	HERBLORE(new Teleport[] {
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//apothecary shop
						return new Position(3083, 3505);
					}
					
				},
	}),
	
	HUNTER(new Teleport[] { 
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//feldip area
						return new Position(2596, 2906);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return new String[] {
							"This camp is located in level-35 wilderness.",
							"Other players will be able to attack you here.",
							"Are you sure you wish to teleport?"
						};
					}

					@Override
					public Position getPosition() {
						//wilderness camp
						return new Position(3246, 3794);
					}
					
				},
				
	}),
	
	MINING(new Teleport[] {
			
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//varrock mine
						return new Position(3285, 3366);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//falador mine
						return new Position(3051, 9773);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//al-kharid mine
						return new Position(3297, 3299);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//hero's guild mine
						return new Position(2937, 9909);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//essence mine
						return new Position(2910, 4832);
					}
					
				},
	}),
	
	SLAYER(new Teleport[] { 
			
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//Masters' cave
						return new Position(3552, 9502);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//Slayer Tower
						return new Position(3429, 3538);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//icey cavern
						return new Position(3035, 9581);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//fremmenik dungeon
						return new Position(2807, 10002);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//lumbridge swamp
						return new Position(3168, 9572);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//mos le'harmless
						return new Position(2829, 9516);
					}
					
				},
	}),
	
	SMITHING(new Teleport[] {
			
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//edgeville
						return new Position(3106, 3499);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//varrock
						return new Position(3187, 3425);
					}
					
				},	
	}),
	
	THIEVING(new Teleport[] {
	        new Teleport() {
			     @Override
					public String[] getWarning() { return null; }

					@Override
					public Position getPosition() {
						//general stalls
						return new Position(1625, 3740);
					}

				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//ardougne
						return new Position(2660, 3306);
					}
					
				},
	}),
	
	WOODCUTTING(new Teleport[] { 
			
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//lumbridge
						return new Position(3183, 3230);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//draynor
						return new Position(3091, 3234);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//seers' village
						return new Position(2724, 3461);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//seers' village (magic)
						return new Position(2693, 3422);
					}
					
				},
	}),
	
	FARMING(new Teleport[] { 
			
			 new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//falador
						return new Position(3056, 3311);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//catherby
						return new Position(2805, 3464);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//ardougne
						return new Position(2662, 3375);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//canifis
						return new Position(3609, 3522);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//rimmington
						return new Position(2939, 3225);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//south varrock
						return new Position(3181, 3362);
					}
					
				}, new Teleport() {

					@Override
					public String[] getWarning() {
						return null;
					}

					@Override
					public Position getPosition() {
						//south ardougne
						return new Position(2617, 3221);
					}
					
				},
	}),
	;

	private SkillingTeleport(Teleport[] teleports) {
		this.teleports = teleports;
	}
	
	private final Teleport[] teleports;
	
	public Teleport[] getTeleports() {
		return teleports;
	}
}
