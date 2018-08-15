package com.overload.settings;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;



import com.overload.settings.impl.GameGraphics;
import com.overload.settings.impl.Misc;
import com.overload.settings.impl.UserInterface;
import com.overload.sign.Signlink;

public class AdvancedSettings {
	
	public static LinkedList<Integer> graphics_button_ids = new LinkedList<Integer>();
	
	public static LinkedList<Integer> userinterface_button_ids = new LinkedList<Integer>();
	
	public static LinkedList<Integer> misc_button_ids = new LinkedList<Integer>();

	public static void load() {
		File f = new File(Signlink.getDataDirectory() + File.separator + "AdvancedSettings");
		if(!f.exists()) {
			save();
			return;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));

			String s = "", setting = "", value = "";
			String[] args = null;
			while((s = br.readLine()) != null) {
				args = s.split("=");
				setting = args[0];
				
				if(args.length > 1)
					value = args[1];
				else
					value = "";
				
				switch(setting.toLowerCase()) {
				
				default:
					if(!setting.equals("")) {
						try {
							GameGraphics se = GameGraphics.valueOf(setting);
							boolean b = value.equals("") ? true : Boolean.parseBoolean(value);
							
							se.toggled = b;
							if(se.toggled)
								for(String conflict : se.conflicting) {
									GameGraphics s2 = GameGraphics.valueOf(conflict);
									s2.toggled = false;
								}
							
							UserInterface se1 = UserInterface.valueOf(setting);
							boolean b1 = value.equals("") ? true : Boolean.parseBoolean(value);
							
							se1.toggled = b1;
							if(se1.toggled)
								for(String conflict : se1.conflicting) {
									UserInterface s21 = UserInterface.valueOf(conflict);
									s21.toggled = false;
								}
							
							
							Misc se3 = Misc.valueOf(setting);
							boolean b3 = value.equals("") ? true : Boolean.parseBoolean(value);
							
							se3.toggled = b3;
							if(se3.toggled)
								for(String conflict : se3.conflicting) {
									Misc s23 = Misc.valueOf(conflict);
									s23.toggled = false;
								}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		} catch(Exception e) {
			System.out.println("Failed to read settings to file "+f.getAbsolutePath());
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void save() {
		File f = new File(Signlink.getDataDirectory() + File.separator + "AdvancedSettings.dat");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(f));
			
			for(GameGraphics s : GameGraphics.values()) {
				bw.write(s.name()+"="+s.toggled);
				bw.newLine();
			}
			for(UserInterface s1 : UserInterface.values()) {
				bw.write(s1.name()+"="+s1.toggled);
				bw.newLine();
			}
			for(Misc s2 : Misc.values()) {
				bw.write(s2.name()+"="+s2.toggled);
				bw.newLine();
			}
				
			
		} catch(Exception e) {
			System.out.println("Failed to save settings to file "+f.getAbsolutePath());
			e.printStackTrace();
		} finally {
			try {
				if(bw != null)
					bw.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
