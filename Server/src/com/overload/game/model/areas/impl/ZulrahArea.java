package com.overload.game.model.areas.impl;

import com.overload.game.World;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Animation;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;
import com.overload.util.timers.TimerKey;

import java.util.Arrays;
import java.util.Optional;

public class ZulrahArea extends PrivateArea {
    public static final Boundary BOUNDARY = new Boundary(2248, 3059, 2283, 3084, true);
    
    public ZulrahArea() {
        super(Arrays.asList(BOUNDARY));
    }

    @Override
    public void enter(Character character) {
        add(character);
    	if (character.isPlayer()) {
    		Player player = character.getAsPlayer();
    		
    		int index = player.getIndex() * 4;
            
	    	NPC npc = new NPC(2042, new Position(2268, 3073, index));
	    	
            npc.getDefinition().setAggressive(false);
            player.setUntargetable(true);
	        player.getCombat().reset();
	        npc.getCombat().reset();
	        TaskManager.submit(new Task(1, npc, true) {
	            int tick = 0;
	            @Override
	            protected void execute() {
	            	if (tick == 2) {
	                    World.getAddNPCQueue().add(npc);
	                    npcs.add(npc);
	                    enter(npc);
	        	        npc.getTimers().register(TimerKey.ATTACK_IMMUNITY, 3);
	                    npc.performAnimation(new Animation(5071));
	                } else if (tick == 4) {
	                    stop();
	                }
	                tick++;
	            }
	            
	            @Override
	            public void stop() {
                    npc.setPositionToFace(player.getPosition());
	                player.setUntargetable(false);
	                npc.getDefinition().setAggressive(true);
	                npc.getCombat().attack(player);
	                npc.getCombat().setTarget(player);
	                setEventRunning(false);
	            }
	        });
    	}
    }
    
    @Override
    public void leave(Character character) {
    	super.leave(character);
    }

    @Override
    public void process(Character character) {
    	
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public boolean canAttack(Character attacker, Character target) {
        return true;
    }

    @Override
    public void defeated(Player player, Character character) {

    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return false;
    }

    @Override
    public boolean isMulti(Character character) {
        return true;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean dropItemsOnDeath(Player player, Optional<Player> killer) {
        return false;
    }

    @Override
    public boolean handleDeath(Player player, Optional<Player> killer) {
    	player.getZulrahLostItems().store();
    	leave(player);
        return false;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {

    }

    @Override
    public boolean handleObjectClick(Player player, int objectId, int type) {
        return false;
    }

    @Override
    public boolean isWilderness() {
        return false;
    }
    
    @Override
	public Position getLootDropPosition() { return new Position(1180, 6109); }
}
