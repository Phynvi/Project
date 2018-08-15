package com.overload.game.model.areas.impl;

import com.overload.game.World;
import com.overload.game.entity.impl.Character;
import com.overload.game.entity.impl.npc.NPC;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.model.Boundary;
import com.overload.game.model.Position;
import com.overload.game.task.Task;
import com.overload.game.task.TaskManager;

import java.util.Arrays;
import java.util.Optional;

public class WinterTodtArea extends PrivateArea {

    public static final Boundary BOUNDARY = new Boundary(1611, 3968, 1649, 4026, true);

    public WinterTodtArea() {
        super(Arrays.asList(BOUNDARY));
    }

    @Override
    public void enter(Character character) {
        add(character);
        if (character.isPlayer()) {
            Player player = character.getAsPlayer();

            NPC pyro1 = new NPC(7371, new Position(1619, 3996));
            NPC pyro2 = new NPC(7371, new Position(1619, 3996));
            NPC pyro3 = new NPC(7371, new Position(1619, 3996));
            NPC pyro4 = new NPC(7371, new Position(1619, 3996));

            TaskManager.submit(new Task(1, player, true) {
                int tick = 0;
                @Override
                protected void execute() {
                    if (tick == 2) {
                        World.getAddNPCQueue().add(pyro1);World.getAddNPCQueue().add(pyro2);World.getAddNPCQueue().add(pyro3);World.getAddNPCQueue().add(pyro4);
                        npcs.add(pyro1);npcs.add(pyro2);npcs.add(pyro3);npcs.add(pyro4);
                        enter(pyro1);enter(pyro2);enter(pyro3);enter(pyro4);
                    } else if (tick == 3) {
                        stop();
                    }
                }
            });

        }
    }

    @Override
    public void process(Character character) {

    }

    @Override
    public boolean canTeleport(Player player) {
        return false;
    }

    @Override
    public boolean canAttack(Character attacker, Character target) {
        return false;
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
        return false;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return false;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return false;
    }

    @Override
    public boolean dropItemsOnDeath(Player player, Optional<Player> killer) {
        return false;
    }

    @Override
    public boolean handleDeath(Player character, Optional<Player> killer) {
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
}
