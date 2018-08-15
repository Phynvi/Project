package com.overload.net.discord.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

	public void onReady(ReadyEvent event) {
        String out = "This bot is running on following servers: ";
        for (Guild g : event.getJDA().getGuilds() ) {
            out += g.getName();
        }
        System.err.println(out);
    }
	
}
