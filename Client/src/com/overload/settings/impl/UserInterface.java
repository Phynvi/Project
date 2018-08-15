package com.overload.settings.impl;

import com.overload.Client;
import com.overload.cache.graphics.widget.Widget;
import com.overload.settings.AdvancedSettings;

public enum UserInterface {
	SKILL_ORBS(true),
	COMBAT_OVERLAY(true),
	TIMERS(true),
	TRANSPARENT_RIGHT_CLICK(false),
	TOGGLE_ORBS(true),
	TOGGLE_SPEC_ORBS(true),
	TRANSPARENT_CHAT_BOX(false),
	TRANSPARENT_INVENTORY(false),
	SIDE_STONE_ARRANGEMENTS(false),
	DISABLE_CHAT_ICONS(false),
	NEW_HITMARKS(false),
	NEW_HP_BAR(false);
	
	
	UserInterface(boolean toggled, String... conflicting) {
		this.toggled = toggled;
		this.conflicting = conflicting;
	}
	

	public boolean toggled;
	public final String[] conflicting;

	public static int size = 0;
	public static int privateChatColor;
	public static String username = "";

	public void toggle() {
		toggled = !toggled;

		if(toggled) {
			for(String conflict : conflicting) {
				UserInterface s = UserInterface.valueOf(conflict);
				System.out.println("Toggling off "+conflict+" "+s);
				if(s.toggled())
					s.toggle();
			}
		}
		AdvancedSettings.save();
		Widget.Option_Main_sub1(Widget.fonts2);
	}
	
	public String toString() {
		return Client.capitalize(this.name().toLowerCase()).replaceAll("_",  " ").replaceAll("__",  "/").replaceAll("Hd ", "HD ");
	}

	public boolean toggled() {
		return toggled;
	}
}