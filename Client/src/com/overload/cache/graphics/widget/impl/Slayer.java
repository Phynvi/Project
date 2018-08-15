package com.overload.cache.graphics.widget.impl;

import com.overload.cache.graphics.GameFont;
import com.overload.cache.graphics.widget.Widget;
import com.overload.util.MiscUtils;

public class Slayer extends Widget {

	private static final int MAIN_START = 47505;
	
	private static final int[] MAIN_INTERFACE =    {47500, 47501, 47502, 47503, 47504};
	private static final int[] SCROLL_CONTAINERS = {47525, 47526, 47527, 47528, 47529};
	private static final int[] SCROLL_STARTS =     {47550, 47700, 47850, 48000, 48150};
    
	public static void init(GameFont[] gf) {
		for (int i = 0; i < MAIN_INTERFACE.length-1; i++) {
			int curr = MAIN_START;
			int parent = MAIN_INTERFACE[i];
			Widget widget = addInterface(parent);
	
			addSpriteLoader(curr++, 731);
			addText(curr++, "Slayer Rewards", gf, 2, 0xFF9900, true, true);
			addClose(curr++);
			addText(curr++, "Reward points: " + (curr-1), gf, 0, 0xFF9900, true, true);
			
			hoverButton(curr++, "Unlock", 732, 733);
			hoverButton(curr++, "Extend", 732, 733);
			hoverButton(curr++, "Buy",    732, 733);
			hoverButton(curr++, "Task",   732, 733);
			addHoverText(curr++, "Unlock", "Unlock", gf, 0, 0xFF9900, true, true, 83, 20, 0xFFFFFF);
			addHoverText(curr++, "Extend", "Extend", gf, 0, 0xFF9900, true, true, 83, 20, 0xFFFFFF);
			addHoverText(curr++, "Buy",    "Buy",    gf, 0, 0xFF9900, true, true, 83, 20, 0xFFFFFF);
			addHoverText(curr++, "Task",   "Task",   gf, 0, 0xFF9900, true, true, 83, 20, 0xFFFFFF);
			
			int childCurr = MAIN_START;
			widget.totalChildren(13);
			widget.child(0, childCurr++, 12,    20);//Background
			widget.child(1, childCurr++, 488/2, 30);//Title
			widget.child(2, childCurr++, 472,   27);//Close Button
			widget.child(3, childCurr++, 435,   61);//Reward Points Text
	
			widget.child(4, childCurr++,  23+(87*0), 56);//Button Unlock
			widget.child(5, childCurr++,  23+(87*1), 56);//Button Extend
			widget.child(6, childCurr++,  23+(87*2), 56);//Button Buy
			widget.child(7, childCurr++,  23+(87*3), 56);//Button Task
			widget.child(8, childCurr++,  23+(87*0), 61);//Button Unlock
			widget.child(9, childCurr++,  23+(87*1), 61);//Button Extend
			widget.child(10, childCurr++, 23+(87*2), 61);//Button Buy
			widget.child(11, childCurr++, 23+(87*3), 61);//Button Task
			
			widget.child(12, SCROLL_CONTAINERS[i], 16, 80);
			
		}
		
		unlockScroll( gf, 0);
		extendScroll( gf, 1);
//		buyScroll(    gf, 2);
//		taskScroll(   gf, 3);
//		infoInterface(gf, 4);
	}
	//47500
	public static void unlockScroll(GameFont[] gf, int tab) {
		int curr = SCROLL_STARTS[tab];
		int parent = SCROLL_CONTAINERS[tab];
		Widget scrollWidget = addTabInterface(parent);

		int[] sprites = {
			736, 737,
			738, 739,
			740, 741,
			742, 743,
			744, 745,
			746, 747,
			748, 749,
			750, 751,
			752, 753,
			754, 755,
			756
		};
		
		String[] unlocks = {
			"Gargoyle smasher", "Slug salter",
			"Reptile freezer", "'Shroom sprayer",
			"Broader Fletching", "Malevolent masquerade",
			"Ring bling", "Seeing red",
			"I hope you mith me", "Watch the birdie",
			"Hot stuff", "Reptile got ripped",
			"Like a boss", "King black bonnet",
			"Bigget and Badder", "Kalphite khat",
			"Unholy helmet", "Dark Mantle",
			"Undead Head", "Duty Noted",
			"Stop the Wyvern"
		};
		
		String[][] details = {
			{"Gargoyles are automatically dealt the",		"finishing blow if the player has a Rock",				"hammer in their inventory. @red@(120 @red@Points)"},
			{"Rock slugs are automatically dealt the",		"finishing blow if the player has a Bag of",			"salt in their inventory. @red@(80 @red@Points)"},
			{"Desert lizards are automatically dealt",		"the finishing blow if the player has an Ice",			"cooler in their inventory. @red@(90 @red@Points)"},
			{"Mutated zygomites are automatically dealt",	"the finishing blow if the player has",					"Fungicide spray & Fungicide. @red@(110 @red@Points)"},
			{"Learn to fletch Broad arrows, Broad bolts,",	"and Amethyst broad bolts. @red@(300 @red@Points)", 	""},
			{"Learn to assemble a Slayer helmet, which",	"requires level 55 Crafting. @red@(400 @red@Points)", 	""},
			{"Learn to craft a Slayer ring, which",			"requires level 75 Crafting. @red@(300 @red@Points)",	""},
			{"Duradel and Nieve will be able to assign",	"you Red dragons asyour task. @red@(50 @red@Points)",	""},
			{"Duradel and Nieve will be able to assign",	"you Mithril dragons as your task.",					"@red@(80 @red@Points)"},
			{"Duradel, Nieve and Chaeldar will be able",	"to assign you Aviansies as your task.",				"@red@(80 @red@Points)"},
			{"Duradel, Nieve and Chaeldar will be able",	"to assign TzHaar as your task including",				"TzTok-Jad. @red@(100 @red@Points)"},
			{"Duradel, Nieve, and Chaeldar will be able",	"to assign Lizardmen as your task.",					"@red@(75 @red@Points)"},
			{"Duradel and Nieve will be able to assign",	"boss monsters as your task, excluding the",			"Corporeal Beast. @red@(200 @red@Points)"},
			{"Increase the risk against certain slayer",	"monsters with the chance of a superior",				"version. @red@(150 @red@Points)"},
			{"Learn how to combine a KBD head with your",	"slayer helm to colour it black.",						"@red@(1000 @red@Points)"},
			{"Learn how to combine a Kalphite Queen head",	"with your slayer helm to colour it green.",			"@red@(1000 @red@Points)"},
			{"Learn how to combine an Abyssal Demon head",	"with your slayer helm to colour it red.",				"@red@(1000 @red@Points)"},
			{"Learn how to combine a Dark Claw with your",	"slayer helm to colour it purple.",						"@red@(1000 @red@Points)"},
			{"Learn how to combine Vorkath's head with",	"your slayer helm to colour it turquoise.",				"@red@(1000 @red@Points)"},
			{"Mithril dragons drop mithril bars in",		"banknote form while killed on assignment.",			"@red@(200 @red@Points)"},
			{"Stops you getting Fossil Island Wyvern",		"tasks, without counting towards your",					"blocked task limit. @red@(500 @red@Points)"}
		};

		scrollWidget.scrollPosition = 0;
		scrollWidget.contentType = 0;
		scrollWidget.width = 462;
		scrollWidget.height = 225;
		scrollWidget.scrollMax = (int)Math.ceil(((sprites.length/2)))*112;
		
		scrollWidget.totalChildren(sprites.length*7);
		
		int childCurr = curr;
		int x = 7, y = 0;
		int startConfig = 1550;
		
		int TOTAL_UNLOCKS = sprites.length;
		int id = 0;
		for (int i = 0; i < TOTAL_UNLOCKS; i++) {
			if (i > 0) {
				if (i % 2 == 0) {
					y+=87;
					x=7;
				} else {
					x+=227;
				}
			}

			hoverButton(curr++, "Unlock", 734, 735);//Container
			addSpriteLoader(curr++, sprites[i]);//Image
			//addConfigSprite(curr++, 133, 134, 0, startConfig++);//Checkbox
			addConfigButton(curr++, parent, 133, 134, 14, 15, "", 0, 1, startConfig++);
			
			addText(curr++, unlocks[i], gf, 0, 0xFF9900, true);//Title

//			String[] tmp = MiscUtils.wrap(details[i], 42, System.lineSeparator(), false).split(System.lineSeparator());
//			String[] lines = new String[3];
//			for (int i2 = 0; i2 <= 2; i2++) {
//				if (i2 < tmp.length) {
//					lines[i2] = tmp[i2] == null ? "" : tmp[i2];
//				} else {
//					lines[i2] = "";
//				}
//			}
			
			addText(curr++, details[i][0], gf, 0, 0xFF9900);//Description Line 1
			addText(curr++, details[i][1], gf, 0, 0xFF9900);//Description Line 2
			addText(curr++, details[i][2], gf, 0, 0xFF9900);//Description Line 3
			
			scrollWidget.child(id++, childCurr++, x, y);//Container
			scrollWidget.child(id++, childCurr++, x+4, y+10);//Image
			scrollWidget.child(id++, childCurr++, x+42, y+15);//Checkbox
			
			scrollWidget.child(id++, childCurr++, x+(224/2)+25, y+15);//Title

			scrollWidget.child(id++, childCurr++, x+4, y+42);//Description Line 1
			scrollWidget.child(id++, childCurr++, x+4, y+52);//Description Line 2
			scrollWidget.child(id++, childCurr++, x+4, y+62);//Description Line 3
		}
	}
	
	
	//47600
	public static void extendScroll(GameFont[] gf, int tab) {
		int curr = SCROLL_STARTS[tab];
		int parent = SCROLL_CONTAINERS[tab];
		Widget scrollWidget = addTabInterface(parent);

		int[] sprites = {
			757, 758,
			759, 760,
			761, 762,
			763, 764,
			765, 766,
			767, 768,
			769, 770,
			771, 772,
			773, 774,
			775, 776,
			777, 778,
			779
		};
		
		String[] unlocks = {
			"Need more darkness", "Ankou very much",
			"Suq-a-nother one", "Fire & Darkness",
			"Pedal to the metals", "I really mith you",
			"Ada'mind some more", "RUUUUUNE",
			"Spiritual fervour", "Birds of a Feather",
			"Greater challenge", "It's dark in here",
			"Bleed me dry", "Smell ya later",
			"Horrorific", "To dust you shall return",
			"Wyver-nother one", "Get smashed",
			"Nechs please", "Augment my abbies",
			"Krack on", "Get scabaright on it",
			"Wyver-nother two"
		};
		
		String[] details = {
			"Amount of dark beasts assigned is increased to 100-149. @red@(100 Points)",
			"Amount of ankous assigned is increased to 91-149. @red@(100 Points)",
			"Amount of suqahs assigned is increased to 185-250. @red@(100 Points)",
			"Amount of black dragons assigned is increased to 40-60. @red@(50 Points)",
			"Amount of bronze, iron or steel dragons assigned is increased to 30-50, 61-100 and 40-60, respectively. @red@(100 Points)",
			"Amount of Mithril dragons assigned is increased to 10-30. @red@(120 Points)",
			"Amount of Adamant dragons assigned is increased to 20-30. @red@(100 Points)",
			"Amount of Rune dragons assigned is increased to 30-60. @red@(100 Points)",
			"Amount of Spiritual mages assigned is increased to 181-250. @red@(100 Points)",
			"Amount of aviansies assigned is increased to 130-250. @red@(100 Points)",
			"Amount of Greater demons assigned is increased to 150-200. @red@(100 Points)",
			"Amount of Black demons assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Bloodvelds assigned is increased to 200-250. @red@(75 Points)",
			"Amount of Aberrant spectres assigned is increased to 200-250 @red@(100 Points)",
			"Amount of Cave horrors assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Dust devils assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Skeletal Wyverns assigned is increased to 50-70. @red@(100 Points)",
			"Amount of Gargoyles assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Nechryaels assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Abyssal demons assigned is increased to 200-250. @red@(100 Points)",
			"Amount of Cave krakens assigned is increased to 150-200. @red@(100 Points)",
			"Amount of Scabarites assigned is increased. @red@(50 Points)",
			"Amount of Fossil Island wyverns assigned is increased to 60-???. @red@(100 Points)"
		};

		scrollWidget.scrollPosition = 0;
		scrollWidget.contentType = 0;
		scrollWidget.width = 462;
		scrollWidget.height = 225;
		scrollWidget.scrollMax = (int)Math.ceil(((sprites.length/2)))*112;
		
		scrollWidget.totalChildren(sprites.length*7);
		
		int childCurr = curr;
		int x = 7, y = 0;
		int startConfig = 1700;
		
		int TOTAL_UNLOCKS = sprites.length;
		int id = 0;
		for (int i = 0; i < TOTAL_UNLOCKS; i++) {
			if (i > 0) {
				if (i % 2 == 0) {
					y+=87;
					x=7;
				} else {
					x+=227;
				}
			}

			hoverButton(curr++, "Extend", 734, 735);//Container
			addSpriteLoader(curr++, sprites[i]);//Image
			//addConfigSprite(curr++, 133, 134, 0, startConfig++);//Checkbox
			addConfigButton(curr++, parent, 133, 134, 14, 15, "", 0, 1, startConfig++);
			
			addText(curr++, unlocks[i], gf, 0, 0xFF9900, true);//Title

			String[] tmp = MiscUtils.wrap(details[i], 42, System.lineSeparator(), false).split(System.lineSeparator());
			String[] lines = new String[3];
			for (int i2 = 0; i2 <= 2; i2++) {
				if (i2 < tmp.length) {
					lines[i2] = tmp[i2] == null ? "" : tmp[i2];
				} else {
					lines[i2] = "";
				}
			}
			
			addText(curr++, lines[0], gf, 0, 0xFF9900);//Description Line 1
			addText(curr++, lines[1], gf, 0, 0xFF9900);//Description Line 2
			addText(curr++, lines[2], gf, 0, 0xFF9900);//Description Line 3
			
			scrollWidget.child(id++, childCurr++, x, y);//Container
			scrollWidget.child(id++, childCurr++, x+4, y+10);//Image
			scrollWidget.child(id++, childCurr++, x+42, y+15);//Checkbox
			
			scrollWidget.child(id++, childCurr++, x+(224/2)+25, y+15);//Title

			scrollWidget.child(id++, childCurr++, x+4, y+42);//Description Line 1
			scrollWidget.child(id++, childCurr++, x+4, y+52);//Description Line 2
			scrollWidget.child(id++, childCurr++, x+4, y+62);//Description Line 3
		}
	}
	/*
	//47700
	public static void buyScroll(GameFont[] gf, int parent) {
		Widget widget = addInterface(47700);
		addSprite(47701, 0, "Interfaces/Slayer interface/Buy/buy");
		
		addHoverButton(47702, "Interfaces/Slayer interface/CLOSE", 1, 21, 21, "Close", -1, 47703, 3);
		addHoveredButton2(47703, "Interfaces/Slayer interface/CLOSE", 2, 21, 21, 47704);
		
		addHoverText(27405, "Unlock", "Unlock", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27406, "Extend", "Extend", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27407, "Buy", "Buy", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27408, "Task", "Task", gf, 0, 0xFF9900, true, true, 57);
		
		addText(48505, "1300", gf, 0, 0xFF9900, true, true);
		
		addToItemGroup(47706, 5, 4, 60, 40, true, "Info","Buy 1","Buy 5","Buy 10");
		
		widget.totalChildren(9);
		widget.child(0, 47701, 12, 20);
		widget.child(1, 47702, 472, 27);
		widget.child(2, 47703, 472, 27);
		widget.child(3, 48505, 470, 61);
		widget.child(4, 47706, 58, 81);
		
		widget.child(5, 27405, 52-15, 61);
		widget.child(6, 27406, 134-12, 61);
		widget.child(7, 27407, 228-19, 61);
		widget.child(8, 27408, 308-14, 61);
	}
	
	//47800
	public static void taskScroll(GameFont[] gf, int parent) {
		Widget widget = addInterface(47800);
		addSprite(47801, 0, "Interfaces/Slayer interface/Task/task");
		
		addHoverButton(47802, "Interfaces/Slayer interface/CLOSE", 1, 21, 21, "Close", -1, 47803, 3);
		addHoveredButton2(47803, "Interfaces/Slayer interface/CLOSE", 2, 21, 21, 47804);
		
		addHoverText(27405, "Unlock", "Unlock", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27406, "Extend", "Extend", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27407, "Buy", "Buy", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(27408, "Task", "Task", gf, 0, 0xFF9900, true, true, 57);
		
		addText(48505, "1300", gf, 0, 0xFF9900, true, true);
		
		addText(48809, "Black Demons x230", gf, 1, 0xFFFFFF, true, true);
		
		addHoverText(48810, "Cancel task", "Cancel", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48811, "Block task", "Block", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48812, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48813, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48814, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48815, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48816, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(48817, "Unblock task", "Unblock task", gf, 0, 0xFF9900, true, true, 57);
		
		addText(48818, "", gf, 1, 0xFFFFFF, true, true);
		addText(48819, "", gf, 1, 0xFFFFFF, true, true);
		addText(48820, "", gf, 1, 0xFFFFFF, true, true);
		addText(48821, "", gf, 1, 0xFFFFFF, true, true);
		addText(48822, "", gf, 1, 0xFFFFFF, true, true);
		addText(48823, "", gf, 1, 0xFFFFFF, true, true);
		
		
		widget.totalChildren(23);
		widget.child(0, 47801, 12, 20);
		widget.child(1, 47802, 472, 27);
		widget.child(2, 47803, 472, 27);
		widget.child(3, 48505, 470, 61);
		
		widget.child(4, 27405, 52-15, 61);
		widget.child(5, 27406, 134-12, 61);
		widget.child(6, 27407, 228-19, 61);
		widget.child(7, 27408, 308-14, 61);
		
		widget.child(8, 48809, 149, 144);
		widget.child(9, 48810, 310, 144);
		widget.child(10, 48811, 410, 144);
		widget.child(11, 48812, 384, 186);
		widget.child(12, 48813, 384, 208);
		widget.child(13, 48814, 384, 230);
		widget.child(14, 48815, 384, 252);
		widget.child(15, 48816, 384, 274);
		widget.child(16, 48817, 384, 296);
		widget.child(17, 48818, 254, 186);
		widget.child(18, 48819, 254, 208);
		widget.child(19, 48820, 254, 230);
		widget.child(20, 48821, 254, 252);
		widget.child(21, 48822, 254, 274);
		widget.child(22, 48823, 254, 296);
	}
	
	//47900
	public static void infoInterface(GameFont[] gf, int parent) {
		Widget widget = addInterface(47900);
		addSpriteLoader(47901, 731);
		
		addHoverButton(47902, "Interfaces/Slayer interface/CLOSE", 1, 21, 21, "Close", -1, 47903, 3);
		addHoveredButton2(47903, "Interfaces/Slayer interface/CLOSE", 2, 21, 21, 47904);
		
		addHoverText(47905, "Back", "Back", gf, 0, 0xFF9900, true, true, 57);
		addHoverText(47906, "Confirm", "Confirm", gf, 0, 0xFF9900, true, true, 57);
		
		widget.totalChildren(15);
		
		int y = 104;
		
		for (int i = 0; i < 10; i++) {
			addText(47907 + i, "", gf, i == 0? 1 : 0, 0xFF9900, true, true);
			
			widget.child(i+5, 47907 + i, 246, y);
			y += 15;
		}
		
		widget.child(0, 47901, 12, 20);
		widget.child(1, 47902, 472, 27);
		widget.child(2, 47903, 472, 27);
		
		widget.child(3, 47905, 161, 265);
		widget.child(4, 47906, 279, 265);
		
	}
	*/
}
