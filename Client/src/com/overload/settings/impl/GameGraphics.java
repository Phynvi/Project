package com.overload.settings.impl;

import com.overload.Client;
import com.overload.cache.graphics.widget.Widget;
import com.overload.settings.AdvancedSettings;

public enum GameGraphics {
	FOG(true),
	ANTI_ALIASING(false),
	HD_MINIMAP(false),
	HD_SHADOWS(false),
	ULTRA_DRAW_DISTANCE(false),
	TWEENING(false);
	
	
	GameGraphics(boolean toggled, String... conflicting) {
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
				GameGraphics s = GameGraphics.valueOf(conflict);
				System.out.println("Toggling off "+conflict+" "+s);
				if(s.toggled())
					s.toggle();
			}
		}
		if(this == HD_MINIMAP) {
			Client.instance.loadRegion();
		}
		AdvancedSettings.save();
		Widget.Option_Main(Widget.fonts2);
	}
	
	public String toString() {
		return Client.capitalize(this.name().toLowerCase()).replaceAll("_",  " ").replaceAll("__",  "/").replaceAll("Hd ", "HD ");
	}

	public boolean toggled() {
		return toggled;
	}
}