package com.overload.cache.graphics;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.overload.Client;
import com.overload.sign.Signlink;



public class FontArchiver {
	
	private static CustomFont[] fontArchive;
	private static CustomFont[] fontArchive1;
	
	public static CustomFont getFont(int index) {
		if (fontArchive == null) {
			return null;
		}
		return fontArchive[index];
	}
	
	public static CustomFont getFont1(int index) {
		if (fontArchive1 == null) {
			return null;
		}
		return fontArchive1[index];
	}
	
	public static CustomFont getFontLoadig(int index) {
		if (fontArchive1 == null) {
			return null;
		}
		return fontArchive1[index];
	}
	
	public static void loadArchive() throws FileNotFoundException, IOException {
		DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(Signlink.findcachedir() + File.separator + "main_file_fonts.dat")));
		try {
			byte fontCount = in.readByte();
			fontArchive = new CustomFont[fontCount + 1];
			for (int index = 0; index < fontCount; index++) {
				long length = in.readLong();
				byte[] fontData = new byte[(int) length];
				in.readFully(fontData);
				fontArchive[index]  = new CustomFont(fontData);
			}
			fontArchive[0] = new CustomFont(Client.instance, "Calibri", Font.BOLD, 20, true);
			fontArchive[1] = new CustomFont(Client.instance, "Calibri", Font.BOLD, 40, true);
			fontArchive[2] = new CustomFont(Client.instance, "Calibri", Font.BOLD, 14, true);
			fontArchive[3] = new CustomFont(Client.instance, "Calibri", Font.PLAIN, 14, true);
			fontArchive[4] = new CustomFont(Client.instance, "Calibri", Font.PLAIN, 14, true);
		} finally {
			in.close();
			Client.instance.fontsloaded = true;	
		}
	}
	
	public static void loadfont() throws FileNotFoundException, IOException {
		DataInputStream in = new DataInputStream(new GZIPInputStream(Client.instance.getClass().getResourceAsStream("fonts.dat")));
		try {
			byte fontCount = in.readByte();
			fontArchive1 = new CustomFont[fontCount + 1];
			for (int index = 0; index < fontCount; index++) {
				long length = in.readLong();
				byte[] fontData = new byte[(int) length];
				in.readFully(fontData);
				fontArchive1[index]  = new CustomFont(fontData);
			}
			fontArchive1[0] = new CustomFont(Client.instance, "Calibri", Font.BOLD, 20, true);
			fontArchive1[1] = new CustomFont(Client.instance, "Calibri", Font.BOLD, 40, true);
		} finally {
			in.close();
			Client.instance.fontsloaded = true;	
		}
	}
	

	public static void writeFontArchive(String destination) {
		try {
			final CustomFont[] FONTS = new CustomFont[] {
				new CustomFont(Client.instance, "Calibri", Font.BOLD, 20, true),
				new CustomFont(Client.instance, "Calibri", Font.BOLD, 40, true),
				new CustomFont(Client.instance, "Calibri", Font.BOLD, 14, true),
				new CustomFont(Client.instance, "Calibri", Font.PLAIN, 14, true),
				new CustomFont(Client.instance, "Calibri", Font.PLAIN, 14, true),
			};
			DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(destination)));
			try {
				out.writeByte(FONTS.length);
				for (int index = 0; index < FONTS.length; index++) {
					out.writeLong(FONTS[index].toByteArray().length);
					out.write(FONTS[index].toByteArray());
					Client.instance.drawLoadingText(50, "Writing Fontts: " + index + " / " + FONTS.length );
				}
			} finally {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFontArchive2(String destination) {
		try {
			final CustomFont[] FONTS = new CustomFont[] {
				new CustomFont(Client.instance, "Calibri", Font.BOLD, 20, true),
				new CustomFont(Client.instance, "Calibri", Font.PLAIN, 14, true),
			};
			DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(destination)));
			try {
				out.writeByte(FONTS.length);
				for (int index = 0; index < FONTS.length; index++) {
					out.writeLong(FONTS[index].toByteArray().length);
					out.write(FONTS[index].toByteArray());
				}
			} finally {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}
