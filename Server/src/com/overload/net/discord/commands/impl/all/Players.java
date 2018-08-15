package com.overload.net.discord.commands.impl.all;

import java.awt.Color;

import com.overload.game.World;
import com.overload.game.content.combat.bountyhunter.BountyHunter;
import com.overload.net.discord.DiscordMisc;
import com.overload.net.discord.commands.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Players implements Command {
	
	 @Override
	    public boolean called(String[] args, MessageReceivedEvent event) {
	        return false;
	    }

	    @Override
	    public void action(String[] args, MessageReceivedEvent event) {
	    	DiscordMisc.sendMessage(event,"There are currently " + World.getPlayers().size() + " players online and " + BountyHunter.PLAYERS_IN_WILD.size() + " players in the Wilderness.",Color.CYAN);
	    }

	    @Override
	    public void executed(boolean sucess, MessageReceivedEvent event) {
	        
	    }

	    @Override
	    public String description() {
	        return "Displays Number of Players Online.";
	    }


}
