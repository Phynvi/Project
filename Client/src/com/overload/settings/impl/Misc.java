package com.overload.settings.impl;

import com.overload.Client;
import com.overload.cache.graphics.widget.Widget;
import com.overload.settings.AdvancedSettings;

public enum Misc {
	TOGGLE_ROOFS(false),
	GROUND_ITEM_NAMES(true),
	NAMES_ABOVE_HEAD(false),
	HP_ABOVE_HEAD(false);
	
	
	Misc(boolean toggled, String... conflicting) {
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
				Misc s = Misc.valueOf(conflict);
				System.out.println("Toggling off "+conflict+" "+s);
				if(s.toggled())
					s.toggle();
			}
		}
		AdvancedSettings.save();
		Widget.Option_Main_sub2(Widget.fonts2);
	}
	
	public String toString() {
		return Client.capitalize(this.name().toLowerCase()).replaceAll("_",  " ").replaceAll("__",  "/").replaceAll("Hd ", "HD ");
	}

	public boolean toggled() {
		return toggled;
	}
}